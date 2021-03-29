package com.bkav.edoc.service.mineutil;

import com.bkav.edoc.service.commonutil.ErrorCommonUtil;
import com.bkav.edoc.service.database.entity.EdocTraceHeaderList;
import com.bkav.edoc.service.resource.EdXmlConstant;
import com.bkav.edoc.service.resource.StringPool;
import com.bkav.edoc.service.xml.base.body.Manifest;
import com.bkav.edoc.service.xml.base.body.Reference;
import com.bkav.edoc.service.xml.base.header.*;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.bkav.edoc.service.xml.ed.header.MessageHeader;
import org.apache.axiom.om.*;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.impl.builder.StAXSOAPModelBuilder;
import org.apache.axis2.util.XMLUtils;
import org.apache.log4j.Logger;
import org.jdom2.Attribute;
import org.jdom2.Namespace;
import org.jdom2.input.DOMBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class XmlUtil {

    private static final ExtractMime extractMine = new ExtractMime();
    private static final XpathUtil xpathUtil = new XpathUtil();

    public Document convertEntityToDocument(Class cls, Object obj) {
        JAXBContext jc;
        Document document;
        StringWriter writer = new StringWriter();
        Result result = new StreamResult(writer);
        try {
            jc = JAXBContext.newInstance(cls);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(obj, result);
            InputStream input = new ByteArrayInputStream(writer.toString()
                    .getBytes(StandardCharsets.UTF_8));

            document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(input);
        } catch (Exception e) {
            LOGGER.error(ErrorCommonUtil.getInfoToLog(e.getLocalizedMessage(),
                    getClass()));
            e.printStackTrace();
            return null;
        }
        return document;
    }

    public Document getDocumentFromFile(InputStream is)
            throws ParserConfigurationException, SAXException, IOException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        return dBuilder.parse(is);
    }

    public Document convertToDocument(SOAPEnvelope envelope) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        return getDocument(envelope, factory);
    }

    private Document getDocument(SOAPEnvelope envelope, DocumentBuilderFactory factory) {
        try {
            String str = envelope.toString();
            InputStream is = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            is.close();
            return doc;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static SOAPEnvelope getFromDocument(Document envelopeDoc)
            throws Exception {

        OMElement tempElement = XMLUtils.toOM(envelopeDoc.getDocumentElement());

        StAXSOAPModelBuilder stAXSOAPModelBuilder = new StAXSOAPModelBuilder(
                tempElement.getXMLStreamReader(), null);

        return stAXSOAPModelBuilder.getSOAPEnvelope();
    }

    public static org.jdom2.Document convertFromDom(Document document) {
        DOMBuilder builder = new DOMBuilder();
        return builder
                .build(document);
    }

    public Document getTraceHeaderDoc(TraceHeaderList traceHeaderList,
                                      OMNamespace ns) throws Exception {

        OMFactory factoryOM = OMAbstractFactory.getOMFactory();

        OMElement traceHeaderNode = factoryOM.createOMElement(

                StringPool.EDXML_TRACE_HEADER_BLOCK, ns);
        if (traceHeaderList != null) {
            List<OMNode> traceNodes = getTraceListChild(traceHeaderList, ns);

            // Them cac node con vao trong TraceHeaderList
            for (OMNode omNode : traceNodes) {

                traceHeaderNode.addChild(omNode);

            }
            traceNodes.clear();
        }
        return XMLUtils.toDOM(traceHeaderNode).getOwnerDocument();
    }

    public Document getSendResponseDocId(String docId) throws Exception {
        OMFactory factoryOM = OMAbstractFactory.getOMFactory();

        QName docIdQName = new QName(StringPool.TARGET_NAMESPACE,
                StringPool.SEND_DOCUMENT_RESPONSE_DOCID);

        OMElement docIdElement = factoryOM.createOMElement(docIdQName);

        docIdElement.setText(docId);

        return XMLUtils.toDOM(docIdElement).getOwnerDocument();
    }

    /**
     * @param manifest
     * @param ns
     * @return
     * @throws Exception
     */
    public Document getBodyChildDoc(Manifest manifest, OMNamespace ns)
            throws Exception {
        if (manifest == null)
            return null;
        OMFactory factoryOM = OMAbstractFactory.getOMFactory();

        OMElement manifestNode = factoryOM.createOMElement("Manifest", ns);
        List<Reference> refs = manifest.getReferences();
        OMNamespace attributeNS = factoryOM.createOMNamespace(
                StringPool.XLINK_NAMESPACE, "xlink");
        for (Reference item : refs) {
            OMElement reference = factoryOM.createOMElement("Reference", ns);

            OMElement attachmentName = factoryOM.createOMElement(
                    "AttachmentName", ns);
            attachmentName.setText(item.getAttachmentName());

            OMAttribute href = factoryOM.createOMAttribute("href", attributeNS,
                    "xlink");
            href.setAttributeValue(item.getContentId());

            OMAttribute type = factoryOM.createOMAttribute("type", attributeNS,
                    "xlink");
            type.setAttributeValue(item.getContentType());

            reference.addAttribute(href);
            reference.addAttribute(type);

            OMElement description = factoryOM
                    .createOMElement("Description", ns);
            description.setText(item.getDescription());

            reference.addChild(attachmentName);
            reference.addChild(description);

            manifestNode.addChild(reference);
        }
        return XMLUtils.toDOM(manifestNode).getOwnerDocument();
    }

    /**
     * @param traceHeaderList
     * @param ns
     * @return
     */
    private List<OMNode> getTraceListChild(TraceHeaderList traceHeaderList,
                                           OMNamespace ns) {

        List<OMNode> nodes = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        OMFactory factoryOM = OMAbstractFactory.getOMFactory();

        List<TraceHeader> traceHeaders = traceHeaderList.getTraceHeaders();

        for (TraceHeader item : traceHeaders) {

            // Tao <TraceHeader>
            OMElement traceHeader = factoryOM
                    .createOMElement("TraceHeader", ns);

            OMElement domain = null;

            domain = factoryOM.createOMElement("OrganId", ns);
            if (domain != null) {

                domain.setText(item.getOrganId());

                traceHeader.addChild(domain);
            }

            OMElement timestamp = factoryOM.createOMElement("Timestamp", ns);
            Date timeDate = item.getTimestamp();
            if (timeDate == null) {
                timeDate = new Date();
            }
            timestamp.setText(sdf.format(timeDate));

            traceHeader.addChild(timestamp);

            nodes.add(traceHeader);
        }

        // get business node
        OMElement businessNode = getBusinessChild(traceHeaderList, ns);
        nodes.add(businessNode);

        return nodes;
    }

    private OMElement getBusinessChild(TraceHeaderList traceHeaderList, OMNamespace ns) {
        OMFactory factoryOM = OMAbstractFactory.getOMFactory();

        Business business = traceHeaderList.getBusiness();

        OMElement businessNode = factoryOM.createOMElement("Business", ns);

        OMElement businessDocTypeNode = factoryOM.createOMElement("BusinessDocType", ns);
        businessDocTypeNode.setText(String.valueOf(business.getBusinessDocType()));
        businessNode.addChild(businessDocTypeNode);

        OMElement businessDocReasonNode = factoryOM.createOMElement("BusinessDocReason", ns);
        businessDocReasonNode.setText(business.getBusinessDocReason());
        businessNode.addChild(businessDocReasonNode);

        OMElement paperNode = factoryOM.createOMElement("Paper", ns);
        paperNode.setText(String.valueOf(business.getPaper()));
        businessNode.addChild(paperNode);

        // staff info
        OMElement staffInfoNode = getStaffInfoNode(business.getStaffInfo(), ns);
        businessNode.addChild(staffInfoNode);
        LOGGER.info("Business " + business.toString());
        // replacement info list
        if (business.getBusinessDocType() == EdocTraceHeaderList.BusinessDocType.REPLACE.ordinal()) {
            OMElement replacementInfoListNode = getReplacementInfoListNode(business.getReplacementInfoList(), ns);
            businessNode.addChild(replacementInfoListNode);
        } else if (business.getBusinessDocType() == EdocTraceHeaderList.BusinessDocType.UPDATE.ordinal()) {
            OMElement businessDocumentInfoNode = getBusinessDocumentInfoNode(business.getBusinessDocumentInfo(), ns);
            businessNode.addChild(businessDocumentInfoNode);
        }

        return businessNode;
    }

    private OMElement getReplacementInfoListNode(ReplacementInfoList replacementInfoList, OMNamespace ns) {
        OMFactory factoryOM = OMAbstractFactory.getOMFactory();

        OMElement replacementInfoListNode = factoryOM.createOMElement("ReplacementInfoList", ns);
        if (replacementInfoList != null) {
            if (replacementInfoList.getReplacementInfo() != null && replacementInfoList.getReplacementInfo().size() > 0) {
                for (ReplacementInfo replacementInfo : replacementInfoList.getReplacementInfo()) {
                    OMElement replacementInfoNode = factoryOM.createOMElement("ReplacementInfo", ns);

                    OMElement documentIdNode = factoryOM.createOMElement("DocumentId", ns);
                    documentIdNode.setText(replacementInfo.getDocumentId());
                    replacementInfoNode.addChild(documentIdNode);

                    OMElement organIdListNode = factoryOM.createOMElement("OrganIdList", ns);
                    for (String organId : replacementInfo.getOrganIdList().getOrganId()) {
                        OMElement organIdNode = factoryOM.createOMElement("OrganId", ns);
                        organIdNode.setText(organId);
                        organIdListNode.addChild(organIdNode);
                    }
                    replacementInfoNode.addChild(organIdListNode);

                    replacementInfoListNode.addChild(replacementInfoNode);
                }
            }
        }
        return replacementInfoListNode;
    }

    private OMElement getBusinessDocumentInfoNode(BusinessDocumentInfo businessDocumentInfo, OMNamespace ns) {
        OMFactory factoryOM = OMAbstractFactory.getOMFactory();

        OMElement businessDocumentInfoNode = factoryOM.createOMElement("BusinessDocumentInfo", ns);
        if (businessDocumentInfo != null) {
            OMElement documentInfoNode = factoryOM.createOMElement("DocumentInfo", ns);
            documentInfoNode.setText(businessDocumentInfo.getDocumentInfo() == null ? "" : businessDocumentInfo.getDocumentInfo());
            businessDocumentInfoNode.addChild(documentInfoNode);

            OMElement documentReceiverNode = factoryOM.createOMElement("DocumentReceiver", ns);
            documentReceiverNode.setText(businessDocumentInfo.getDocumentReceiver() == null ? "" : businessDocumentInfo.getDocumentReceiver());
            businessDocumentInfoNode.addChild(documentReceiverNode);

            OMElement receiverListNode = factoryOM.createOMElement("ReceiverList", ns);
            if (businessDocumentInfo.getReceiverList() != null) {
                if (businessDocumentInfo.getReceiverList().getReceiver() != null && businessDocumentInfo.getReceiverList().getReceiver().size() > 0) {
                    for (Receiver receiver : businessDocumentInfo.getReceiverList().getReceiver()) {
                        OMElement receiverNode = factoryOM.createOMElement("Receiver", ns);

                        OMElement receiverTypeNode = factoryOM.createOMElement("ReceiverType", ns);
                        receiverTypeNode.setText(String.valueOf(receiver.getReceiverType()));
                        receiverNode.addChild(receiverTypeNode);

                        OMElement organIdNode = factoryOM.createOMElement("OrganId", ns);
                        organIdNode.setText(String.valueOf(receiver.getOrganId()));
                        receiverNode.addChild(organIdNode);

                        receiverListNode.addChild(receiverNode);
                    }
                }
            }
            businessDocumentInfoNode.addChild(receiverListNode);
        }

        return businessDocumentInfoNode;
    }

    private OMElement getStaffInfoNode(StaffInfo staffInfo, OMNamespace ns) {
        OMFactory factoryOM = OMAbstractFactory.getOMFactory();
        OMElement staffInfoNode = factoryOM.createOMElement("StaffInfo", ns);

        OMElement departmentNode = factoryOM.createOMElement("Department", ns);
        departmentNode.setText(staffInfo.getDepartment());
        staffInfoNode.addChild(departmentNode);

        OMElement staffNode = factoryOM.createOMElement("Staff", ns);
        staffNode.setText(staffInfo.getStaff());
        staffInfoNode.addChild(staffNode);

        OMElement mobileNode = factoryOM.createOMElement("Mobile", ns);
        mobileNode.setText(staffInfo.getMobile());
        staffInfoNode.addChild(mobileNode);

        OMElement emailNode = factoryOM.createOMElement("Email", ns);
        emailNode.setText(staffInfo.getEmail());
        staffInfoNode.addChild(emailNode);

        return staffInfoNode;
    }

    public long getDocumentId(Document envelope) throws NumberFormatException,
            XMLStreamException {
        long documentId = 0L;
        String elementName = "documentId";
        String strData = getElementData(envelope, elementName);
        if (strData == null) {
            LOGGER.error("Error when get data from envelop when get document !!!");
            return documentId;
        } else {
            try {
                documentId = Long.parseLong(strData);
            } catch (Exception ex) {
                LOGGER.error("Error when parse documentId String to long cause: " + ex.getMessage());
                documentId = 0L;
            }
        }
        return documentId;
    }

    /**
     * @param envelope
     * @return
     * @throws NumberFormatException
     * @throws XMLStreamException
     */
    public String getOrganDomain(Document envelope)
            throws NumberFormatException, XMLStreamException {
        DOMSource source = new DOMSource(envelope);

        XMLStreamReader reader = XMLInputFactory.newInstance()
                .createXMLStreamReader(source);

        if (reader == null) {
            return StringPool.BLANK;
        }

        String organDomain = StringPool.BLANK;

        while (reader.hasNext()) {

            int type = reader.next();

            if (type == XMLStreamReader.START_ELEMENT) {

                if (reader.getLocalName().equalsIgnoreCase("organId")) {

                    // Chuyen den phan text cua element
                    reader.next();

                    String strId = reader.getText().trim();

                    if (!strId.equals("\n")
                            && !strId
                            .equals(StringPool.SPACE)) {

                        organDomain = strId;

                        break;
                    }
                }
            }
        }
        return organDomain;
    }

    /**
     * @param reader
     * @return
     * @throws NumberFormatException
     * @throws XMLStreamException
     */
    public long getDocumentId(XMLStreamReader reader)
            throws NumberFormatException, XMLStreamException {
        if (reader == null)
            return 0L;
        long documentId = 0L;
        while (reader.hasNext()) {
            int type = reader.next();
            if (type == XMLStreamReader.START_ELEMENT) {
                if (reader.getLocalName().equalsIgnoreCase("documentId")) {
                    reader.next();
                    String strId = reader.getText().trim();
                    if (!strId.equals("\n") && !strId.equals(" ")) {
                        documentId = Long.parseLong(strId);
                        LOGGER.info("Da lay thanh cong documentId");
                        break;
                    }
                }
            }
        }
        return documentId;
    }

    /**
     * @param document
     * @param elementName
     * @return
     * @throws XMLStreamException
     */
    public String getElementData(Document document, String elementName)
            throws XMLStreamException {
        DOMSource source = new DOMSource(document);
        XMLStreamReader reader = XMLInputFactory.newInstance()
                .createXMLStreamReader(source);
        if (reader == null) {
            return null;
        }
        String data = "";
        while (reader.hasNext()) {
            int type = reader.next();
            if (type == XMLStreamReader.START_ELEMENT) {
                if (reader.getLocalName().equalsIgnoreCase(elementName)) {
                    reader.next();
                    String tmpData = reader.getText().trim();
                    if (!tmpData.equals("\n") && !tmpData.equals(" ")) {
                        data = tmpData;
                        LOGGER.info("Get data success from " + elementName);
                        break;
                    }
                }
            }
        }
        return data;
    }

    public Namespace getEnvelopeNS(org.jdom2.Element rootElement) {
        Namespace envNs = null;
        List<Namespace> nss = rootElement.getNamespacesInScope();
        for (Namespace item : nss) {
            String uri = item.getURI();
            if (uri.equals(EdXmlConstant.SOAP_URI)) {
                envNs = item;
                break;
            }
        }
        return envNs;
    }

    public String getAttachmentName(Document envelope, String contentId)
            throws XPathExpressionException {
        String body = "//*[local-name()='Body'][1]//*[local-name()='Manifest'][1]";
        XPathExpression expr0 = xpathUtil.getXpathExpression(body);
        if (expr0 == null)
            return StringPool.BLANK;
        Node bodyNode = (Node) expr0.evaluate(envelope, XPathConstants.NODE);

        String reference = String
                .format("//*[local-name()='Reference'][@href='cid:%s']//*[local-name()='AttachmentName']",
                        contentId);

        XPathExpression expr = xpathUtil.getXpathExpression(reference);
        if (expr == null)
            return StringPool.BLANK;

        return expr.evaluate(bodyNode);
    }

    public Map<String, String> getAttachmentIds(Document envelope) {
        Map<String, String> result = new HashMap<>();

        org.jdom2.Document doc = XmlUtil.convertFromDom(envelope);

        org.jdom2.Element rootElement = doc.getRootElement();

        Namespace envNs = getEnvelopeNS(rootElement);

        org.jdom2.Element bodyNode = extractMine.getSingerElement(rootElement,
                EdXmlConstant.BODY_TAG, envNs);

        org.jdom2.Element maniNode = extractMine.getSingerElement(bodyNode,
                EdXmlConstant.MANIFEST_TAG, EdXmlConstant.EDXML_NS);

        List<org.jdom2.Element> referenceNodes = extractMine.getMultiElement(
                maniNode, EdXmlConstant.REFERENCE_TAG, EdXmlConstant.EDXML_NS);

        for (org.jdom2.Element item : referenceNodes) {
            // Get contentId from href link
            Attribute att = item.getAttribute(EdXmlConstant.HREF_ATTR);
            String href = att.getValue();

            // Get att name
            String attName = item.getChildText(
                    EdXmlConstant.ATTACHMENT_NAME_TAG, EdXmlConstant.EDXML_NS);

            result.put(attName, href.substring(href.indexOf(":") + 1));
        }

        return result;
    }


    public Document getMessHeaderDoc(MessageHeader messageHeader, OMNamespace ns)
            throws Exception {
        OMFactory factoryOM = OMAbstractFactory.getOMFactory();

        OMElement messageHeaderNode = factoryOM.createOMElement(
                StringPool.EDXML_MESSAGE_HEADER_BLOCK, ns);

        List<OMNode> nodes = getHeaderChild(messageHeader, ns);

        for (OMNode omNode : nodes) {
            messageHeaderNode.addChild(omNode);
        }

        nodes.clear();

        return XMLUtils.toDOM(messageHeaderNode).getOwnerDocument();
    }

    private List<OMNode> getHeaderChild(MessageHeader currentHeader,
                                        OMNamespace ns) {

        List<OMNode> nodes = new ArrayList<>();

        OMFactory factoryOM = OMAbstractFactory.getOMFactory();

        // Tao from Element
        OMElement from = factoryOM.createOMElement("From", ns);

        OMElement fromOrganId = factoryOM
                .createOMElement("OrganId", ns);

        fromOrganId.setText(currentHeader.getFrom().getOrganId());

        from.addChild(fromOrganId);


        OMElement fromOrganName = factoryOM.createOMElement(
                "OrganName", ns);

        fromOrganName.setText(currentHeader.getFrom().getOrganName());

        from.addChild(fromOrganName);


        OMElement fromOrganInCharge = factoryOM.createOMElement(
                "OrganInCharge", ns);

        fromOrganInCharge.setText(currentHeader.getFrom()
                .getOrganizationInCharge());

        from.addChild(fromOrganInCharge);


        OMElement fromOrganAdd = factoryOM.createOMElement("OrganAdd",
                ns);

        fromOrganAdd.setText(currentHeader.getFrom().getOrganAdd());

        from.addChild(fromOrganAdd);


        OMElement fromEmail = factoryOM.createOMElement("Email", ns);

        fromEmail.setText(currentHeader.getFrom().getEmail());

        from.addChild(fromEmail);


        OMElement fromTelephone = factoryOM.createOMElement(
                "Telephone", ns);

        fromTelephone.setText(currentHeader.getFrom().getTelephone());

        from.addChild(fromTelephone);


        OMElement fromFax = factoryOM.createOMElement("Fax", ns);

        fromFax.setText(currentHeader.getFrom().getFax());

        from.addChild(fromFax);


        OMElement fromWebsite = factoryOM
                .createOMElement("Website", ns);

        fromWebsite.setText(currentHeader.getFrom().getWebsite());

        from.addChild(fromWebsite);

        nodes.add(from);

        // Tao To element
        List<Organization> tos = currentHeader.getToes();
        Date dueDate = currentHeader.getDueDate();
        for (Organization item : tos) {

            OMElement to = factoryOM.createOMElement("To", ns);

            OMElement toOrganId = factoryOM.createOMElement("OrganId",
                    ns);
            toOrganId.setText(item.getOrganId());
            to.addChild(toOrganId);

            OMElement toOrganName = factoryOM.createOMElement(
                    "OrganName", ns);
            toOrganName.setText(item.getOrganName());
            to.addChild(toOrganName);

            OMElement toOrganAdd = factoryOM.createOMElement(
                    "OrganAdd", ns);
            toOrganAdd.setText(item.getOrganAdd());
            to.addChild(toOrganAdd);

            OMElement toEmail = factoryOM.createOMElement("Email", ns);
            toEmail.setText(item.getEmail());
            to.addChild(toEmail);

            OMElement toTelephone = factoryOM.createOMElement(
                    "Telephone", ns);
            toTelephone.setText(item.getTelephone());
            to.addChild(toTelephone);

            OMElement toFax = factoryOM.createOMElement("Fax", ns);
            toFax.setText(item.getFax());
            to.addChild(toFax);

            OMElement toWebsite = factoryOM.createOMElement("Website",
                    ns);
            toWebsite.setText(item.getWebsite());
            to.addChild(toWebsite);

            nodes.add(to);

        }

        // Tao <edXML:DocumentId>
        OMElement documentId = factoryOM.createOMElement("DocumentId", ns);
        documentId.setText(currentHeader.getDocumentId());
        nodes.add(documentId);

        // Tao Code
        OMElement code = factoryOM.createOMElement("Code", ns);

        OMElement codeNumber = factoryOM.createOMElement("CodeNumber",
                ns);
        codeNumber.setText(currentHeader.getCode().getCodeNumber());
        code.addChild(codeNumber);

        OMElement codeNotation = factoryOM.createOMElement(
                "CodeNotation", ns);
        codeNotation.setText(currentHeader.getCode().getCodeNotation());
        code.addChild(codeNotation);

        nodes.add(code);

        // Tao PromulgationInfo
        OMElement promulgationInfo = factoryOM.createOMElement(
                "PromulgationInfo", ns);

        OMElement promulgationDate = factoryOM.createOMElement(
                "PromulgationDate", ns);

        Date promulgationDateVal = currentHeader.getPromulgationInfo()
                .getPromulgationDate();
        String promulgationDateStr = DateUtils.format(promulgationDateVal, DateUtils.DEFAULT_DATE_FORMAT_REVERSE);
        promulgationDate
                .setText(promulgationDateStr);
        promulgationInfo.addChild(promulgationDate);

        OMElement promulgationPlace = factoryOM.createOMElement(
                "Place", ns);
        promulgationPlace.setText(currentHeader.getPromulgationInfo()
                .getPlace());
        promulgationInfo.addChild(promulgationPlace);

        nodes.add(promulgationInfo);

        // Tao DocumentType
        OMElement documentType = factoryOM.createOMElement("DocumentType",
                ns);

        OMElement type = factoryOM.createOMElement("Type", ns);
        LOGGER.info(currentHeader);
        String typeString = String.valueOf(currentHeader
                .getDocumentType().getType());
        type.setText(typeString.isEmpty() ? StringPool.DEAUlt_INTEGER
                : typeString);
        documentType.addChild(type);

        OMElement typeName = factoryOM.createOMElement("TypeName", ns);
        typeName.setText(currentHeader.getDocumentType().getTypeName());
        documentType.addChild(typeName);

        nodes.add(documentType);

        // Tao Subject
        OMElement subject = factoryOM.createOMElement("Subject", ns);
        subject.setText(currentHeader.getSubject());
        nodes.add(subject);

        // Tao Content
        OMElement content = factoryOM.createOMElement("Content", ns);
        content.setText(currentHeader.getContent());
        nodes.add(content);
        // Create signerInfo
        OMElement singerInfo = factoryOM.createOMElement("SignerInfo", ns);
        OMElement signerFullName = factoryOM.createOMElement("FullName", ns);
        String fullName = currentHeader.getSignerInfo().getFullName();

        signerFullName.setText(fullName == null ? StringPool.BLANK
                : fullName);
        singerInfo.addChild(signerFullName);

        OMElement signerPosition = factoryOM.createOMElement("Position", ns);
        signerPosition.setText(currentHeader.getSignerInfo().getPosition());
        singerInfo.addChild(signerPosition);

        OMElement competence = factoryOM.createOMElement("Competence", ns);
        competence.setText(currentHeader.getSignerInfo().getCompetence());
        singerInfo.addChild(competence);
        nodes.add(singerInfo);

        // Tao ResponseDate
        OMElement responseDate = factoryOM.createOMElement("DueDate", ns);
        //String dateStr = currentHeader.getResponseDate();
        Date dateStr = currentHeader.getDueDate();
        if (dateStr == null) {
            responseDate.setText("");
        } else {
            responseDate.setText(DateUtils.format(dateStr));
        }
        nodes.add(responseDate);

        // Tao ToPlaces
        OMElement toPlaces = factoryOM.createOMElement("ToPlaces", ns);
        for (String placeStr : currentHeader.getToPlaces()) {
            OMElement place = factoryOM.createOMElement("Place", ns);
            place.setText(placeStr);
            toPlaces.addChild(place);
        }
        nodes.add(toPlaces);

        // Tao OtherInfo
        OMElement otherInfo = factoryOM.createOMElement("OtherInfo", ns);


        OMElement priority = factoryOM.createOMElement("Priority", ns);
        String priorityStr = String.valueOf(currentHeader
                .getOtherInfo().getPriority());
        priority.setText(priorityStr.isEmpty() ? StringPool.DEAUlt_INTEGER
                : priorityStr);
        otherInfo.addChild(priority);

        OMElement sphere = factoryOM.createOMElement(
                "SphereOfPromulgation", ns);
        sphere.setText(currentHeader.getOtherInfo()
                .getSphereOfPromulgation());
        otherInfo.addChild(sphere);

        OMElement typerNotation = factoryOM.createOMElement(
                "TyperNotation", ns);
        typerNotation.setText(currentHeader.getOtherInfo()
                .getTyperNotation());
        otherInfo.addChild(typerNotation);

        OMElement promulgationAmount = factoryOM.createOMElement(
                "PromulgationAmount", ns);
        String promulgationAmountStr = String.valueOf(currentHeader
                .getOtherInfo().getPromulgationAmount());
        promulgationAmount
                .setText(promulgationAmountStr.isEmpty() ? StringPool.DEAUlt_INTEGER
                        : promulgationAmountStr);
        otherInfo.addChild(promulgationAmount);

        OMElement pageAmount = factoryOM.createOMElement("PageAmount",
                ns);
        String pageAmountStr = String.valueOf(currentHeader
                .getOtherInfo().getPageAmount());
        pageAmount
                .setText(pageAmountStr.isEmpty() ? StringPool.DEAUlt_INTEGER
                        : pageAmountStr);
        otherInfo.addChild(pageAmount);

        nodes.add(otherInfo);

        // create SteeringType
        OMElement steeringType = factoryOM.createOMElement("SteeringType", ns);
        steeringType.setText(String.valueOf(currentHeader.getSteeringType()));
        nodes.add(steeringType);

        // get response for revoke document
        // generate response for nodes
        List<ResponseFor> responseFors = currentHeader.getResponseFor();
        if (responseFors != null) {
            for (ResponseFor item : responseFors) {
                OMElement responseFor = factoryOM.createOMElement("ResponseFor", ns);

                OMElement organId = factoryOM.createOMElement("OrganId",
                        ns);
                organId.setText(item.getOrganId());
                responseFor.addChild(organId);


                OMElement codeResponseFor = factoryOM.createOMElement(
                        "Code", ns);
                code.setText(item.getCode());
                responseFor.addChild(codeResponseFor);

                OMElement promulgationDateResponseFor = factoryOM.createOMElement(
                        "PromulgationDate", ns);
                promulgationDate.setText(DateUtils.format(item.getPromulgationDate()));
                responseFor.addChild(promulgationDateResponseFor);


                OMElement documentIdResponseFor = factoryOM.createOMElement("DocumentId", ns);
                documentId.setText(item.getDocumentId());
                responseFor.addChild(documentIdResponseFor);
                nodes.add(responseFor);
            }
        }
        return nodes;
    }

    private static final Logger LOGGER = Logger.getLogger(XmlUtil.class);

}
