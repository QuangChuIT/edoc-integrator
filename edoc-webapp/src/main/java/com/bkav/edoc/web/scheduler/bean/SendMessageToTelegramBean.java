package com.bkav.edoc.web.scheduler.bean;

import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.entity.EmailRequest;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.bkav.edoc.service.database.util.EdocNotificationServiceUtil;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.web.util.MessageSourceUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component("sendTelegramMessage")
public class SendMessageToTelegramBean {
    @Autowired
    private MessageSourceUtil messageSourceUtil;

    public void runScheduleSendMessageToTelegram() {
        try {
            Date today = new Date();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            Date yesterday = cal.getTime();
            String warningMessage = null;
            int i = 1;

            List<EmailRequest> messageObject = EdocNotificationServiceUtil.emailScheduleSend(yesterday, today);
            if (messageObject.size() == 0) {
                LOGGER.info("ALL OF ORGANIZATION TAKEN DOCUMENT!!!!!!!");
            } else {
                warningMessage = "Ngày " + DateUtils.format(today, DateUtils.VN_DATE_FORMAT) + " có " + messageObject.size() + " đơn vị chưa nhận văn bản về như sau:\n";
                sendTelegramMessage(warningMessage);
                System.out.println(warningMessage);
                for (EmailRequest request: messageObject) {
                    LOGGER.info("Starting count with organ domain " + request.getReceiverId());
                    EdocDynamicContact receiverContact = EdocDynamicContactServiceUtil.findContactByDomain(request.getReceiverId());
                    String detailMessageOrgan = new String(i + ". " + receiverContact.getName() + " chưa nhận về " + request.getNumberOfDocument() + " văn bản có số ký hiệu như sau:\n");
                    List<EdocDocument> documents = request.getEdocDocument();
                    if (documents.size() <= 30) {
                        for (EdocDocument document: documents) {
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
                        for (EdocDocument document: documents) {
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
                    }
                    LOGGER.info("End count !!!!!!!!!!");
                    i++;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Not send message to telegram cause " + e);
        }
    }

    private void sendTelegramMessage(String inputString) throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        String urlString = "https://api.telegram.org/bot%s/sendmessage";

        String result = "";
        String apiToken = "1526166070:AAFH9CKxIgrmxAwRVm4rvxZJCwWg2lOWG7c";
        String chatId = "495981246";

        urlString = String.format(urlString, apiToken);

        String json = createJson(chatId, inputString);

        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                builder.build());
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(
                sslConnectionSocketFactory).build();

        try {
            HttpPost httpPost = new HttpPost(urlString);
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-Type", "application/json");
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[]{ new DefaultTrustManager()}, new SecureRandom());
            SSLContext.setDefault(ctx);
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

    private static class DefaultTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    private String createJson(String chat_id, String text) {
        JSONObject json = new JSONObject();
        json.put("chat_id", chat_id);
        json.put("text", text);
        return json.toString();
    }

    public static void main(String[] args) throws IOException {
        SendMessageToTelegramBean test = new SendMessageToTelegramBean();
        test.runScheduleSendMessageToTelegram();
    }
    private final static Logger LOGGER = Logger.getLogger(SendMessageToTelegramBean.class);
}
