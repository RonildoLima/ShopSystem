package com.accenture.shopsystem.domain.Email;

import com.accenture.shopsystem.domain.Enums.StatusEmail;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void testGettersAndSetters() {
        UUID emailId = UUID.randomUUID();
        String ownerRef = "User123";
        String emailFrom = "remetente@example.com";
        String emailTo = "destinatario@example.com";
        String subject = "Assunto Teste";
        String text = "Conteúdo do e-mail";
        LocalDateTime sendDateEmail = LocalDateTime.now();
        StatusEmail statusEmail = StatusEmail.SENT;

        Email email = new Email();
        email.setEmailId(emailId);
        email.setOwnerRef(ownerRef);
        email.setEmailFrom(emailFrom);
        email.setEmailTo(emailTo);
        email.setSubject(subject);
        email.setText(text);
        email.setSendDateEmail(sendDateEmail);
        email.setStatusEmail(statusEmail);

        assertEquals(emailId, email.getEmailId());
        assertEquals(ownerRef, email.getOwnerRef());
        assertEquals(emailFrom, email.getEmailFrom());
        assertEquals(emailTo, email.getEmailTo());
        assertEquals(subject, email.getSubject());
        assertEquals(text, email.getText());
        assertEquals(sendDateEmail, email.getSendDateEmail());
        assertEquals(statusEmail, email.getStatusEmail());
    }

    @Test
    void testEquals() {
        UUID emailId = UUID.randomUUID();

        Email email1 = new Email();
        email1.setEmailId(emailId);
        email1.setEmailTo("destinatario@example.com");

        Email email2 = new Email();
        email2.setEmailId(emailId);
        email2.setEmailTo("destinatario@example.com");

        Email email3 = new Email();
        email3.setEmailId(UUID.randomUUID());
        email3.setEmailTo("outro@example.com");

        assertEquals(email1, email2);
        assertNotEquals(email1, email3);
    }

    @Test
    void testHashCode() {
        UUID emailId = UUID.randomUUID();

        Email email1 = new Email();
        email1.setEmailId(emailId);

        Email email2 = new Email();
        email2.setEmailId(emailId);

        assertEquals(email1.hashCode(), email2.hashCode());
    }

    @Test
    void testToString() {
        UUID emailId = UUID.randomUUID();
        String emailFrom = "remetente@example.com";
        String emailTo = "destinatario@example.com";
        String subject = "Teste Assunto";
        String text = "Teste de mensagem";
        LocalDateTime sendDateEmail = LocalDateTime.now();
        StatusEmail statusEmail = StatusEmail.SENT;

        Email email = new Email();
        email.setEmailId(emailId);
        email.setEmailFrom(emailFrom);
        email.setEmailTo(emailTo);
        email.setSubject(subject);
        email.setText(text);
        email.setSendDateEmail(sendDateEmail);
        email.setStatusEmail(statusEmail);

        String expectedToString = "Email(emailId=" + emailId +
                ", ownerRef=null, emailFrom=" + emailFrom +
                ", emailTo=" + emailTo +
                ", subject=" + subject +
                ", text=" + text +
                ", sendDateEmail=" + sendDateEmail +
                ", statusEmail=" + statusEmail + ")";

        assertEquals(expectedToString, email.toString());
    }
}
