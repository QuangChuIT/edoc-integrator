package com.bkav.edoc.sdk.edxml.mineutil;

import com.bkav.edoc.sdk.edxml.entity.*;
import com.bkav.edoc.sdk.edxml.entity.env.Envelop;
import com.bkav.edoc.sdk.edxml.util.UUidUtils;
import com.bkav.edoc.sdk.edxml.util.XmlUtils;
import com.bkav.edoc.sdk.resource.EdXmlConstant;
import com.google.common.io.Files;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.util.XMLUtils;
import org.w3c.dom.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArchiveMime {
    private static final ArchiveMime INSTANCE = new ArchiveMime();

    public static ArchiveMime getInstance() {
        return INSTANCE;
    }

    public File createStatus(MessageStatus messageStatus, String fileName, String path) {
        try {
            Document document = XmlUtils.convertEntityToDocument(MessageStatus.class, messageStatus);
            if (document != null) {
                OMElement node = XMLUtils.toOM(document.getDocumentElement());
                OMFactory factoryOM = OMAbstractFactory.getOMFactory();
                OMNamespace ns = factoryOM.createOMNamespace(
                        EdXmlConstant.EDXML_URI, EdXmlConstant.EDXML_PREFIX);
                OMNamespace omNamespace = factoryOM.createOMNamespace(EdXmlConstant.EDXML_URI, "");
                OMElement rootElement = factoryOM.createOMElement("edXML", omNamespace);
                OMElement envelopElement = factoryOM.createOMElement("edXMLEnvelope", ns);
                OMElement headerElement = factoryOM.createOMElement("edXMLHeader", ns);
                headerElement.addChild(node);
                envelopElement.addChild(headerElement);
                rootElement.addChild(envelopElement);
                Document status = XMLUtils.toDOM(rootElement).getOwnerDocument();
                return XmlUtils.buildContent(status, fileName, path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public File createMime(Envelop envelop, String fileName, String path) {
        File tmpFile = Files.createTempDir();
        try {
            OMFactory factoryOM = OMAbstractFactory.getOMFactory();

            OMNamespace ns = factoryOM.createOMNamespace(
                    EdXmlConstant.EDXML_URI, EdXmlConstant.EDXML_PREFIX);
            OMNamespace omNamespace = factoryOM.createOMNamespace(EdXmlConstant.EDXML_URI, "");
            OMElement rootElement = factoryOM.createOMElement("edXML", omNamespace);
            OMElement envelopElement = factoryOM.createOMElement("edXMLEnvelope", ns);
            OMElement headerElement = factoryOM.createOMElement("edXMLHeader", ns);
            // Create MessageHeader
            OMElement messageHeader = XmlUtils.getMessHeaderDoc(envelop.getHeader()
                    .getMessageHeader(), ns);
            System.out.println("Success convert message header in create mine with attachment size " + envelop.getAttachments().size() + " !!!!!!!!!!!!!!!!!!!!!");
            // Create TraceHeaderList
            OMElement traceHeaderList = XmlUtils.getTraceHeaderDoc(envelop.getHeader()
                    .getTraceHeaderList(), ns);
            System.out.println("Success convert trace header list in create mine with attachment size " + envelop.getAttachments().size() + " !!!!!!!!!!!!!!!!!!!!!!!!!");
            // Create Signature
            OMElement signature = XmlUtils.getSignatureDoc(envelop.getHeader().getSignature());
            headerElement.addChild(messageHeader);
            headerElement.addChild(traceHeaderList);
            headerElement.addChild(signature);
            envelopElement.addChild(headerElement);
            List<Reference> references = new ArrayList<>();
            List<Attachment> attachments = new ArrayList<>();
            // Create body
            if (envelop.getAttachments().size() > 0) {
                for (Attachment attachment : envelop.getAttachments()) {
                    // create content id for attachment
                    String contentId = UUidUtils.generate();
                    String contentType = attachment.getContentType();
                    // Create reference on body
                    Reference reference = new Reference();
                    reference.setContentId("cid:" + contentId);
                    reference.setAttachmentName(attachment.getName());
                    reference.setContentType(contentType);
                    reference.setDescription(attachment.getDescription());
                    references.add(reference);
                    attachment.setContentId(contentId);
                    attachments.add(attachment);
                }
            }

            Manifest manifest = new Manifest();

            manifest.setReferences(references);

            OMElement body = XmlUtils.getBodyChildDoc(manifest, ns);
            envelopElement.addChild(body);
            // Create Attachment
            OMElement attachmentEncoded = XmlUtils.getAttachmentDoc(attachments, ns, tmpFile.getAbsolutePath());
            rootElement.addChild(envelopElement);
            rootElement.addChild(attachmentEncoded);
            Document document = XMLUtils.toDOM(rootElement).getOwnerDocument();
            return XmlUtils.buildContent(document, fileName, path);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            File[] files = tmpFile.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    file.delete();
                }
            }

            tmpFile.delete();
        }
        return null;
    }

    public static void main(String[] args) {
        MessageStatus msgStatus = new MessageStatus();

        //set ResponseFor Tag
        msgStatus.setResponseFor(new ResponseFor("000.00.00.G22", "7816/VPCP-TTĐT", new Date(), "000.00.00.G22,2015/09/30,7816/VPCP-TTĐT"));
        //set from information (organization)
        msgStatus.setFrom(new Organization(
                "000.00.00.H41",
                "UBND Tỉnh Nghệ An",
                "UBND Tỉnh Nghệ An",
                "Số 03, đường Trường Thi, Thành phố Vinh, Tỉnh Nghệ An, Việt Nam", "nghean@gov.vn", "0383 840418", "0383 843049", "www.nghean.vn"
        ));

        //set status code info
        msgStatus.setStatusCode("01");
        msgStatus.setDescription("Văn thư - Phòng Văn thư - Lưu trữ: Đã đến - Phần mềm QLVB đã nhận nhưng văn thư chưa xử lý");
        msgStatus.setTimestamp(new Date());

        //set staff details
        StaffInfo staffInfo = new StaffInfo();
        staffInfo.setDepartment("Văn thư văn phòng");
        staffInfo.setStaff("Nguyễn Thị Ngọc Trâm");
        staffInfo.setEmail("vanthuvanphong@gov.vn");
        staffInfo.setMobile("84912000001");
        msgStatus.setStaffInfo(staffInfo);
        File file = ArchiveMime.getInstance().createStatus(msgStatus, "Status", "/home/quangcv/edoc");
        if (file != null) {
            System.out.println(file.getAbsolutePath());
        } else {
            System.out.println("Error");
        }
        ArchiveMime.getInstance().createStatus(msgStatus, "Test","/home/quangcv/edoc");
    }
}
