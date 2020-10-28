package com.bkav.edoc.service.resource;

public class QueryString {
    public static String BASE_QUERY_DOCUMENT_INBOX_TMP = "SELECT ed from EdocDocument ed where ed.toOrganDomain like :organDomain and ed.draft=false #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String BASE_QUERY_DOCUMENT_OUTBOX_TMP = "SELECT ed from EdocDocument ed where ed.fromOrganDomain = :organDomain and ed.draft=false #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String BASE_QUERY_DOCUMENT_DRAFT_TMP = "SELECT ed FROM EdocDocument ed where ed.fromOrganDomain = :organDomain and ed.draft=true #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String BASE_QUERY_DOCUMENT_INBOX_NOT_RECEIVER_TMP = "SELECT ed FROM EdocDocument ed INNER JOIN ed.notifications n WHERE n.taken = 0 and n.receiverId = :organDomain and ed.draft=false #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String BASE_QUERY_DOCUMENT_INBOX_RECEIVER_TMP = "SELECT ed FROM EdocDocument ed INNER JOIN ed.notifications n WHERE n.taken = 1 and n.receiverId = :organDomain and ed.draft=false #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String QUERY_COUNT_DOCUMENT_INBOX_TMP = "SELECT COUNT(*) FROM EdocDocument ed WHERE ed.toOrganDomain like concat('%',:organDomain,'%') and ed.draft=false #WHERE_CLAUSE#";

    public static String QUERY_COUNT_DOCUMENT_OUTBOX_TMP = "SELECT COUNT(ed.documentId) FROM EdocDocument ed WHERE ed.fromOrganDomain = :organDomain and ed.draft=false #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String QUERY_COUNT_DOCUMENT_INBOX_NOT_RECEIVER_TMP = "SELECT count(ed.documentId) FROM EdocDocument ed INNER JOIN ed.notifications n WHERE n.taken = 0 and n.receiverId = :organDomain and ed.draft=false #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String QUERY_COUNT_DOCUMENT_INBOX_RECEIVER_TMP = "SELECT count(ed.documentId) FROM EdocDocument ed INNER JOIN ed.notifications n WHERE n.taken = 1 and n.receiverId = :organDomain and ed.draft=false #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String QUERY_COUNT_DOCUMENT_DRAFT_TMP = "SELECT count(ed.documentId) FROM EdocDocument ed where ed.fromOrganDomain = :organDomain and ed.draft=true #WHERE_CLAUSE# #ORDER_CLASUE#";
}
