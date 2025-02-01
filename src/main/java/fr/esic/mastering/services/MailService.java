package fr.esic.mastering.services;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
	@Autowired
    private JavaMailSender mailSender;
	public void envoyerConvocation(String to, String sujet, String contenu, File fichierPdf) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(sujet);
        helper.setText(contenu, true); // true pour HTML
        helper.addAttachment("convocation.pdf", fichierPdf);
        mailSender.send(message);
}
}