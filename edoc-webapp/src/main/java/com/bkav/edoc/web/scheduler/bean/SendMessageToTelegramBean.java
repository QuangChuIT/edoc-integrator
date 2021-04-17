package com.bkav.edoc.web.scheduler.bean;

import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.entity.TelegramMessage;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.bkav.edoc.service.database.util.EdocNotificationServiceUtil;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.web.util.MessageSourceUtil;
import com.bkav.edoc.web.util.PropsUtil;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component("sendTelegramMessageBean")
public class SendMessageToTelegramBean {

    @Autowired
    private MessageSourceUtil messageSourceUtil;

    public void runScheduleSendMessageToTelegram() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -15);
            Date yesterday = cal.getTime();
            Date today = new Date();
            String warningMessage = "";
            int i = 1;

            LOGGER.info("--------------------- Start scheduler notification send document not taken ------------------------");
            List<TelegramMessage> messageObject = EdocNotificationServiceUtil.telegramScheduleSend();
            if (messageObject.size() == 0) {
                LOGGER.info("ALL OF ORGANIZATION TAKEN DOCUMENT!!!!!!!");
                warningMessage += messageSourceUtil.getMessage("edoc.title.all.taken", new Object[]{SIMPLE_DATE_FORMAT.format(today)});
                sendTelegramMessage(warningMessage);
                //System.out.println(warningMessage);
            } else {
                warningMessage += messageSourceUtil.getMessage("edoc.title.telegram",
                        new Object[]{DateUtils.format(today, DateUtils.VN_DATE_FORMAT), messageObject.size()});
                sendTelegramMessage(warningMessage);
                //System.out.println(warningMessage);

                String detailMessageOrgan = "";
                for (TelegramMessage telegramMessage : messageObject) {
                    /*EdocDynamicContact receiverContact = EdocDynamicContactServiceUtil.findContactByDomain(telegramMessage.getReceiverId());
                    if (receiverContact.getReceiveNotify()) {

                     */
                        detailMessageOrgan += messageSourceUtil.getMessage("edoc.title.telegram.header",
                                new Object[]{i, telegramMessage.getReceiverName()});
                        EdocDocument document = telegramMessage.getDocument();
                        LOGGER.info("Organ with domain " + telegramMessage.getReceiverId() + " not taken document with code " + document.getDocCode());

                        String doc_code = document.getDocCode();
                        EdocDynamicContact senderOrgan = EdocDynamicContactServiceUtil.findContactByDomain(document.getFromOrganDomain());
                        String sender = senderOrgan.getName();
                        String value = document.getDocumentId() + "," + doc_code + "(" + SIMPLE_DATE_FORMAT.format(telegramMessage.getCreateDate()) + ")";
                        String msg = messageSourceUtil.getMessage("edoc.telegram.detail.msg", new Object[]{sender, value});
                        detailMessageOrgan += msg;
                        if (detailMessageOrgan.length() > 3500) {
                            sendTelegramMessage(detailMessageOrgan);
                            //System.out.println(detailMessageOrgan);
                            detailMessageOrgan = "";
                        }
                        TimeUnit.SECONDS.sleep(2);
                        i++;
                    //}
                }
                sendTelegramMessage(detailMessageOrgan);
                //System.out.println(detailMessageOrgan);
            }
            /*warningMessage = "";

            // Notification to Telegram for docuemtn not send to VPCP
            LOGGER.info("--------------------- Start scheduler notification document not send to VPCP ------------------------");
            List<TelegramMessage> messageListNotSendVPCP = EdocNotificationServiceUtil.telegramScheduleDocumentNotSendVPCP();
            if (messageObject.size() == 0) {
                LOGGER.info("ALL OF DOCUMENT SEND TO VPCP SUCCESS !!!!!!!");
                warningMessage += messageSourceUtil.getMessage("edoc.title.all.send.vpcp", new Object[]{SIMPLE_DATE_FORMAT.format(today)});
                sendTelegramMessage(warningMessage);
                //System.out.println(warningMessage);
            } else {
                warningMessage += messageSourceUtil.getMessage("edoc.title.telegram.vpcp",
                        new Object[]{DateUtils.format(today, DateUtils.VN_DATE_FORMAT), messageObject.size()});
                sendTelegramMessage(warningMessage);
                //System.out.println(warningMessage);

                String detailMessageOrgan = "";
                for (TelegramMessage telegramMessage : messageObject) {
                    detailMessageOrgan += messageSourceUtil.getMessage("edoc.title.telegram.header",
                            new Object[]{i, telegramMessage.getReceiverName()});
                    EdocDocument document = telegramMessage.getDocument();
                    LOGGER.info("Organ with domain " + telegramMessage.getReceiverId() + " not send document to VPCP with code " + document.getDocCode());

                    String doc_code = document.getDocCode();
                    EdocDynamicContact senderOrgan = EdocDynamicContactServiceUtil.findContactByDomain(document.getFromOrganDomain());
                    String sender = senderOrgan.getName();
                    String value = document.getDocumentId() + "," + doc_code + "(" + SIMPLE_DATE_FORMAT.format(telegramMessage.getCreateDate()) + ")";
                    String msg = messageSourceUtil.getMessage("edoc.telegram.detail.msg", new Object[]{sender, value});
                    detailMessageOrgan += msg;
                    if (detailMessageOrgan.length() > 3500) {
                        sendTelegramMessage(detailMessageOrgan);
                        //System.out.println(detailMessageOrgan);
                        detailMessageOrgan = "";
                    }
                    TimeUnit.SECONDS.sleep(2);
                    i++;
                    //}
                }
                sendTelegramMessage(detailMessageOrgan);
                //System.out.println(detailMessageOrgan);
            }*/
            LOGGER.info("--------------------------------------- Done schedule send to telegram -----------------------------------------");
        } catch (Exception e) {
            LOGGER.error("Not send message to telegram cause " + e);
        }
    }

    private void sendTelegramMessage(String inputString) throws IOException {
        String urlString = "https://api.telegram.org/bot%s/sendMessage";

        String result = "";
        String host = PropsUtil.get("telegram.message.host");
        String chatId = PropsUtil.get("telegram.message.chatid");
        String token = PropsUtil.get("telegram.message.token");

        urlString = String.format(urlString, token);

        CloseableHttpClient httpclient = HttpClients.custom().build();

        try {
            HttpPost httpPost = new HttpPost(urlString);
            httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
            httpPost.addHeader("Accept-Charset", "UTF-8");

            /// Create list of parameters
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            /// Add chatid to the list
            nameValuePairs.add(new BasicNameValuePair("chat_id", "-" + chatId));
            /// Add text to the list
            nameValuePairs.add(new BasicNameValuePair("text", inputString));
            /// Set list of parameters as entity of the Http POST method
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity httpEntity = response.getEntity();
                if (httpEntity != null) {
                    result = EntityUtils.toString(httpEntity);
                    LOGGER.info(result);
                }
            } catch (Exception e) {
                LOGGER.error(Arrays.toString(e.getStackTrace()));
            } finally {
                response.close();
            }
        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
            httpclient.close();
        }
    }

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private final static Logger LOGGER = Logger.getLogger(SendMessageToTelegramBean.class);
}
