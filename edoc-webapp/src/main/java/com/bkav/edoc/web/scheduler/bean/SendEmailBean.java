package com.bkav.edoc.web.scheduler.bean;

import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.entity.EmailRequest;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.bkav.edoc.service.database.util.EdocNotificationServiceUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.*;

@Component()
public class SendEmailBean {
//    private EmailSender emailSender;
//    private Date _counterDate;
//
//
//    public void runScheduleSendEmail() {
//        try {
////            Calendar yesterday = Calendar.getInstance();
////            //yesterday.add(Calendar.DATE, -1);
////            //yesterday.add(Calendar.HOUR, 7);
////            _counterDate = yesterday.getTime();
////            LOGGER.info("Email prepare send at " + _counterDate);
//            List<EmailRequest> emailSendObject = EdocNotificationServiceUtil.emailScheduleSend();
//            int test = 0;
//            for (EmailRequest emailObject: emailSendObject) {
//                LOGGER.info("Start send email to organ with domain " + emailObject.getReceiverId());
//                EdocDynamicContact contact = EdocDynamicContactServiceUtil.findContactByDomain(emailObject.getReceiverId());
//                String receiverEmail = contact.getEmail();
//                emailObject.setReceiverName(contact.getName());
//                emailSender.sendEmailUsingVelocityTemplate("Thống kê văn bản chưa được nhận về", null, "noreply@bmail.com", "bxc61616@cuoly.com", emailObject);
//                LOGGER.info("Has " + emailObject.getNumberOfDocument() + " documents not taken");
//                LOGGER.info("Send email to organ with id " + emailObject.getReceiverId() + " successfully!!!");
//                test++;
//                if (test == 2)
//                    break;
//            }
//        } catch (Exception e) {
//            LOGGER.error("Error to send email because "  + e);
//        }
//    }

    private final static Logger LOGGER = Logger.getLogger(SendEmailBean.class);
}
