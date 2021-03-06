package com.bkav.edoc.converter.util;

public class StringQuery {
    public static final String GET_DOCUMENT = "select d.documentId,d.edXMLDocId,d.createDate,d.modifiedDate,d.subject,d.codeNumber," +
            "d.codeNotation,dd.promulgationPlace,d.promulgationDate,d.type_,d.typeName,d.priorityId," +
            "d.isDraft,d.sentDate,d.toOrganDomain,d.fromOrganDomain,d.visible from edoc_document as d, " +
            "edoc_documentdetail as dd where d.documentId =dd.docId and subject not like '%test%' and year(d.sentDate) = 2020 and d.documentId > 3802057";
    public static final String GET_DOCUMENT_DETAIL_BY_DOC_ID = "select * from edoc_documentdetail where docId=?";
    public static final String GET_NOTIFICATION_BY_DOC_ID = "Select notificationId, receiverId, docId, sendNumber, createDate, modifiedDate, dueDate, webEnable, adapterEnable  from edoc_notification where docId=?";
    public static final String GET_ATTACHMENT_BY_DOC_ID = "Select attachmentId ,organDomain,name,createDate,fullPath,type_,size_,toOrganDomain,docId  from edoc_attachment where docId=?";
    public static final String GET_TRACE_HEADER_LIST_BY_DOC_ID = "select bussinessDocType,bussinessDocReason,paper from edoc_traceheaderlist where docRefId=?";
    public static final String GET_TRACE_HEADER_BY_DOC_ID = "select organDomain,timeStamp from edoc_traceheaderlist where docRefId = ?";
    public static final String GET_USER_ORGANIZATION_BY_USERID = "select organizationId from users_orgs where userId = ?";
    public static final String GET_DYNAMIC_CONTACT_BY_ORG_ID = "select * from edoc_dynamiccontact where organizationId= ?";
    public static final String COUNT_DOCUMENTS = "Select count(*) from edoc_document";
    public static final String GET_DYNAMIC_CONTACT = "Select Id, Name, InCharge, Domain, Email, Address, Telephone, Fax, Website, Type, Version from edoc_dynamiccontact";
    public static final String GET_DOMAIN = "Select domain from edoc_dynamiccontact where agency = 1";
    public static final String GET_USER = "Select userId, screenName, emailAddress, CONCAT(firstName,' ', middleName,' ', lastName), password_, status, createDate, modifiedDate, lastLoginDate, lastLoginIP from user_";

    public static final String GET_DATE_COUNTER = "Select distinct Date(sentDate) from edoc_document where year(sentDate)= 2020";

    public static final String GET_DATE = "SELECT distinct Date(create_date) FROM edoc_document where Date(create_date) > \"2020-12-31\" and Date(create_date) < \"2021-03-25\"";

    /*public static final String GET_DATE_COUNTER = "Select distinct Date(sent_date) from edoc_document where year(sent_date) = 2020";*/

    //public static final String GET_DOCUMENT_BY_COUNTER_DATE = "Select document_id, from_organ_domain, to_organ_domain, sent_date, doc_code from edoc_document where Date(sent_date) = ?";
    public static final String GET_DOCUMENT_BY_COUNTER_DATE = "Select document_id, from_organ_domain, to_organ_domain, sent_date from edoc_document where Date(sent_date) = ? and doc_code = ?";

    public static final String CHECK_SIGNED_ATTACHMENT = "Select count(*) from edoc_attachment where document_id = ? and name like \"%_Signed%\"";

    public static final String COUNT_SENT_EXT_DOCUMENT = "SELECT * FROM edoc_document where date(create_date) > ? and to_organ_domain = ? and received_ext = ?";

    public static final String GET_DOC_CODE_BY_DOMAIN = "SELECT document_id FROM edoc_document where date(create_date) > ? and to_organ_domain = ? group by document_id";

    public static final String GET_DOC_CODE_BY_COUNTER_DATE = "Select doc_code from edoc_document where date(create_date) = ? group by doc_code";

    //public static final String GET_DOCID_BY_DOC_CODE = "SELECT document_id FROM edoc_document where date(create_date) = ? and doc_code = ?";

    public static final String GET_DOCUMENT_BY_DATE = "Select * From edoc_document where date(create_date) > \"2021-01-25\"";

    /*public static final String GET_DOCUMENT_BY_COUNTER_DATE = "Select from_organ_domain, to_organ_domain, sent_date from " +
            "edoc_document where Date(sent_date) = ? group by doc_code";*/
}
