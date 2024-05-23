package com.smart.service;

import java.util.Properties;

import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	public boolean sendEmail(String subject,String text,String to) {
		boolean flag = false;
		String from = "techvoyageblog@gmail.com";
		Properties props = new Properties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.host","smtp.gmail.com");
        String username = "techvoyageblog";
        String password = "hfzy fuvb uqjw ryya";
        Session session = Session.getInstance(props,new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username , password); 
            }
        });
        try {
            Message message  = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setFrom(new InternetAddress(from));
            message.setSubject(subject);
//            message.setText(text);
            message.setContent(text, "text/html");
            Transport.send(message);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
	}
}
