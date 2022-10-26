package com.ecommerce.customer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    @Autowired
    public JavaMailSender emailSender;

    //Tạo method để send email
    public void send(String email, String subject, String content) throws MessagingException {
//        SimpleMailMessage message = new SimpleMailMessage();
        MimeMessage message=emailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,true);

        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(content,true);

        //Send Messgae
        emailSender.send(message);
    }
}
