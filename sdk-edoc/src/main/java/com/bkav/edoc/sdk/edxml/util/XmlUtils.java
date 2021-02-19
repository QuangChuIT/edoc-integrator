package com.bkav.edoc.sdk.edxml.util;

import com.bkav.edoc.sdk.edxml.entity.*;
import com.bkav.edoc.sdk.resource.EdXmlConstant;
import com.bkav.edoc.sdk.util.StringPool;
import com.bkav.edoc.sdk.util.Validator;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteStreams;
import org.apache.axiom.om.*;
import org.jdom2.Namespace;
import org.jdom2.input.DOMBuilder;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class XmlUtils {

    public static File buildContent(Document document, String fileName, String path) {
        try {
            File file = new File(path, fileName + "." + UUID.randomUUID().toString() + ".edxml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            // for output to file, console
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            // for pretty print
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(document);

            // write to console or file
            StreamResult console = new StreamResult(System.out);
            StreamResult streamResult = new StreamResult(file);

            // write data
            transformer.transform(source, streamResult);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static org.jdom2.Document convertFromDom(Document document) {
        DOMBuilder builder = new DOMBuilder();
        return builder
                .build(document);
    }

    public static Namespace getEnvelopeNS(org.jdom2.Element rootElement) {
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

    public static OMElement getTraceHeaderDoc(TraceHeaderList traceHeaderList,
                                              OMNamespace ns) {

        OMFactory factoryOM = OMAbstractFactory.getOMFactory();

        OMElement traceHeaderNode = factoryOM.createOMElement(

                StringPool.EDXML_TRACE_HEADER_BLOCK, ns);
        if (traceHeaderList != null) {
            List<OMNode> traceNodes = getTraceListChild(traceHeaderList, ns);

            // Add child node to trace header list
            for (OMNode omNode : traceNodes) {

                traceHeaderNode.addChild(omNode);

            }
            traceNodes.clear();
        }
        return traceHeaderNode;
    }

    public static OMElement getBodyChildDoc(Manifest manifest, OMNamespace ns) {
        if (manifest == null)
            return null;
        OMFactory factoryOM = OMAbstractFactory.getOMFactory();
        OMElement body = factoryOM.createOMElement(StringPool.EDXML_BODY_BLOCK, ns);
        OMElement manifestNode = factoryOM.createOMElement(StringPool.EDXML_MANIFEST_BLOCK, ns);
        OMAttribute version = factoryOM.createOMAttribute("version", ns, manifest.getVersion());
        manifestNode.addAttribute(version);
        List<Reference> refs = manifest.getReferences();
        for (Reference item : refs) {
            OMElement reference = factoryOM.createOMElement("Reference", ns);
            OMElement contentId = factoryOM.createOMElement("ContentId", ns);
            contentId.setText(item.getContentId());
            OMElement contentType = factoryOM.createOMElement("ContentType", ns);
            contentType.setText(item.getContentType());
            OMElement attachmentName = factoryOM.createOMElement(
                    "AttachmentName", ns);
            attachmentName.setText(item.getAttachmentName());

            OMElement description = factoryOM
                    .createOMElement("Description", ns);
            description.setText(item.getDescription());
            reference.addChild(contentId);
            reference.addChild(contentType);
            reference.addChild(attachmentName);
            reference.addChild(description);
            manifestNode.addChild(reference);
        }
        body.addChild(manifestNode);
        return body;
    }

    private static List<OMNode> getTraceListChild(TraceHeaderList traceHeaderList,
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

            timestamp.setText(sdf.format(item.getTimestamp()));

            traceHeader.addChild(timestamp);

            nodes.add(traceHeader);
        }

        // get business node
        OMElement businessNode = getBusinessChild(traceHeaderList, ns);
        nodes.add(businessNode);

        return nodes;
    }

    private static OMElement getBusinessChild(TraceHeaderList traceHeaderList, OMNamespace ns) {
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
        // replacement info list
        if (business.getBusinessDocType() == BusinessDocType.REPLACE.ordinal()) {
            OMElement replacementInfoListNode = getReplacementInfoListNode(business.getReplacementInfoList(), ns);
            businessNode.addChild(replacementInfoListNode);
        } else if (business.getBusinessDocType() == BusinessDocType.UPDATE.ordinal()) {
            OMElement businessDocumentInfoNode = getBusinessDocumentInfoNode(business.getBusinessDocumentInfo(), ns);
            businessNode.addChild(businessDocumentInfoNode);
        }

        return businessNode;
    }

    private static OMElement getReplacementInfoListNode(ReplacementInfoList replacementInfoList, OMNamespace ns) {
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

    private static OMElement getBusinessDocumentInfoNode(BusinessDocumentInfo businessDocumentInfo, OMNamespace ns) {
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

    private static OMElement getStaffInfoNode(StaffInfo staffInfo, OMNamespace ns) {
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

    public static OMElement getMessHeaderDoc(MessageHeader messageHeader, OMNamespace ns) {
        OMFactory factoryOM = OMAbstractFactory.getOMFactory();

        OMElement messageHeaderNode = factoryOM.createOMElement(
                StringPool.EDXML_MESSAGE_HEADER_BLOCK, ns);

        List<OMNode> nodes = getHeaderChild(messageHeader, ns);

        for (OMNode omNode : nodes) {
            messageHeaderNode.addChild(omNode);
        }

        nodes.clear();

        return messageHeaderNode;
    }

    private static List<OMNode> getHeaderChild(MessageHeader currentHeader,
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

        // Create <edXML:DocumentId>
        OMElement documentId = factoryOM.createOMElement("DocumentId", ns);
        documentId.setText(currentHeader.getDocumentId());
        nodes.add(documentId);

        // Create Code
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

        // Create PromulgationInfo
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

        // Create DocumentType
        OMElement documentType = factoryOM.createOMElement("DocumentType",
                ns);

        OMElement type = factoryOM.createOMElement("Type", ns);
        System.out.println(currentHeader);
        String typeString = String.valueOf(currentHeader
                .getDocumentType().getType());
        type.setText(typeString.isEmpty() ? StringPool.DEAUlt_INTEGER
                : typeString);
        documentType.addChild(type);

        OMElement typeName = factoryOM.createOMElement("TypeName", ns);
        typeName.setText(currentHeader.getDocumentType().getTypeName());
        documentType.addChild(typeName);

        nodes.add(documentType);

        // create Subject
        OMElement subject = factoryOM.createOMElement("Subject", ns);
        subject.setText(currentHeader.getSubject());
        nodes.add(subject);

        // create Content
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

        // Create Due Date
        OMElement responseDate = factoryOM.createOMElement("DueDate", ns);
        //String dateStr = currentHeader.getResponseDate();
        Date dateStr = currentHeader.getDueDate();
        if (dateStr == null) {
            responseDate.setText("");
        } else {
            responseDate.setText(DateUtils.format(dateStr));
        }
        nodes.add(responseDate);

        // Create ToPlaces
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
        // Create Appendixes
        OMElement appendixes = factoryOM.createOMElement("Appendixes", ns);
        for (String appStr : currentHeader.getOtherInfo().getAppendixes()) {
            OMElement appendix = factoryOM.createOMElement("Appendix", ns);
            appendix.setText(appStr);
            appendixes.addChild(appendix);
        }
        otherInfo.addChild(appendixes);
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

    public static OMElement getSignatureDoc(Signature signature) {
        OMFactory factoryOM = OMAbstractFactory.getOMFactory();

        OMElement signatureNode = factoryOM.createOMElement(
                StringPool.EDXML_SIGNATURE_BLOCK, null);

        List<OMNode> nodes = getSignatureChild(signature);

        for (OMNode omNode : nodes) {
            signatureNode.addChild(omNode);
        }

        nodes.clear();

        return signatureNode;
    }

    private static List<OMNode> getSignatureChild(Signature signature) {
        List<OMNode> nodes = new ArrayList<>();

        OMFactory factoryOM = OMAbstractFactory.getOMFactory();

        SignedInfo signedInfoValue = signature.getSignedInfo();
        // Create SignedInfo
        OMElement signedInfo = factoryOM.createOMElement("SignedInfo", null);
        if (signedInfoValue != null) {

            OMAttribute algorithm1 = factoryOM.createOMAttribute("Algorithm", null, signedInfoValue.getCanonicalizationMethod());

            OMElement canonicalizationMethod = factoryOM.createOMElement("CanonicalizationMethod", null);
            canonicalizationMethod.addAttribute(algorithm1);
            signedInfo.addChild(canonicalizationMethod);

            OMAttribute algorithm2 = factoryOM.createOMAttribute("Algorithm", null, signedInfoValue.getSignatureMethod());
            OMElement signatureMethod = factoryOM.createOMElement("SignatureMethod", null);
            signatureMethod.addAttribute(algorithm2);
            signedInfo.addChild(signatureMethod);
            if (signedInfoValue.getReference() != null && signedInfoValue.getReference().size() > 0) {
                SignReference signReference = signedInfoValue.getReference().get(0);
                OMElement reference = factoryOM.createOMElement("Reference", null);
                String uriValue = "";
                if (!Validator.isNullOrEmpty(signReference.getURI())) {
                    uriValue = signReference.getURI();
                }
                OMAttribute uri = factoryOM.createOMAttribute("URI", null, uriValue);
                reference.addAttribute(uri);
                OMElement transforms = factoryOM.createOMElement("Transforms", null);
                if (signReference.getTransforms() != null && signReference.getTransforms().size() > 0) {
                    for (String transformStr : signReference.getTransforms()) {
                        OMElement transform = factoryOM.createOMElement("Transform", null);
                        OMAttribute algorithm = factoryOM.createOMAttribute("Algorithm", null, transformStr);
                        transform.addAttribute(algorithm);
                        transforms.addChild(transform);
                    }
                }
                reference.addChild(transforms);
                OMElement digestMethod = factoryOM.createOMElement("DigestMethod", null);
                digestMethod.setText(signReference.getDigestMethod());
                reference.addChild(digestMethod);

                OMElement digestValue = factoryOM.createOMElement("DigestValue", null);
                digestValue.setText(signReference.getDigestValue());
                reference.addChild(digestValue);
                signedInfo.addChild(reference);
            }
            nodes.add(signedInfo);
        }
        // Create Signature Value
        OMElement signatureValue = factoryOM.createOMElement("SignatureValue", null);
        signatureValue.setText(signature.getSignatureValue());
        nodes.add(signatureValue);

        // Create KeyInfo
        OMElement keyInfo = factoryOM.createOMElement("KeyInfo", null);
        OMElement x509Data = factoryOM.createOMElement("X509Data", null);

        OMElement x509SubjectName = factoryOM.createOMElement("X509SubjectName", null);
        x509SubjectName.setText(signature.getKeyInfo().getX509Data().getX509SubjectName());
        x509Data.addChild(x509SubjectName);

        OMElement x509Certificate = factoryOM.createOMElement("X509Certificate", null);
        x509Certificate.setText(signature.getKeyInfo().getX509Data().getX509Certificate());
        x509Data.addChild(x509Certificate);
        keyInfo.addChild(x509Data);
        nodes.add(keyInfo);
        return nodes;
    }

    public static OMElement getAttachmentDoc(List<Attachment> attachments, OMNamespace ns, String path) throws IOException {
        OMFactory factoryOM = OMAbstractFactory.getOMFactory();

        OMElement attachmentNode = factoryOM.createOMElement(
                StringPool.EDXML_ATTACHMENT_BLOCK, ns);
        for (Attachment attachment : attachments) {
            InputStream inputStream;
            String contentType = attachment.getContentType();
            if ("pdf".equals(attachment.getFormat().toLowerCase())) {
                inputStream = attachment.getInputStreamFromFile();
                contentType = "application/pdf";
            } else if ("doc".equals(attachment.getFormat().toLowerCase())) {
                contentType = "application/msword";
                inputStream = attachment.getInputStreamFromFile();
            } else if ("docx".equals(attachment.getFormat().toLowerCase())) {
                contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                inputStream = attachment.getInputStreamFromFile();
            } else if ("xls".equals(attachment.getFormat().toLowerCase())) {
                contentType = "application/vnd.ms-excel";
                inputStream = attachment.getInputStreamFromFile();
            } else if ("xslx".equals(attachment.getFormat().toLowerCase())) {
                contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                inputStream = attachment.getInputStreamFromFile();
            } else if ("zip".equals(attachment.getFormat().toLowerCase())) {
                contentType = "application/zip";
                inputStream = attachment.getInputStreamFromFile();
            } else {
                contentType = "application/zip";
                inputStream = new FileInputStream(ArchiveUtils.zip(attachment.getInputStreamFromFile(), attachment.getFormat(), path));
            }
            String contentId = attachment.getContentId();
            String attName = attachment.getName();
            String description = attachment.getDescription();
            String contentTransferEncoded = BaseEncoding.base64().encode(ByteStreams.toByteArray(inputStream));
            OMElement attachmentElement = factoryOM.createOMElement("Attachment", ns);

            OMElement contentIdNode = factoryOM.createOMElement("ContentId", ns);
            contentIdNode.setText(contentId);
            attachmentElement.addChild(contentIdNode);

            OMElement attachmentName = factoryOM.createOMElement("AttachmentName", ns);
            attachmentName.setText(attName);
            attachmentElement.addChild(attachmentName);

            OMElement descriptionNode = factoryOM.createOMElement("Description", ns);
            descriptionNode.setText(description);
            attachmentElement.addChild(descriptionNode);

            OMElement contentTypeNode = factoryOM.createOMElement("ContentType", ns);
            contentTypeNode.setText(contentType);
            attachmentElement.addChild(contentTypeNode);

            OMElement contentTransferEncodedNode = factoryOM.createOMElement("ContentTransferEncoded", ns);

            contentTransferEncodedNode.setText(contentTransferEncoded);
            attachmentElement.addChild(contentTransferEncodedNode);
            attachmentNode.addChild(attachmentElement);
            inputStream.close();
        }
        return attachmentNode;
    }

    public static org.jdom2.Document getDocument(InputStream inputStream) {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
            org.w3c.dom.Document document = documentBuilder.parse(inputStream);
            DOMBuilder domBuilder = new DOMBuilder();
            return domBuilder.build(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
