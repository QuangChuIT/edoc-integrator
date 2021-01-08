package com.bkav.edoc.web.email;

import java.util.Properties;

import com.bkav.edoc.web.util.PropsUtil;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class EmailConfig {

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(PropsUtil.get("mail.host"));
        mailSender.setPort(Integer.parseInt(PropsUtil.get("mail.port"))); // 587 465
        /*mailSender.setUsername(PropsUtil.get("mail.username"));
        mailSender.setPassword(PropsUtil.get("mail.password"));*/

        Properties javaMailProperties = new Properties();
        /*javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put(PropsUtil.get("mail.starttls"), "true");*/
        javaMailProperties.put("mail.smtp.ssl.trust", PropsUtil.get("mail.host"));

        mailSender.setJavaMailProperties(javaMailProperties);

        return mailSender;
    }

    @Bean
    public VelocityEngine velocityEngine() {
        VelocityEngine engine = new VelocityEngine();

        engine.setProperty("resource.loader", "class");
        engine.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        return engine;
    }

}
