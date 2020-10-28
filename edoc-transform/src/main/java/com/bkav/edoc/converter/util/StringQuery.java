package com.bkav.edoc.converter.util;

public class StringQuery {
    public static final String GET_DOCUMENT = "select d.documentId,d.edXMLDocId,d.createDate,d.modifiedDate,d.subject,d.codeNumber," +
            "d.codeNotation,dd.promulgationPlace,d.promulgationDate,d.type_,d.typeName,d.priorityId," +
            "d.isDraft,d.sentDate,d.toOrganDomain,d.fromOrganDomain,d.visible from edoc_document as d, " +
            "edoc_documentdetail as dd where d.documentId =dd.docId and subject not like '%test%'";
    public static final String GET_DOCUMENT_DETAIL_BY_DOC_ID = "select * from edoc_documentdetail where docId=?";
    public static final String GET_NOTIFICATION_BY_DOC_ID = "Select notificationId, receiverId, docId, sendNumber, createDate, modifiedDate, dueDate, webEnable, adapterEnable  from edoc_notification where docId=?";
    public static final String GET_ATTACHMENT_BY_DOC_ID = "Select attachmentId ,organDomain,name,createDate,fullPath,type_,size_,toOrganDomain,docId  from edoc_attachment where docId=?";
    public static final String GET_TRACE_HEADER_LIST_BY_DOC_ID = "select bussinessDocType,bussinessDocReason,paper from edoc_traceheaderlist where docRefId=?";
    public static final String GET_TRACE_HEADER_BY_DOC_ID = "select organDomain,timeStamp from edoc_traceheaderlist where docRefId = ?";
    public static final String GET_USER_ORGANIZATION_BY_USERID = "select organizationId from users_orgs where userId = ?";
    public static final String GET_DYNAMIC_CONTACT_BY_ORG_ID = "select * from edoc_dynamiccontact where organizationId= ?";
    public static final String COUNT_DOCUMENTS = "Select count(*) from edoc_document";
    public static final String GET_DYNAMIC_CONTACT = "Select Id, Name, InCharge, Domain, Email, Address, Telephone, Fax, Website, Type, Version from edoc_dynamiccontact";
    public static final String GET_USER = "Select userId,screenName, emailAddress, CONCAT(firstName,' ', middleName,' ', lastName), password_, status, createDate, modifiedDate, lastLoginDate, lastLoginIP from user_";
}
