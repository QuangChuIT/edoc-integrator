package com.bkav.edoc.service.resource;

public class QueryString {
    public static String BASE_QUERY_DOCUMENT_INBOX_TMP = "select * from edoc_document ed where (:organDomain is null or" +
            " ed.to_organ_domain like concat('%',:organDomain,'%')) and ed.is_draft=false and (:toOrgan is null or ed.to_organ_domain like concat('%',:toOrgan,'%'))" +
            " and (:fromOrgan is null or ed.from_organ_domain = :fromOrgan)" +
            " and (:docCode is null or ed.doc_code = :docCode) #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String BASE_QUERY_DOCUMENT_OUTBOX_TMP = "select * from edoc_document " +
            "ed where (:organDomain is null or ed.from_organ_domain = :organDomain) and ed.is_draft=false " +
            "and (:toOrgan is null or ed.to_organ_domain like concat('%',:toOrgan,'%')) " +
            "and (:fromOrgan is null or ed.from_organ_domain = :fromOrgan) " +
            "and (:docCode is null or ed.doc_code = :docCode) #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String BASE_QUERY_DOCUMENT_DRAFT_TMP = "select * from edoc_document ed " +
            "where (:organDomain is null or ed.from_organ_domain = :organDomain) and ed.is_draft=true " +
            "and (:toOrgan is null or ed.to_organ_domain like concat('%',:toOrgan,'%')) " +
            "and (:fromOrgan is null or ed.from_organ_domain = :fromOrgan) " +
            "and (:docCode is null or ed.doc_code = :docCode) #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String BASE_QUERY_DOCUMENT_INBOX_NOT_RECEIVER_TMP = "select * from edoc_document ed inner join edoc_notification n on ed.document_id = n.document_id " +
            "where n.taken = 0 and (:organDomain is null or n.receiver_id = :organDomain) and ed.is_draft=false " +
            "and (:toOrgan is null or ed.to_organ_domain like concat('%',:toOrgan,'%')) " +
            "and (:fromOrgan is null or ed.from_organ_domain = :fromOrgan) " +
            "and (:docCode is null or ed.doc_code = :docCode) #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String BASE_QUERY_DOCUMENT_INBOX_RECEIVER_TMP = "select * from edoc_document ed INNER JOIN edoc_notification n on ed.document_id = n.document_id" +
            " where n.taken = 1 and (:organDomain is null or n.receiver_id = :organDomain) and ed.is_draft=false" +
            " and (:toOrgan is null or ed.to_organ_domain like concat('%',:toOrgan,'%'))" +
            " and (:fromOrgan is null or ed.from_organ_domain = :fromOrgan)" +
            " and (:docCode is null or ed.doc_code = :docCode) #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String QUERY_COUNT_DOCUMENT_INBOX_TMP = "select count(1) from edoc_document ed " +
            "where (:organDomain is null or ed.to_organ_domain like concat('%',:organDomain,'%')) and ed.is_draft=false " +
            "and (:toOrgan is null or ed.to_organ_domain like concat('%',:toOrgan,'%')) " +
            "and (:fromOrgan is null or ed.from_organ_domain = :fromOrgan) " +
            "and (:docCode is null or ed.doc_code = :docCode) #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String QUERY_COUNT_DOCUMENT_OUTBOX_TMP = "select count(ed.document_id) from edoc_document ed " +
            "where (:organDomain is null or from_organ_domain = :organDomain) and is_draft=false " +
            "and (:toOrgan is null or ed.to_organ_domain like concat('%',:toOrgan,'%')) " +
            "and (:fromOrgan is null or ed.from_organ_domain = :fromOrgan) " +
            "and (:docCode is null or ed.doc_code = :docCode) #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String QUERY_COUNT_DOCUMENT_INBOX_NOT_RECEIVER_TMP = "select count(ed.document_id) from edoc_document ed inner join edoc_notification n " +
            "on ed.document_id = n.document_id where n.taken = 0 and (:organDomain is null or n.receiver_id = :organDomain) and ed.is_draft=false" +
            " and (:toOrgan is null or ed.to_organ_domain like concat('%',:toOrgan,'%'))" +
            " and (:fromOrgan is null or ed.from_organ_domain = :fromOrgan)" +
            " and (:docCode is null or ed.doc_code = :docCode) #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String QUERY_COUNT_DOCUMENT_INBOX_RECEIVER_TMP = "select count(ed.document_id) from edoc_document ed inner join edoc_notification n " +
            "on ed.document_id = n.document_id where n.taken = 1 and (:organDomain is null or n.receiver_id = :organDomain) and ed.is_draft=false" +
            " and (:toOrgan is null or ed.to_organ_domain like concat('%',:toOrgan,'%'))" +
            " and (:fromOrgan is null or ed.from_organ_domain = :fromOrgan)" +
            " and (:docCode is null or ed.doc_code = :docCode) #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String QUERY_COUNT_DOCUMENT_DRAFT_TMP = "select count(ed.document_id) from edoc_document ed " +
            "where (:organDomain is null or ed.from_organ_domain = :organDomain) and ed.is_draft=true" +
            " and (:toOrgan is null or ed.to_organ_domain like concat('%',:toOrgan,'%'))" +
            " and (:fromOrgan is null or ed.from_organ_domain = :fromOrgan)" +
            " and (:docCode is null or ed.doc_code = :docCode) #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String BASE_QUERY_DOCUMENT_NOT_TAKEN_TMP = "select en.* from edoc_notification as en, edoc_document as ed, edoc_dynamiccontact as co " +
            "where ed.document_id = en.document_id and ed.to_organ_domain like concat('%', en.receiver_id, '%') " +
            "and en.receiver_id = co.domain and co.receive_notify = 1 and en.taken = 0 #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String QUERY_COUNT_DOCUMENT_NOT_TAKEN_TMP = "select count(1) from edoc_document as ed, edoc_notification as en, edoc_dynamiccontact as co " +
            "where ed.document_id = en.document_id and ed.to_organ_domain like concat('%', en.receiver_id, '%') " +
            "and en.receiver_id = co.domain and co.receive_notify = 1 and en.taken = 0 #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String BASE_QUERY_DOCUMENT_NOT_SEND_VPCP = "select ed.* from edoc_document as ed where (ed.send_ext = 0 and document_ext_id = NULL " +
            "and ed.to_organ_domain like '%000.00.00.G%') or (ed.send_ext = 1 and ed.document_ext_id = '' and ed.to_organ_domain like '%000.00.00.G%') #WHERE_CLAUSE# #ORDER_CLASUE#";

    public static String QUERY_COUNT_DOCUMENT_NOT_SEND_VPCP = "select count(1) from edoc_document as ed where (ed.send_ext = 0 and document_ext_id = NULL " +
            "and ed.to_organ_domain like '%000.00.00.G%') or (ed.send_ext = 1 and ed.document_ext_id = '' and ed.to_organ_domain like '%000.00.00.G%') #WHERE_CLAUSE# #ORDER_CLASUE#";
}
