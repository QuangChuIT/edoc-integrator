package com.bkav.edoc.service.vpcp;

import com.bkav.edoc.service.database.cache.AttachmentCacheEntry;
import com.bkav.edoc.service.util.PropsUtil;
import com.bkav.edoc.service.xml.base.Content;
import com.bkav.edoc.service.xml.base.attachment.Attachment;
import com.bkav.edoc.service.xml.base.builder.BuildException;
import com.bkav.edoc.service.xml.base.header.*;
import com.bkav.edoc.service.xml.ed.Ed;
import com.bkav.edoc.service.xml.ed.builder.EdXmlBuilder;
import com.bkav.edoc.service.xml.ed.header.*;
import com.google.common.io.Files;

import java.io.File;
import java.util.Date;
import java.util.List;

public class EdxmlBuilder {
    private static EdxmlBuilder INSTANCE;

    static public EdxmlBuilder getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EdxmlBuilder();
        }
        return INSTANCE;
    }

    public Content create_edoc(MessageHeader messageHeader, TraceHeaderList traceHeaderList, List<AttachmentCacheEntry> attachments) throws BuildException {
        Ed ed = new Ed();
        // khoi tao code cho van ban
        Code code = messageHeader.getCode();
        // Clean code to send
        String codeNation = code.getCodeNotation();
        if (codeNation.contains("#")) {
            codeNation = codeNation.substring(0, codeNation.indexOf("#"));
        }
        code.setCodeNotation(codeNation);
        // khoi tao don vi gui
        Organization from = messageHeader.getFrom();

        // khoi tao danh sach don vi nhan
        List<Organization> toes = messageHeader.getToes();

        // khoi tao thong tin ban hanh
        PromulgationInfo promulgationInfo = messageHeader.getPromulgationInfo();

        // khoi tao loai van ban
        DocumentType docType = messageHeader.getDocumentType();

        // khoi tao thong tin nguoi ky
        SignerInfo signerInfo = messageHeader.getSignerInfo();
        // khai bao tieu de
        String subject = messageHeader.getSubject();
        // khai bao noi dung
        String context = messageHeader.getContent();

        List<String> toPlaces = messageHeader.getToPlaces();

        Date dueDate = messageHeader.getDueDate();

        OtherInfo otherInfo = messageHeader.getOtherInfo();
        // khoi tao header
        MessageHeader header = new MessageHeader(from, toes, code, promulgationInfo, docType, subject,
                context, null, signerInfo, dueDate, toPlaces, otherInfo);

        // khoi tao loai chi dao
        header.setSteeringType(1);

        // khoi tao thong tin ma dinh danh cua van ban
        header.setDocumentId(messageHeader.getDocumentId());

        // khoi tao thong tin van ban phan hoi va phuc dap
        List<ResponseFor> responseFors = messageHeader.getResponseFor();

        header.setResponseFor(responseFors);

        // khoi tao thong tin chu ky
        /*SignReference signReference = new SignReference("", "http://www.w3.org/2000/09/xmldsig#sha1",
                "FwgIqsSYJshUS2+wlOM61L+q7Aw=");
        signReference.addToTransform("http://www.w3.org/2000/09/xmldsig#enveloped-signature");
        signReference.addToTransform("http://www.w3.org/TR/xml-exc-c14n#");
        List<SignReference> listSignReference = new ArrayList<>();
        listSignReference.add(signReference);
        SignedInfo signedInfo = new SignedInfo("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", "http://www.w3.org/2000/09/xmldsig#rsa-sha1", listSignReference);
        String SignatureValue = "GbVb8n9+qFxz466Ag0sbpVdLPs2R0+9JBekp12UyarAZjoG0W/kPZZJ1auRZDcNcrSwzgkQMpqrjwxcy3ejzbY/ON5USUPgoYNYM8p4wsgQpEAeQaZ+EWLkkEBxYjb+iWEiAjYuJI7gtJoOyENcOK4fO050SXp2ctOc32LJMA5eEI6Hw7sxhc2LAgcPiynJHdDW2Z+eut6QZiUsbIF9+S3T6u/tfLImw39dgSlCxupwLPHepxuiLOqyd08HeJGCZufg9WqRBVyLFM76uCIaPRP5wwAdx72GVjPcG2kh+2jjrt7fqtJOufJzCObtQgPgBqIvDiZCGoOM41OQMiqtF3w==";
        KeyInfo keyInfo = new KeyInfo(new X509Data("CN=user05", "MIIFqTCCBJG"));*/
        Signature signature = new Signature();

        // khoi tao header van ban
        ed.setHeader(new Header(header, traceHeaderList, signature));

        // khoi tao file dinh kem
        for (AttachmentCacheEntry attachmentCacheEntry : attachments) {
            File file = new File(attachmentCacheEntry.getRelativePath());
            Attachment attachment = new Attachment(attachmentCacheEntry.getAttachmentId().toString(),
                    attachmentCacheEntry.getName(), attachmentCacheEntry.getName(), file);
            String ext = Files.getFileExtension(attachmentCacheEntry.getName());
            attachment.setFormat(ext);
            attachment.setContentType(attachmentCacheEntry.getFileType());
            ed.addAttachment(attachment);
        }
        // ghi file ra thu muc
        String fileName = "SendDocument" + "." + messageHeader.getFrom().getOrganId();
        String filePath = PropsUtil.get("VPCP.attachment.dir");
        return EdXmlBuilder.build(ed, fileName, filePath);
    }
}
