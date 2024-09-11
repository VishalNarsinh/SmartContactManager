package com.scm.services.impl;

import com.scm.services.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
//        mailSender
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);
        simpleMailMessage.setFrom("vishalnarsinh@gmail.com");
        mailSender.send(simpleMailMessage);
    }

    @Override
    public void sendHtmlEmail(String to, String subject, String body) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(body, true); // Set to true for HTML
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("vishalnarsinh@gmail.com");
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            // Handle the exception
            e.printStackTrace();
        }
    }

    @Override
    public void sendAttachmentsEmail(String to, String subject, String body) {

    }
}
