package com.bkav.edoc.web.email;

import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class EmailConfig {

    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("jvmailsender@gmail.com");
        mailSender.setPassword("123abc@A");

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.smtp.starttls.enable", "true");
        javaMailProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        //javaMailProperties.put("mail.smtp.socketFactory.fallback", true);
        javaMailProperties.put("mail.transport.protocol", "smtp");

        mailSender.setJavaMailProperties(javaMailProperties);

        return mailSender;
    }

    public VelocityEngine velocityEngine() {
        VelocityEngine engine = new VelocityEngine();

        engine.setProperty("resource.loader", "class");
        engine.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        return engine;
    }

}
