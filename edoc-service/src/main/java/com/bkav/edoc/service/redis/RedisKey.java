package com.bkav.edoc.service.redis;

public class RedisKey {

    public final static String CHECK_PERMISSION_KEY = "CheckPermission_";

    public final static String SEND_DOCUMENT_KEY = "SendDocument_";

    public final static String GET_DOCUMENT_KEY = "GetDocument_";

    public final static String GET_PENDING_KEY = "GetPendingDocument_";

    public final static String GET_TRACE_KEY = "GetTrace_";

    public final static String SEND_TO_VPCP = "SendVPCP_";

    public final static String GET_ENVELOP_FILE = "GetEnvelopFile_";

    public final static String CHECK_ALLOW_KEY = "CheckAllowDocument_";

    public final static String GET_ATTACHMENT_BY_DOC_ID = "GetAttachmentByDocID_";

    public final static String LIST_DOCUMENT = "ListDocument_";

    public final static String GET_LIST_DOCUMENT_KEY = "ListDocumentBy_";

    public final static String GET_LIST_CONTACT_KEY = "ListOrganization_";

    public final static String LIST_USER = "ListUser_";

    public final static String GET_LIST_CONTACT_BY_KEY = "ListOrganizationByDomain_";

    public static String getKey(String finalKey, String methodPrefixKey) {
        StringBuilder resultKey = new StringBuilder();
        resultKey.append(methodPrefixKey);
        resultKey.append(finalKey);

        return resultKey.toString();
    }
}
