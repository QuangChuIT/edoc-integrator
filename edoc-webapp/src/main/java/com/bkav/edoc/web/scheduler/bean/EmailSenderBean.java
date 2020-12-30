package com.bkav.edoc.web.scheduler.bean;

import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.entity.EmailRequest;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.bkav.edoc.service.database.util.EdocNotificationServiceUtil;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.web.util.FilePDFUtil;

import org.apache.commons.codec.CharEncoding;
import org.apache.log4j.Logger;
import org.apache.pdfbox.io.IOUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("sendEmailBean")
public class EmailSenderBean {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    public void runScheduleSendEmail() {
        try {
            List<EmailRequest> emailSendObject = EdocNotificationServiceUtil.emailScheduleSend();
            Map<String, Object> mail = null;
            int test = 0;
            for (EmailRequest emailObject: emailSendObject) {
                mail = new HashMap<>();
                LOGGER.info("Start send email to organ with domain " + emailObject.getReceiverId());
                EdocDynamicContact contact = EdocDynamicContactServiceUtil.findContactByDomain(emailObject.getReceiverId());
                String receiverEmail = contact.getEmail();
                mail.put("receiverName", contact.getName());
                mail.put("TotalDocument", emailObject.getNumberOfDocument());
                mail.put("Date", DateUtils.format(new Date(), DateUtils.VN_DATE_FORMAT));
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                FilePDFUtil.WriteDocumentsToPDF(emailObject.getEdocDocument(), outputStream);
                byte[] bytes = outputStream.toByteArray();
                sendEmailUsingVelocityTemplate("Thống kê văn bản chưa được nhận về tới ngày " + DateUtils.format(new Date(), DateUtils.VN_DATE_FORMAT), null, "jvmailsender@gmail.com",
                        "jvmailsender@gmail.com", mail, bytes);
                LOGGER.info("Has " + emailObject.getNumberOfDocument() + " documents not taken");
                LOGGER.info("Send email to organ with id " + emailObject.getReceiverId() + " successfully!!!");

                // test run 2 times
                test++;
                if (test == 2)
                    break;
            }
        } catch (Exception e) {
            LOGGER.error("Error to send email because "  + e);
        }
    }

    private void sendEmailUsingVelocityTemplate(final String subject, final String message,
                                                final String fromEmailAddress, final String toEmailAddress, final Map<String, Object> mailRequest, final byte[] bytes) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, CharEncoding.UTF_8);
                InputStream is = new ByteArrayInputStream(bytes);
                message.setTo(toEmailAddress);
                message.setFrom(new InternetAddress(fromEmailAddress));
                message.addAttachment("Báo_cáo_văn_bản_chưa_nhận_về_" + mailRequest.get("receiverName") +
                        "_" + DateUtils.format(new Date(), DateUtils.VN_DATE_FORMAT) + ".pdf", new ByteArrayResource(IOUtils.toByteArray(is)));

                VelocityContext velocityContext = new VelocityContext();
                velocityContext.put("mailRequest", mailRequest);
                LOGGER.info(velocityContext.get("mailRequest"));

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

    public static void main(String[] args) {
        System.out.println("Start...");
        EmailSenderBean emailSenderBean = new EmailSenderBean();
        emailSenderBean.runScheduleSendEmail();
        System.out.println("End!!!!");
    }

    private final static Logger LOGGER = Logger.getLogger(EmailSenderBean.class);
}
