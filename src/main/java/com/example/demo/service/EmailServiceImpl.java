package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

  @Autowired
  private JavaMailSender mailSender;

  @Value("${spring.mail.defaultFrom}")
  private String defaultFrom;
  @Value("${spring.mail.host}")
  private String host;
  @Value("${spring.mail.port}")
  private int port;
  @Value("${spring.mail.username}")
  private String username;
  @Value("${spring.mail.password}")
  private String password;

  @Bean
  public JavaMailSender getJavaMailSender() {

    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    mailSender.setHost("smtp.gmail.com");
    mailSender.setPort(587);
    mailSender.setUsername("ksatish.narola@gmail.com");
    mailSender.setPassword("4c0j1h1n06caW1x");


    Properties properties = mailSender.getJavaMailProperties();
    properties.put("mail.transport.protocol", "smtp");
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.debug", "true");

    return mailSender;
  }

  @Override
  public void sendMail(String to, String subject, String body) {

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = null;
    try {
      helper = new MimeMessageHelper(message, true);
      helper.setSubject(subject);
      helper.setText(body, true);
      helper.setTo(to);
      helper.setFrom(defaultFrom, "noreply@planplus.com");

    } catch (MessagingException | UnsupportedEncodingException e) {
      LOGGER.error("Error while sending email! {}", e.getMessage());
    }

    mailSender.send(message);

  }
}
