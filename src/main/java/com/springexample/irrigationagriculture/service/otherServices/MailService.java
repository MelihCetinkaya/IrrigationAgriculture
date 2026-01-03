package com.springexample.irrigationagriculture.service.otherServices;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Value("${spring.mail.username}")
    private String mail;

    private final MailSender mailSender;

    public MailService(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String message){

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(mail);
        mailMessage.setSubject("Bildirim");
        mailMessage.setText(message);
        mailMessage.setFrom(mail);

        mailSender.send(mailMessage);
    }
}
