package com.bkav.edoc.web.scheduler.bean;

import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.entity.EmailPDFRequest;
import com.bkav.edoc.service.database.entity.EmailRequest;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.bkav.edoc.service.database.util.EdocNotificationServiceUtil;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.web.util.FilePDFUtil;
import com.bkav.edoc.web.util.MessageSourceUtil;
import com.bkav.edoc.web.util.PropsUtil;
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;

@Component("sendEmailBean")
public class EmailSenderBean {

    @Autowired
    private MessageSourceUtil messageSourceUtil;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VelocityEngine velocityEngine;

    public void runScheduleSendEmail() {
        try {
            int dateRange = Integer.parseInt(PropsUtil.get("edoc.date.range"));
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -dateRange);
            Date yesterday = cal.getTime();
            Date now = new Date();
            List<EmailRequest> emailSendObject = EdocNotificationServiceUtil.emailScheduleSend(yesterday, now);

            Map<String, Object> mail = null;
            Map<String, Object> mailAdmin = new HashMap<>();
            List<EmailPDFRequest> pdfRequests = new ArrayList<>();
            String adminReceivedName = messageSourceUtil.getMessage("edoc.admin.name", null);
            // put to mail admin object
            mailAdmin.put("receiverName", adminReceivedName);
            mailAdmin.put("TotalOrgan", emailSendObject.size());
            mailAdmin.put("currentDate", DateUtils.format(new Date(), DateUtils.VN_DATE_FORMAT));
            mailAdmin.put("yesterday", DateUtils.format(yesterday, DateUtils.VN_DATE_FORMAT));

            long num_documents = 0;
            int test = 0;
            String edocTitleMailSender = messageSourceUtil.getMessage("edoc.send.mail.title",
                    new Object[]{DateUtils.format(yesterday, DateUtils.VN_DATE_FORMAT),
                            DateUtils.format(new Date(), DateUtils.VN_DATE_FORMAT)});
            for (EmailRequest emailObject : emailSendObject) {
                mail = new HashMap<>();
                num_documents += emailObject.getNumberOfDocument();
                LOGGER.info("Start send email to organ with domain " + emailObject.getReceiverId() + " !!!!!!!");

                EdocDynamicContact contact = EdocDynamicContactServiceUtil.findContactByDomain(emailObject.getReceiverId());
                String receiverEmail = contact.getEmail();

                if (contact.getEmail() == null || contact.getEmail().equals("")) {
                    LOGGER.info("Organ with domain " + contact.getDomain() + " have not email config !!!!!!");
                } else {
                    LOGGER.info("Total " + emailObject.getNumberOfDocument() + " documents not taken by organ " + contact.getDomain() + " !!!!!!!!");
                    // put to mail organ
                    mail.put("receiverName", contact.getName());
                    mail.put("TotalDocument", emailObject.getNumberOfDocument());
                    mail.put("currentDate", DateUtils.format(new Date(), DateUtils.VN_DATE_FORMAT));
                    mail.put("yesterday", DateUtils.format(yesterday, DateUtils.VN_DATE_FORMAT));

                    // write document to pdf
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    FilePDFUtil.WriteDocumentsToPDF(emailObject.getEdocDocument(), outputStream, contact.getName());
                    byte[] bytes = outputStream.toByteArray();

                    // create email pdf content object
                    EmailPDFRequest emailPDFRequest = new EmailPDFRequest();
                    emailPDFRequest.setOrganName(contact.getName());
                    emailPDFRequest.setBytes(bytes);
                    pdfRequests.add(emailPDFRequest);

                    // send mail to each organ
                    sendEmailToOrgans(edocTitleMailSender, null,
                            PropsUtil.get("mail.to.address"),
                            receiverEmail, mail, bytes);
                    LOGGER.info("Send email to organ with id " + emailObject.getReceiverId() + " ended !!!!!!");
                }

                // test run 2 times
                /*test++;
                if (test == 2)
                    break;*/
            }
            LOGGER.info("Start send email to admin!!!!!");
            mailAdmin.put("TotalDocuments", num_documents);
            // send mail to admin mail
            sendEmailToAdmin(edocTitleMailSender, null,
                    PropsUtil.get("mail.to.address"),
                    PropsUtil.get("admin.mail.username"), mailAdmin, pdfRequests);
            LOGGER.info("Send email to admin ended!!!");
        } catch (Exception e) {
            LOGGER.error("Error to send email because " + e);
        }
    }

    // merge content to template for each organ
    private void sendEmailToOrgans(final String subject, final String message,
                                   final String fromEmailAddress, final String toEmailAddress, final Map<String, Object> mailRequest, final byte[] bytes) {
        //EmailConfig emailConfig = new EmailConfig();
        String attachmentName = messageSourceUtil.getMessage("edoc.attachment.name.send.mail",
                new Object[]{mailRequest.get("receiverName"), DateUtils.format(new Date(), DateUtils.VN_DATE_FORMAT)});

        MimeMessagePreparator preparatory = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, CharEncoding.UTF_8);
                InputStream is = new ByteArrayInputStream(bytes);
                message.setTo(toEmailAddress);
                message.setFrom(new InternetAddress(fromEmailAddress));
                message.addAttachment(attachmentName, new ByteArrayResource(IOUtils.toByteArray(is)));

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
            mailSender.send(preparatory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // merge content to template for admin
    private void sendEmailToAdmin(final String subject, final String message,
                                  final String fromEmailAddress, final String toEmailAddress, final Map<String, Object> mailRequest, final List<EmailPDFRequest> pdfRequests) {
        //EmailConfig emailConfig = new EmailConfig();

        MimeMessagePreparator perpetrator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, CharEncoding.UTF_8);
                message.setTo(toEmailAddress);
                message.setFrom(new InternetAddress(fromEmailAddress));
                for (EmailPDFRequest pdfRequest : pdfRequests) {
                    InputStream is = new ByteArrayInputStream(pdfRequest.getBytes());
                    String attachmentName = messageSourceUtil.getMessage("edoc.attachment.name.send.mail",
                            new Object[]{pdfRequest.getOrganName(), DateUtils.format(new Date(), DateUtils.VN_DATE_FORMAT)});
                    message.addAttachment(attachmentName, new ByteArrayResource(IOUtils.toByteArray(is)));
                }

                VelocityContext velocityContext = new VelocityContext();
                velocityContext.put("mailRequest", mailRequest);
                LOGGER.info(velocityContext.get("mailRequest"));

                StringWriter stringWriter = new StringWriter();

                velocityEngine.mergeTemplate("velocity/admin-email-template.vm", "UTF-8", velocityContext, stringWriter);

                message.setSubject(subject);
                message.setText(stringWriter.toString(), true);
            }
        };

        try {
            mailSender.send(perpetrator);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // main test
//    public static void main(String[] args) {
//        System.out.println("Start...");
//        EmailSenderBean emailSenderBean = new EmailSenderBean();
//        emailSenderBean.runScheduleSendEmail();
//        System.out.println("End!!!!");
//    }

    private final static Logger LOGGER = Logger.getLogger(EmailSenderBean.class);
}
