package com.bkav.edoc.sdk.edxml.util;

import java.util.UUID;

public class UUidUtils {

    public static String generate() {
        UUID localUUID = UUID.randomUUID();
        return localUUID.toString();
    }
}
