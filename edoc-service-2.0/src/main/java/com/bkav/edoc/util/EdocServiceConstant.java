package com.bkav.edoc.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EdocServiceConstant {
    public final static String ORGAN_ID = "organid";
    public final static String TOKEN = "authorization";
    public final static String DOC_ID = "docid";
    public final static String STATUS = "status";
    public final static String MESSAGE_TYPE = "type";
    public final static String SEND_DOCUMENT = "SendDocument_";
    public final static String SEND_STATUS = "SendStatus_";
    public final static List<String> MESSAGE_TYPES = new ArrayList<>(Arrays.asList("EDOC", "STATUS"));
}
