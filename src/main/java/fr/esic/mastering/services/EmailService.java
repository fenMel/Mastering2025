package fr.esic.mastering.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import fr.esic.mastering.entities.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendConvocationEmail(User user, String subject, String messageText, byte[] pdfContent, String filename) 
            throws MessagingException {
        
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(messageText, true); // true indicates HTML content
        
        // Add PDF attachment
        helper.addAttachment(filename, new ByteArrayResource(pdfContent));
        
        emailSender.send(message);
    }
}