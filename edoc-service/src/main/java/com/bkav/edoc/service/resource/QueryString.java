package com.bkav.edoc.service.resource;

public class QueryString {
    public static String BASE_QUERY_DOCUMENT_INBOX_TMP = "select * from edoc_document ed where ed.to_organ_domain like" +
            " :organDomain and ed.is_draft=false #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String BASE_QUERY_DOCUMENT_OUTBOX_TMP = "select * from edoc_document " +
            "ed where ed.from_organ_domain = :organDomain and ed.is_draft=false #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String BASE_QUERY_DOCUMENT_DRAFT_TMP = "select * from edoc_document ed " +
            "where ed.from_organ_domain = :organDomain and ed.is_draft=true #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String BASE_QUERY_DOCUMENT_INBOX_NOT_RECEIVER_TMP = "select * from edoc_document ed inner join edoc_notification n on ed.document_id = ed.document_id " +
            "where n.taken = 0 and n.receiver_id = :organDomain and ed.is_draft=false #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String BASE_QUERY_DOCUMENT_INBOX_RECEIVER_TMP = "select * from edoc_document ed INNER JOIN edoc_notification n on ed.document_id = n.document_id" +
            " where n.taken = 1 and n.receiver_id = :organDomain and ed.is_draft=false #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String QUERY_COUNT_DOCUMENT_INBOX_TMP = "select count(1) from edoc_document ed " +
            "where ed.to_organ_domain like concat('%',:organDomain,'%') and ed.is_draft=false #WHERE_CLAUSE#";

    public static String QUERY_COUNT_DOCUMENT_OUTBOX_TMP = "select count(ed.document_id) from edoc_document ed " +
            "where from_organ_domain = :organDomain and is_draft=false #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String QUERY_COUNT_DOCUMENT_INBOX_NOT_RECEIVER_TMP = "select count(ed.document_id) from edoc_document ed inner join edoc_notification n " +
            "on ed.document_id = n.document_id " +
            "where n.taken = 0 and n.receiver_id = :organDomain and ed.is_draft=false #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String QUERY_COUNT_DOCUMENT_INBOX_RECEIVER_TMP = "select count(ed.document_id) from edoc_document ed inner join edoc_notification n " +
            "on ed.document_id = n.document_id where n.taken = 1 and n.receiver_id = :organDomain and ed.is_draft=false #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String QUERY_COUNT_DOCUMENT_DRAFT_TMP = "select count(ed.document_id) from edoc_document ed " +
            "where ed.from_organ_domain = :organDomain and ed.is_draft=true #WHERE_CLAUSE# #ORDER_CLASUE#";
}
