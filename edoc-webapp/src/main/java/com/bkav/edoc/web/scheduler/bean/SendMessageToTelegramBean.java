package com.bkav.edoc.web.scheduler.bean;

import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.entity.EmailRequest;
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
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component("sendTelegramMessageBean")
public class SendMessageToTelegramBean {

    @Autowired
    private MessageSourceUtil messageSourceUtil;

    public void runScheduleSendMessageToTelegram() {
        try {
            Date today = new Date();
            String warningMessage;
            int i = 1;

            List<EmailRequest> messageObject = EdocNotificationServiceUtil.telegramScheduleSend(today);
            if (messageObject.size() == 0) {
                LOGGER.info("ALL OF ORGANIZATION TAKEN DOCUMENT!!!!!!!");
            } else {
                warningMessage = messageSourceUtil.getMessage("edoc.title.telegram",
                        new Object[]{DateUtils.format(today, DateUtils.VN_DATE_FORMAT), messageObject.size()});
                sendTelegramMessage(warningMessage);
                //System.out.println(warningMessage);

                for (EmailRequest request : messageObject) {
                    LOGGER.info("Starting count with organ domain " + request.getReceiverId());
                    EdocDynamicContact receiverContact = EdocDynamicContactServiceUtil.findContactByDomain(request.getReceiverId());
                    String detailMessageOrgan = messageSourceUtil.getMessage("edoc.title.telegram.header",
                            new Object[]{i, receiverContact.getName(), request.getNumberOfDocument()});
                    List<EdocDocument> documents = request.getEdocDocument();
                    LOGGER.info("Organ with domain " + request.getReceiverId() + " has " + documents.size() + " not taken documents");
                    Map<String, String> map = new HashMap<>();
                    for (EdocDocument document : documents) {
                        String doc_code = document.getDocCode();
                        EdocDynamicContact senderOrgan = EdocDynamicContactServiceUtil.findContactByDomain(document.getFromOrganDomain());
                        String sender = senderOrgan.getName();
                        String msg = "";
                        if (map.containsKey(senderOrgan.getDomain())) {
                            msg = map.get(senderOrgan.getDomain()) + ", " + doc_code;
                        } else {
                            msg = messageSourceUtil.getMessage("edoc.telegram.detail.msg", new Object[]{sender, doc_code});
                        }
                        map.put(senderOrgan.getDomain(), msg);
                    }
                    String str = "";
                    String sub_str = "";
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        if (str.length() < 3000) {
                            str += entry.getValue();
                        } else {
                            String new_str = entry.getValue();
                            sub_str += new_str;
                        }
                    }
                    if (sub_str != "") {
                        TimeUnit.SECONDS.sleep(2);
                        sendTelegramMessage(detailMessageOrgan + str);
                        sendTelegramMessage(sub_str);
                        //System.out.println(detailMessageOrgan + str);
                        //System.out.println(sub_str);
                    } else {
                        TimeUnit.SECONDS.sleep(2);
                        sendTelegramMessage(detailMessageOrgan + str);
                        //System.out.println(detailMessageOrgan + str);
                    }
                    LOGGER.info("End count !!!!!!!!!!");
                    i++;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Not send message to telegram cause " + e);
        }
    }

    private void sendTelegramMessage(String inputString) throws IOException {
        String urlString = "https://api.telegram.org/bot%s/sendMessage";

        String result = "";
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
            nameValuePairs.add(new BasicNameValuePair("chat_id",  "-" + chatId));
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

    public static void main(String[] args) throws IOException {
        SendMessageToTelegramBean test = new SendMessageToTelegramBean();
        test.sendTelegramMessage("Bản Final");
        //test.runScheduleSendMessageToTelegram();
    }

    private final static Logger LOGGER = Logger.getLogger(SendMessageToTelegramBean.class);
}
