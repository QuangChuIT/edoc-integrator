package com.bkav.edoc.web.email;

import com.bkav.edoc.service.database.entity.EmailRequest;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.StringWriter;

@Component
public class EmailSender {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    public void sendEmailUsingVelocityTemplate(final String subject, final String message,
                                               final String fromEmailAddress, final String toEmailAddress, EmailRequest emailRequest) {

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setTo(toEmailAddress);
                message.setFrom(new InternetAddress(fromEmailAddress));

                VelocityContext velocityContext = new VelocityContext();
                velocityContext.put("emailRequest", emailRequest);

                StringWriter stringWriter = new StringWriter();

                velocityEngine.mergeTemplate("velocity/email-template.vm", "UTF-8", velocityContext, stringWriter);

                message.setSubject(subject);
                message.setText(stringWriter.toString(), true);
            }
        };

        try {
            mailSender.send(preparator);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
