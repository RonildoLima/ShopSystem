package com.accenture.shopsystem.modulos.email;

import com.accenture.shopsystem.domain.Email.Email;
import com.accenture.shopsystem.domain.Enums.StatusEmail;
import com.accenture.shopsystem.dtos.email.EmailDto;
import com.accenture.shopsystem.services.email.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EmailConsumerTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailConsumer emailConsumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listen_DeveEnviarEmailComSucesso() {
        EmailDto emailDto = new EmailDto();
        emailDto.setEmailTo("destinatario@example.com");
        emailDto.setEmailFrom("remetente@example.com");
        emailDto.setSubject("Teste");
        emailDto.setText("Conteúdo do e-mail");

        // Criamos um e-mail com status definido
        Email emailRetornado = new Email();
        emailRetornado.setEmailTo(emailDto.getEmailTo());
        emailRetornado.setEmailFrom(emailDto.getEmailFrom());
        emailRetornado.setSubject(emailDto.getSubject());
        emailRetornado.setText(emailDto.getText());
        emailRetornado.setStatusEmail(StatusEmail.SENT);

        // Simula a chamada do serviço para retornar um email com status preenchido
        when(emailService.sendEmail(any(Email.class))).thenAnswer(invocation -> {
            Email emailRecebido = invocation.getArgument(0);
            emailRecebido.setStatusEmail(StatusEmail.SENT);
            return emailRecebido;
        });

        emailConsumer.listen(emailDto);

        ArgumentCaptor<Email> captor = ArgumentCaptor.forClass(Email.class);
        verify(emailService, times(1)).sendEmail(captor.capture());

        Email emailCapturado = captor.getValue();
        assertEquals(emailDto.getEmailTo(), emailCapturado.getEmailTo());
        assertEquals(emailDto.getEmailFrom(), emailCapturado.getEmailFrom());
        assertEquals(emailDto.getSubject(), emailCapturado.getSubject());
        assertEquals(emailDto.getText(), emailCapturado.getText());

        // Verifica se o status foi preenchido corretamente
        assertEquals(StatusEmail.SENT, emailCapturado.getStatusEmail());
    }
}
