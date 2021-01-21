package com.bkav.edoc.web.scheduler.bean;

import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.entity.EmailRequest;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.bkav.edoc.service.database.util.EdocNotificationServiceUtil;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.web.util.MessageSourceUtil;
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
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
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
                System.out.println(warningMessage);

                for (EmailRequest request : messageObject) {
                    LOGGER.info("Starting count with organ domain " + request.getReceiverId());
                    EdocDynamicContact receiverContact = EdocDynamicContactServiceUtil.findContactByDomain(request.getReceiverId());
                    String detailMessageOrgan = messageSourceUtil.getMessage("edoc.title.telegram.header",
                            new Object[]{i, receiverContact.getName(), request.getNumberOfDocument()});
                    List<EdocDocument> documents = request.getEdocDocument();
                    Map<String, String> map = new HashMap<>();
                    for (EdocDocument document : documents) {
                        String doc_code = document.getDocCode();
                        EdocDynamicContact senderOrgan = EdocDynamicContactServiceUtil.findContactByDomain(document.getFromOrganDomain());
                        String sender = senderOrgan.getName();
                        String msg = "";
                        if (map.containsKey(senderOrgan.getDomain())) {
                            msg = map.get(senderOrgan.getDomain()) + "," + doc_code;
                        } else {
                            msg = messageSourceUtil.getMessage("edoc.telegram.detail.msg", new Object[]{sender, doc_code});
                        }
                        map.put(senderOrgan.getDomain(), msg);
                    }
                    TimeUnit.SECONDS.sleep(1);
                    sendTelegramMessage(detailMessageOrgan);
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        sendTelegramMessage(entry.getValue());
                    }
                   /* if (documents.size() <= 30) {
                        for (EdocDocument document : documents) {
                            String doc_code = document.getDocCode();
                            EdocDynamicContact senderOrgan = EdocDynamicContactServiceUtil.findContactByDomain(document.getFromOrganDomain());
                            String sender = senderOrgan.getName();
                            detailMessageOrgan += "\t\t➢\t" + doc_code + " được gửi bởi " + sender + "\n";
                        }
                        TimeUnit.SECONDS.sleep(1);
                        sendTelegramMessage(detailMessageOrgan);
                        System.out.println(detailMessageOrgan);
                    } else {
                        int count = 1;
                        String detailMessage = "";
                        for (EdocDocument document : documents) {
                            String doc_code = document.getDocCode();
                            EdocDynamicContact senderOrgan = EdocDynamicContactServiceUtil.findContactByDomain(document.getFromOrganDomain());
                            String sender = senderOrgan.getName();
                            detailMessage += "\t\t➢\t" + doc_code + " được gửi bởi " + sender + "\n";
                            if (count == 30) {
                                TimeUnit.SECONDS.sleep(1);
                                sendTelegramMessage(detailMessageOrgan + detailMessage);
                                System.out.println(detailMessageOrgan + detailMessage);
                                detailMessage = "";
                            }
                            count++;
                            if (count % 30 == 0 && count != 30) {
                                TimeUnit.SECONDS.sleep(1);
                                sendTelegramMessage(detailMessage);
                                System.out.println(detailMessage);
                                detailMessage = "";
                            }
                        }
                        TimeUnit.SECONDS.sleep(1);
                        sendTelegramMessage(detailMessage);
                        System.out.println(detailMessage);
                    }*/
                    LOGGER.info("End count !!!!!!!!!!");
                    i++;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Not send message to telegram cause " + e);
        }
    }

    private void sendTelegramMessage(String inputString) throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        String urlString = "https://api.telegram.org/bot1526166070:AAFH9CKxIgrmxAwRVm4rvxZJCwWg2lOWG7c/sendMessage";

        String result = "";
        String chatId = "1087968824";
        CloseableHttpClient httpclient = HttpClients.custom().build();

        try {
            HttpPost httpPost = new HttpPost(urlString);
            httpPost.addHeader("Content-type", "application/x-www-form-urlencoded");
            httpPost.addHeader("charset", "UTF-8");
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
        test.runScheduleSendMessageToTelegram();
    }

    private final static Logger LOGGER = Logger.getLogger(SendMessageToTelegramBean.class);
}
