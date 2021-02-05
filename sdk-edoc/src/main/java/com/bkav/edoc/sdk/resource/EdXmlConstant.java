/**
 * FirstName LastName - Feb 5, 2015
 */
package com.bkav.edoc.sdk.resource;

import org.jdom2.Namespace;

import java.io.File;

public class EdXmlConstant {

    public static final String EDXML_PREFIX = "edXML";
    public static final Namespace EDXML_NS = Namespace.getNamespace("http://www.e-doc.vn/Schema/");
    public static final Namespace XLINK_NS = Namespace.getNamespace("http://www.w3.org/1999/xlink");
    public static final String EDXML_URI = "http://www.mic.gov.vn/TBT/QCVN_102_2016";
    public static final String SOAP_URI = "http://schemas.xmlsoap.org/soap/envelope/";

    public static final String BODY_TAG = "Body";
    public static final String MANIFEST_TAG = "Manifest";
    public static final String REFERENCE_TAG = "Reference";
    public static final String HREF_ATTR = "href";
    public static final String ATTACHMENT_NAME_TAG = "AttachmentName";
    public static final String GET_PENDING_DOCUMENT = "GetPendingDocumentIdsRequest";
    public static final String GET_DOCUMENT = "GetDocumentRequest";
    public static final String GET_TRACE = "GetTraces";
    public static final String CONFIRM_RECEIVED_REQUEST = "ConfirmReceived";

    //soap action
    public static final String GET_PENDING_DOCUMENT_ACTION = "GetPendingDocumentIds";
    public static final String GET_DOCUMENT_ACTION = "GetDocument";
    public static final String GET_TRACE_ACTION = "GetTraces";
    public static final String SEND_DOCUMENT_ACTION = "SendDocument";
    public static final String UPDATE_TRACE_ACTION = "UpdateTraces";
    public static final String CHECK_PERMISSION = "CheckPermission";
    public static final String GET_CONTACT = "GetOrganizations";

    // action vpcp
    public static final String SEND_DOC_VPCP = "SendDocResult";
    public static final String GET_RECEIVED_VPCP = "GetReceivedEdocResult";
    public static final String DELETE_AGENCY_VPCP = "DeleteAgencyResult";
    public static final String REGISTER_AGENCY_VPCP = "RegisterAgencyResult";
    public static final String GET_DOC_VPCP = "GetEdocResult";
    public static final String GET_DOC_STATUS = "GetSendEdocResult";
    public static final String UPDATE_DOC_STATUS = "GetChangeStatusResult";

    public static final String INBOX_MODE = "inbox";
    public static final String INBOX_RECEIVED = "inboxReceived";
    public static final String INBOX_NOT_RECEIVED = "inboxNotReceived";
    public static final String OUTBOX = "outbox";
    public static final String DRAFT = "draft";

    public final static String SEPARATOR = File.separator;
}
