package com.accenture.shopsystem.services.email;

import com.accenture.shopsystem.domain.Email.Email;
import com.accenture.shopsystem.domain.Enums.StatusEmail;
import com.accenture.shopsystem.dtos.email.EmailDto;
import com.accenture.shopsystem.repositories.EmailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private static RabbitTemplate rabbitTemplate = new RabbitTemplate();

    public EmailService(RabbitTemplate rabbitTemplate) {
        EmailService.rabbitTemplate = rabbitTemplate;
        logger.info("EmailService inicializado com RabbitTemplate.");
    }

    @Autowired
    EmailRepository emailRepository;



    @Autowired
    JavaMailSender emailSender;

    public static void enviarEmail(EmailDto emailDto) {
    	logger.info("Enviando e-mail para a fila: email-queue.");
        logger.debug("Detalhes do e-mail: {}", emailDto);
        rabbitTemplate.convertAndSend("email-queue", emailDto);
        logger.info("E-mail enviado para a fila com sucesso.");
    }

    @Transactional
    public Email sendEmail(Email emailModel) {
    	emailModel.setSendDateEmail(LocalDateTime.now());

        logger.info("Iniciando envio de e-mail...");
        logger.debug("Dados do e-mail: De: {}, Para: {}, Assunto: {}", 
            emailModel.getEmailFrom(), emailModel.getEmailTo(), emailModel.getSubject());

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailModel.getEmailFrom());
            message.setTo(emailModel.getEmailTo());
            message.setSubject(emailModel.getSubject());
            message.setText(emailModel.getText());

            logger.info("Enviando e-mail via SMTP...");
            emailSender.send(message);
            emailModel.setStatusEmail(StatusEmail.SENT);
            logger.info("E-mail enviado com sucesso!");

        } catch (MailException e) {
            emailModel.setStatusEmail(StatusEmail.ERROR);
            logger.error("Erro ao enviar o e-mail: {}", e.getMessage(), e);
        } finally {
            Email savedEmail = emailRepository.save(emailModel);
            logger.info("Registro do e-mail salvo no banco de dados com status: {}", savedEmail.getStatusEmail());
            return savedEmail;
        }
    }

    public Page<Email> findAll(Pageable pageable) {
    	logger.info("Buscando todos os e-mails paginados...");
        Page<Email> emails = emailRepository.findAll(pageable);
        logger.info("Total de e-mails encontrados: {}", emails.getTotalElements());
        return emails;
    }

    public Optional<Email> findById(UUID emailId) {
        logger.info("Buscando e-mail com ID: {}", emailId);
        Optional<Email> email = emailRepository.findById(emailId);
        if (email.isPresent()) {
            logger.info("E-mail encontrado: {}", email.get());
        } else {
            logger.warn("E-mail com ID {} não encontrado.", emailId);
        }
        return email;
    }
}
