package com.bkav.edoc.web.scheduler.bean;

import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.entity.EmailRequest;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.bkav.edoc.service.database.util.EdocNotificationServiceUtil;
import org.apache.log4j.Logger;
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
import java.util.List;

@Component ("sendEmailBean")
public class EmailSenderBean {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    public void runScheduleSendEmail() {
        try {
//            Calendar yesterday = Calendar.getInstance();
//            //yesterday.add(Calendar.DATE, -1);
//            //yesterday.add(Calendar.HOUR, 7);
//            _counterDate = yesterday.getTime();
//            LOGGER.info("Email prepare send at " + _counterDate);
            List<EmailRequest> emailSendObject = EdocNotificationServiceUtil.emailScheduleSend();
            int test = 0;
            for (EmailRequest emailObject: emailSendObject) {
                LOGGER.info("Start send email to organ with domain " + emailObject.getReceiverId());
                EdocDynamicContact contact = EdocDynamicContactServiceUtil.findContactByDomain(emailObject.getReceiverId());
                String receiverEmail = contact.getEmail();
                emailObject.setReceiverName(contact.getName());
                sendEmailUsingVelocityTemplate("Thống kê văn bản chưa được nhận về", null, "noreply@bmail.com", "fbt87716@zwoho.com", emailObject);
                LOGGER.info("Has " + emailObject.getNumberOfDocument() + " documents not taken");
                LOGGER.info("Send email to organ with id " + emailObject.getReceiverId() + " successfully!!!");
                test++;
                if (test == 2)
                    break;
            }
        } catch (Exception e) {
            LOGGER.error("Error to send email because "  + e);
        }
    }

    private void sendEmailUsingVelocityTemplate(final String subject, final String message,
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

    private final static Logger LOGGER = Logger.getLogger(EmailSenderBean.class);
}
