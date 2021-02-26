/**
 *
 */
package com.bkav.edoc.service.mineutil;

import java.util.Random;

import com.bkav.edoc.service.resource.StringPool;

public class RandomUtil {

    /**
     * @return
     */
    static public String randomId() {
        char[] chars = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

        StringBuilder sb = new StringBuilder(StringPool.BLANK);

        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(randomId());
    }
}

