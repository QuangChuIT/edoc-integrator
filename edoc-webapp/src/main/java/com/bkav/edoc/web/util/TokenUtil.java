package com.bkav.edoc.web.util;

import org.apache.axiom.om.util.Base64;
import org.apache.log4j.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class TokenUtil {

    public static final Logger log = Logger.getLogger(TokenUtil.class);
    private static final String ALGORITHM = "HmacSHA1";

    private TokenUtil() {

    }

    /**
     * Generates a random number using two UUIDs and HMAC-SHA1
     *
     * @return generated secure random number
     */
    public static String getRandomNumber() {
        try {
            String secretKey = UUID.randomUUID().toString();
            String baseString = UUID.randomUUID().toString();

            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(key);
            byte[] rawHmac = mac.doFinal(baseString.getBytes(StandardCharsets.UTF_8));
            // Registry doesn't have support for these character.
            /*random = random.replace("/", "_");
            random = random.replace("=", "a");
            random = random.replace("+", "f");*/
            return Base64.encode(rawHmac);
        } catch (Exception e) {
            log.error("Error when gen key for organization register");
        }
        return null;
    }

    /**
     * Generates a random number using two UUIDs and HMAC-SHA1
     *
     * @return generated secure random number
     */
    public static String getRandomNumber(String organDomain, String organName) {
        try {
            String secretKey = UUID.randomUUID().toString();
            String baseString = organDomain + "/" + organName;

            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(key);
            byte[] rawHmac = mac.doFinal(baseString.getBytes(StandardCharsets.UTF_8));
            // Registry doesn't have support for these character.
            /*random = random.replace("/", "_");
            random = random.replace("=", "a");
            random = random.replace("+", "f");*/
            return Base64.encode(rawHmac);
        } catch (Exception e) {
            log.error("Error when gen key for organization register");
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getRandomNumber());
    }
}
