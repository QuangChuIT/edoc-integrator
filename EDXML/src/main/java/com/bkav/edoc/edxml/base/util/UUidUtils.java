package com.bkav.edoc.edxml.base.util;

import java.util.UUID;

public class UUidUtils {
    public UUidUtils() {
    }

    public static String generate() {
        UUID localUUID = UUID.randomUUID();
        return localUUID.toString();
    }
}
