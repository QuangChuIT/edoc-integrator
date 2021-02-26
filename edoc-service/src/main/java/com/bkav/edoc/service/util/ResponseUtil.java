package com.bkav.edoc.service.util;

import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.mineutil.XmlUtil;
import com.bkav.edoc.service.resource.StringPool;
import com.bkav.edoc.service.xml.base.header.GetOrganizationsResponse;
import com.bkav.edoc.service.xml.base.header.GetPendingDocumentIDResponse;
import com.bkav.edoc.service.xml.base.header.GetTraceResponse;
import com.bkav.edoc.service.xml.base.header.Organization;
import com.bkav.edoc.service.xml.status.header.MessageStatus;
import org.apache.axiom.attachments.Attachments;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.util.XMLUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResponseUtil {

    /**
     * Takes a map containing key value pairs and maps those key-value pairs in to tag-value pairs within an
     * OMElement, and creates a new SOAP envelope using this OMElement.
     *
     * @param elements to be written to the envelope in Map format
     * @return SOAP Envelope to be added to the message context
     * @throws IOException If a failure on parsing JSON
     */
    public static SOAPEnvelope buildResultEnvelope(MessageContext messageContext,
                                                   final Map<String, Object> elements, String soapAction) {

        SOAPEnvelope responseEnvelope = null;
        OMFactory omFactory = OMAbstractFactory.getOMFactory();

        String soapNamespace = messageContext.getEnvelope().getNamespace()
                .getNamespaceURI();
        SOAPFactory soapFactory = null;

        OMNamespace omNamespace = omFactory.createOMNamespace(
                StringPool.TARGET_NAMESPACE, StringPool.EDXML_PREFIX);

        Object objectResult = elements.get(StringPool.ENVELOPE_SAVED_KEY);

        OMElement omElement = omFactory.createOMElement(soapAction
                + "Response", omNamespace);
        try {
            if (soapNamespace.equals(SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI)) {

                soapFactory = OMAbstractFactory.getSOAP11Factory();

            } else if (soapNamespace

                    .equals(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI)) {

                soapFactory = OMAbstractFactory.getSOAP12Factory();

            } else {

                LOGGER.error(
                        "Unknown soap message version");
            }

            if (soapFactory != null) {
                responseEnvelope = soapFactory.getDefaultEnvelope();
            }

            if (responseEnvelope != null) {

                // delete input message
                messageContext.flush();
                //check getDocument return document from cached or file
                if (objectResult instanceof Document) {
                    // SAAJUtil.
                    Document doc = (Document) objectResult;

                    responseEnvelope = XmlUtil.getFromDocument(doc);
                } else {

                    // Check and add body child from all action
                    objectResult = elements.get(StringPool.CHILD_BODY_KEY);

                    if (objectResult != null) {
                        if (objectResult instanceof Document) {

                            Document bodyChild = (Document) objectResult;

                            OMElement resultChild = XMLUtils.toOM(bodyChild
                                    .getDocumentElement());

                            if (soapAction.equals("GetDocument")) {

                                responseEnvelope.getBody()
                                        .addChild(resultChild);

                            } else {

                                omElement.addChild(resultChild);

                                // Check and add documentId to sendDocument
                                // response
                                if (soapAction.equals("SendDocument")) {

                                    objectResult = elements
                                            .get(StringPool.SEND_DOCUMENT_RESPONSE_ID_KEY);

                                    if (objectResult != null) {
                                        if (objectResult instanceof Document) {

                                            Document documentId = (Document) objectResult;

                                            OMElement documentIdOM = XMLUtils
                                                    .toOM(documentId
                                                            .getDocumentElement());

                                            omElement.addChild(documentIdOM);
                                        }
                                    }

                                }

                                responseEnvelope.getBody().addChild(omElement);
                            }
                        }
                    }

                    // Check and add header child for getDocument
                    objectResult = elements.get(StringPool.MESSAGE_HEADER_KEY);
                    if (objectResult != null) {

                        if (objectResult instanceof Document) {

                            Document headerChild = (Document) objectResult;

                            OMElement resultChild = XMLUtils.toOM(headerChild
                                    .getDocumentElement());

                            responseEnvelope.getHeader().addChild(resultChild);
                        }
                    }

                    // Check and add header child for getDocument
                    objectResult = elements.get(StringPool.TRACE_HEADER_KEY);

                    if (objectResult != null) {
                        if (objectResult instanceof Document) {

                            Document headerChild = (Document) objectResult;

                            OMElement resultChild = XMLUtils.toOM(headerChild
                                    .getDocumentElement());

                            responseEnvelope.getHeader().addChild(resultChild);
                        }
                    }
                }

                // Check and add attachment for getDocument
                objectResult = elements.get(StringPool.ATTACHMENT_KEY);

                if (objectResult != null) {

                    if (objectResult instanceof Attachments) {

                        Attachments attachments = (Attachments) objectResult;

                        messageContext.setAttachmentMap(attachments);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error build response envelop with soap action " + soapAction + e);
        }

        return responseEnvelope;
    }

    public GetPendingDocumentIDResponse createGetPendingDocumentIDResponse(
            List<Long> documentIds) {
        return new GetPendingDocumentIDResponse(documentIds);
    }

    public static GetOrganizationsResponse createGetOrganizationsResponse(List<EdocDynamicContact> contacts) {
        GetOrganizationsResponse response = new GetOrganizationsResponse();

        List<Organization> organizations = new ArrayList<>();

        for (EdocDynamicContact contact : contacts) {
            Organization organization = new Organization(contact.getDomain(), contact.getInCharge(), contact.getName(),
                    contact.getAddress(), contact.getEmail(), contact.getTelephone(), contact.getFax(), contact.getWebsite());
            organizations.add(organization);
        }

        response.setOrganization(organizations);

        return response;
    }

    public GetTraceResponse createGetTraceResponse(List<MessageStatus> statuses) {
        return new GetTraceResponse(statuses);
    }

    private final static Logger LOGGER = Logger.getLogger(ResponseUtil.class);
}
