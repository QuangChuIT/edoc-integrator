package com.bkav.edoc.web.scheduler.bean;

import com.bkav.edoc.service.database.entity.*;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.bkav.edoc.service.database.util.EdocNotificationServiceUtil;
import com.bkav.edoc.service.database.util.MailReceiverAdminServiceUtil;
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
import java.text.Normalizer;
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

            if (emailSendObject.size() == 0) {
                LOGGER.info("ALL OF ORGANIZATION TAKEN DOCUMENT!!!!!!!");
            } else {
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
                    LOGGER.info(emailObject.toString());
                    num_documents += emailObject.getNumberOfDocument();
                    LOGGER.info("Start send email to organ with domain " + emailObject.getReceiverId() + " !!!!!!!");
                    EdocDynamicContact contact = EdocDynamicContactServiceUtil.findContactByDomain(emailObject.getReceiverId());
                    boolean is_notify = contact.getReceiveNotify();
                    if (is_notify) {
                        if (emailObject.getNumberOfDocument() > 0) {
                            Set<User> users = contact.getUsers();
                            if (users.size() > 0) {
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
                                for (User user : users) {
                                    String email = user.getEmailAddress();
                                    if (email != null && !email.equals("")) {
                                        LOGGER.info("-------------- Prepare send mail to email " + email + " of organ "
                                                + contact.getDomain() + "----------------------------");

                                        // send mail to each organ
                                        /*sendEmailToOrgans(edocTitleMailSender, null,
                                                PropsUtil.get("mail.to.address"),
                                                email, mail, bytes);

                                         */
                                        LOGGER.info("-------------- Send email to organ with id " + emailObject.getReceiverId() + " ended ----------------");
                                    } else {
                                        LOGGER.error("Organization with domain " + contact.getDomain() + " with user "
                                                + user.getUsername() + " don't have mail config !!!!!!!!!!!!!");
                                    }
                                }
                            }
                        }
                    }
                    // test run 2 times
                    /*test++;
                    if (test == 2)
                        break;*/
                }
                if (num_documents > 0) {
                    LOGGER.info("--------------------- Start send email to admin ------------------------");
                    mailAdmin.put("TotalDocuments", num_documents);
                    // send mail to admin mail
                    /*sendEmailToAdmin(edocTitleMailSender, null,
                            PropsUtil.get("mail.to.address"),
                            PropsUtil.get("admin.mail.username"), mailAdmin, pdfRequests);*/
                    List<MailReceiverAdmin> mailReceivers = MailReceiverAdminServiceUtil.getAllMailReceiver();
                    for (MailReceiverAdmin mailReceiver: mailReceivers) {
                        String emailAdress = mailReceiver.getEmailAddress();
                        LOGGER.info("Start send email to " + emailAdress + "!!!!!!!!!!");
                        sendEmailToAdmin(edocTitleMailSender, null,
                                PropsUtil.get("mail.to.address"),
                                emailAdress, mailAdmin, pdfRequests);
                    }
                    LOGGER.info("------------------------------- Send email to admin ended -----------------------------");
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error to send email cause " + Arrays.toString(e.getStackTrace()));
        }
    }

    // merge content to template for each organ
    private void sendEmailToOrgans(final String subject, final String message,
                                   final String fromEmailAddress, final String toEmailAddress, final Map<String, Object> mailRequest, final byte[] bytes) {
        //EmailConfig emailConfig = new EmailConfig();
        String dateFormat = DateUtils.format(new Date(), DateUtils.VN_DATE_FORMAT);
        dateFormat = dateFormat.replaceAll("/", "_");
        String attachmentName = messageSourceUtil.getMessage("edoc.attachment.name.send.mail",
                new Object[]{encodeForUrl(mailRequest.get("receiverName").toString()), dateFormat});
        LOGGER.info("Attachment name " + attachmentName);
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
            LOGGER.error("Error send mail to mail " + toEmailAddress + " cause " + e.getMessage());
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
                    String dateFormat = DateUtils.format(new Date(), DateUtils.VN_DATE_FORMAT);
                    dateFormat = dateFormat.replaceAll("/", "_");
                    String attachmentName = messageSourceUtil.getMessage("edoc.attachment.name.send.mail",
                            new Object[]{encodeForUrl(pdfRequest.getOrganName()), dateFormat});
                    LOGGER.info("Attachment name " + attachmentName);
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
            LOGGER.error("Error send mail to admin " + toEmailAddress + " cause " + e.getMessage());
        }
    }

    public static String encodeForUrl(String input) {
        StringBuilder sb = new StringBuilder(input.length());
        input = Normalizer.normalize(input, Normalizer.Form.NFD);
        for (char c : input.toCharArray()) {
            if (c <= '\u007F') sb.append(c);
        }
        return sb.toString().replaceAll(" ", "_");
    }

    public static void main(String[] args) {
        System.out.println("Start...");
        EmailSenderBean emailSenderBean = new EmailSenderBean();
        emailSenderBean.runScheduleSendEmail();
        System.out.println(EmailSenderBean.encodeForUrl("Sở thông tin truyền thông hà nội"));
        System.out.println("End!!!!");
    }


    private final static Logger LOGGER = Logger.getLogger(EmailSenderBean.class);
}
