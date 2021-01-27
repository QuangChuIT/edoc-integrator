package com.bkav.edoc.edxml.base.util;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class Encoders {
    private static final char[] HEX_DIGIT = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final String LINE_SEPARATORS = "\r\n";

    private Encoders() {
        throw new AssertionError("The Encoders utility must not be instantiated.");
    }

    public static byte[] utf8encode(String source) {
        if (source == null) {
            return null;
        } else {
            return source.length() == 0 ? new byte[0] : source.getBytes(Charsets.UTF_8);
        }
    }

    public static String utf8decode(byte[] source) {
        if (source == null) {
            return null;
        } else {
            return source.length == 0 ? "" : new String(source, Charsets.UTF_8);
        }
    }

    public static byte[] base64Encode(byte[] source) {
        return utf8encode(BaseEncoding.base64().encode(source));
    }

    public static byte[] base64Decode(byte[] source) {
        return BaseEncoding.base64().decode(utf8decode(source));
    }

    public static String base64EncodeText(byte[] source) {
        return BaseEncoding.base64().encode(source);
    }

    public static String base64UrlEncodeText(byte[] source) {
        return BaseEncoding.base64Url().encode(source);
    }

    public static byte[] base64DecodeText(String source) {
        return BaseEncoding.base64().decode(source);
    }

    public static byte[] base64UrlDecodeText(String source) {
        return BaseEncoding.base64Url().decode(source);
    }

    public static String bytesToHex(byte[] bytes) {
        return bytesToHex(bytes, true);
    }

    public static OutputStream createMimeEncodingStream(OutputStream output) {
        return BaseEncoding.base64().withSeparator("\r\n", 76).encodingStream(new OutputStreamWriter(output));
    }

    public static InputStream createMimeDecodingStream(InputStream input) {
        return BaseEncoding.base64().withSeparator("\r\n", 76).decodingStream(new InputStreamReader(input));
    }

    public static OutputStream createPemEncodingStream(OutputStream output) {
        return BaseEncoding.base64().withSeparator("\r\n", 64).encodingStream(new OutputStreamWriter(output));
    }

    public static InputStream createPemDecodingStream(InputStream input) {
        return BaseEncoding.base64().withSeparator("\r\n", 64).decodingStream(new InputStreamReader(input));
    }

    public static String bytesToHex(byte[] bytes, boolean lowercase) {
        char[] hexChars = new char[bytes.length * 2];
        int ms = lowercase ? 0 : 16;

        for (int index = 0; index < bytes.length; ++index) {
            int value = bytes[index] & 255;
            hexChars[index * 2] = HEX_DIGIT[value >>> 4 | ms];
            hexChars[index * 2 + 1] = HEX_DIGIT[value & 15 | ms];
        }

        return new String(hexChars);
    }

    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        if (len % 2 != 0) {
            throw new IllegalArgumentException("The hex value: [" + hex + "] is not valid.");
        } else {
            byte[] bytes = new byte[len / 2];

            for (int index = 0; index < len; index += 2) {
                bytes[index / 2] = (byte) (Character.digit(hex.charAt(index), 16) << 4 | Character.digit(hex.charAt(index + 1), 16));
            }

            return bytes;
        }
    }
}
