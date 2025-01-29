package com.accenture.shopsystem.services.email;

import com.accenture.shopsystem.domain.Email.Email;
import com.accenture.shopsystem.domain.Enums.StatusEmail;
import com.accenture.shopsystem.dtos.email.EmailDto;
import com.accenture.shopsystem.repositories.EmailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private EmailRepository emailRepository;

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private RabbitTemplate rabbitTemplate;

    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailService(rabbitTemplate);
        emailService.emailRepository = emailRepository;
        emailService.emailSender = emailSender;
    }

    @Test
    void enviarEmail_DeveEnviarMensagemParaFila() {
        EmailDto emailDto = new EmailDto();
        emailDto.setEmailTo("destinatario@example.com");
        emailDto.setEmailFrom("remetente@example.com");
        emailDto.setSubject("Teste");
        emailDto.setText("Conteúdo do e-mail");

        EmailService.enviarEmail(emailDto);

        verify(rabbitTemplate, times(1)).convertAndSend(eq("email-queue"), eq(emailDto));
    }

    @Test
    void sendEmail_DeveEnviarEmailComSucesso() {
        Email email = new Email();
        email.setEmailId(UUID.randomUUID());
        email.setEmailTo("destinatario@example.com");
        email.setEmailFrom("remetente@example.com");
        email.setSubject("Teste");
        email.setText("Conteúdo do e-mail");
        email.setSendDateEmail(LocalDateTime.now());

        when(emailRepository.save(any(Email.class))).thenAnswer(invocation -> {
            Email savedEmail = invocation.getArgument(0);
            savedEmail.setStatusEmail(StatusEmail.SENT);
            return savedEmail;
        });

        Email result = emailService.sendEmail(email);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(emailSender, times(1)).send(captor.capture());

        assertEquals(StatusEmail.SENT, result.getStatusEmail());
        assertEquals(email.getEmailTo(), captor.getValue().getTo()[0]);
        assertEquals(email.getEmailFrom(), captor.getValue().getFrom());
        assertEquals(email.getSubject(), captor.getValue().getSubject());
        assertEquals(email.getText(), captor.getValue().getText());
    }

    @Test
    void sendEmail_DeveRegistrarErroAoFalharEnvio() {
        Email email = new Email();
        email.setEmailId(UUID.randomUUID());
        email.setEmailTo("destinatario@example.com");
        email.setEmailFrom("remetente@example.com");
        email.setSubject("Teste");
        email.setText("Conteúdo do e-mail");
        email.setSendDateEmail(LocalDateTime.now());

        doThrow(new MailException("Falha no envio") {}).when(emailSender).send(any(SimpleMailMessage.class));

        when(emailRepository.save(any(Email.class))).thenAnswer(invocation -> {
            Email savedEmail = invocation.getArgument(0);
            savedEmail.setStatusEmail(StatusEmail.ERROR);
            return savedEmail;
        });

        Email result = emailService.sendEmail(email);

        assertEquals(StatusEmail.ERROR, result.getStatusEmail());
        verify(emailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(emailRepository, times(1)).save(any(Email.class));
    }

    @Test
    void findAll_DeveRetornarListaDeEmailsPaginada() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Email> emails = List.of(new Email(), new Email());
        Page<Email> emailPage = new PageImpl<>(emails, pageable, emails.size());

        when(emailRepository.findAll(pageable)).thenReturn(emailPage);

        Page<Email> result = emailService.findAll(pageable);

        assertEquals(2, result.getTotalElements());
        verify(emailRepository, times(1)).findAll(pageable);
    }

    @Test
    void findById_DeveRetornarEmailQuandoEncontrado() {
        UUID emailId = UUID.randomUUID();
        Email email = new Email();
        email.setEmailId(emailId);

        when(emailRepository.findById(emailId)).thenReturn(Optional.of(email));

        Optional<Email> result = emailService.findById(emailId);

        assertTrue(result.isPresent());
        assertEquals(emailId, result.get().getEmailId());
        verify(emailRepository, times(1)).findById(emailId);
    }

    @Test
    void findById_DeveRetornarVazioQuandoNaoEncontrado() {
        UUID emailId = UUID.randomUUID();

        when(emailRepository.findById(emailId)).thenReturn(Optional.empty());

        Optional<Email> result = emailService.findById(emailId);

        assertFalse(result.isPresent());
        verify(emailRepository, times(1)).findById(emailId);
    }
}
