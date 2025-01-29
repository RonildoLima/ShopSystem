package com.accenture.shopsystem.modulos.email;

import com.accenture.shopsystem.domain.Email.Email;
import com.accenture.shopsystem.dtos.email.EmailDto;
import com.accenture.shopsystem.services.email.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = "email-queue")
    public void listen(@Payload EmailDto emailDto) {
        Email emailModel = new Email ();
        BeanUtils.copyProperties(emailDto, emailModel);
        emailService.sendEmail(emailModel);
        System.out.println("Email Status: " + emailModel.getStatusEmail().toString());
    }
}

