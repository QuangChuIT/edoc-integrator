package com.bkav.edoc.service.xml.base.attachment;

import com.bkav.edoc.service.resource.EdXmlConstant;
import com.bkav.edoc.service.util.AttachmentGlobalUtil;
import com.bkav.edoc.service.xml.base.util.BaseXmlUtils;
import com.bkav.edoc.service.xml.base.util.CompressUtils;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import org.apache.commons.codec.binary.Base64;
import org.jdom2.Element;

import java.io.*;
import java.util.List;
import java.util.UUID;

public class Attachment {
    private String contentType = "application/zip";
    private String contentTransferEncoded;
    private String contentId;
    private String name;
    private String description;
    private File content;
    private InputStream inputStream;
    private String format;

    public Attachment() {
    }

    public Attachment(String fileName) {
        this.format = AttachmentGlobalUtil.getFileExtension(fileName);
        this.name = fileName;
    }

    public Attachment(String fileName, File file) {
        this.format = AttachmentGlobalUtil.getFileExtension(fileName);
        this.name = fileName;
        this.content = file;
    }

    public Attachment(String contentId, String fileName, File content) {
        this.format = AttachmentGlobalUtil.getFileExtension(fileName);
        this.name = fileName;
        this.content = content;
        this.contentId = contentId;
    }

    public Attachment(String contentId, String fileName, String description, File file) {
        this.format = AttachmentGlobalUtil.getFileExtension(fileName);
        this.name = fileName;
        this.description = description;
        this.content = file;
        this.contentId = contentId;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String paramString) {
        this.contentType = paramString;
    }

    public String getContentTransferEncoded() {
        return this.contentTransferEncoded;
    }

    public void setContentTransferEncoded(String paramString) {
        this.contentTransferEncoded = paramString;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String paramString) {
        this.name = paramString;
    }

    public File getContent() {
        return this.content;
    }

    public void setContent(File paramFile) {
        this.content = paramFile;
    }

    public InputStream getInputStreamFromFile() throws IOException {
        return this.content != null ? new FileInputStream(this.content) : null;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setFormat(String paramString) {
        this.format = paramString;
    }

    public String getFormat() {
        if (Strings.isNullOrEmpty(this.format)) {
            this.format = "unk";
        }

        return this.format;
    }

    public String getContentId() {
        return this.contentId;
    }

    public void setContentId(String paramString) {
        if (!Strings.isNullOrEmpty(paramString)) {
            this.contentId = "cid:" + paramString;
        }
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String paramString) {
        this.description = paramString;
    }

    public void accumulate(Element element, String fileName) throws IOException {
        Element attachment = this.accumulateWithoutPrefix(element, "Attachment");
        if (this.contentId == null) {
            this.contentId = UUID.randomUUID().toString();
        }
        String str = "cid:" + this.contentId;
        this.accumulateWithoutPrefix(attachment, "ContentId", str);
        this.accumulateWithoutPrefix(attachment, "AttachmentName", this.name);
        this.accumulateWithoutPrefix(attachment, "Description", this.description);
        InputStream inputStream;
        if ("pdf".equals(this.getFormat().toLowerCase())) {
            this.contentType = "application/pdf";
            inputStream = this.getInputStreamFromFile();
        } else if ("doc".equals(this.getFormat().toLowerCase())) {
            this.contentType = "application/msword";
            inputStream = this.getInputStreamFromFile();
        } else if ("docx".equals(this.getFormat().toLowerCase())) {
            this.contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            inputStream = this.getInputStreamFromFile();
        } else if ("xls".equals(this.getFormat().toLowerCase())) {
            this.contentType = "application/vnd.ms-excel";
            inputStream = this.getInputStreamFromFile();
        } else if ("xslx".equals(this.getFormat().toLowerCase())) {
            this.contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            inputStream = this.getInputStreamFromFile();
        } else if ("zip".equals(this.getFormat().toLowerCase())) {
            this.contentType = "application/zip";
            inputStream = this.getInputStreamFromFile();
        } else {
            this.contentType = "application/zip";
            inputStream = new FileInputStream(CompressUtils.zip(this.getInputStreamFromFile(), this.getFormat(), fileName));
        }

        this.accumulateWithoutPrefix(attachment, "ContentType", this.contentType);
        this.contentTransferEncoded = BaseEncoding.base64().encode(ByteStreams.toByteArray(inputStream));
        this.accumulate(attachment, "ContentTransferEncoded", this.contentTransferEncoded);
        inputStream.close();
    }

    public static Attachment fromContent(Element element, String filePath) throws IOException {
        Attachment attachment = new Attachment();
        List<Element> elementList = element.getChildren();
        if (elementList != null && elementList.size() != 0) {
            for (Element children : elementList) {
                if ("ContentType".equals(children.getName())) {
                    attachment.setContentType(children.getText());
                }

                if ("ContentTransferEncoded".equals(children.getName())) {
                    attachment.setContentTransferEncoded(children.getText());
                }

                if ("ContentId".equals(children.getName())) {
                    attachment.setContentId(BaseXmlUtils.resolveContentId(children.getText()));
                }

                if ("AttachmentName".equals(children.getName())) {
                    attachment.setName(children.getText());
                }

                if ("Description".equals(children.getName())) {
                    attachment.setDescription(children.getText());
                }

                if ("Format".equals(children.getName())) {
                    attachment.setFormat(children.getText());
                }
            }

            String attachmentFormat = attachment.getFormat();
            if (Strings.isNullOrEmpty(attachmentFormat) || "unk".equals(attachmentFormat)) {
                attachment.setFormat(getFormat(attachment.getName()));
            }

            parseContentFile(attachment, filePath);
            return attachment;
        } else {
            return null;
        }
    }

    private static String getFormat(String fileName) {
        String str = Files.getFileExtension(fileName);
        if (Strings.isNullOrEmpty(str)) {
            return "unk";
        } else {
            if (str.charAt(str.length() - 1) == '>') {
                str = str.substring(0, str.length() - 1);
            }

            return Strings.isNullOrEmpty(str) ? "unk" : str;
        }
    }

    private static void parseContentFile(Attachment attachment, String filePath) throws IOException {
        try {
            File file = new File(filePath, UUID.randomUUID().toString() + "." + attachment.getFormat());
            Files.write(Base64.decodeBase64(attachment.getContentTransferEncoded()), file);
            if ("application/zip".equals(attachment.getContentType()) && !"zip".equals(attachment.getFormat())) {
                file = CompressUtils.unzip(file);
                attachment.setContent(file);
            }
            InputStream inputStream = new FileInputStream(file);
            attachment.setInputStream(inputStream);
        } catch (Exception e) {
            throw new IOException("Error when parse content file of attachment with id " + attachment.getContentId());
        }
    }

    private static void parseContentFile(Attachment attachment) {
        try {
            byte[] fileBytes = Base64.decodeBase64(attachment.getContentTransferEncoded());
            InputStream inputStream = new ByteArrayInputStream(fileBytes);
            attachment.setInputStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        try {
            return MoreObjects.toStringHelper(super.getClass())
                    .add("contentType", this.contentType)
                    .add("name", this.name).add("format", this.format)
                    .add("description", this.description)
                    .add("content", this.content)
                    .add("fileLength", this.inputStream.available())
                    .toString();
        } catch (IOException e) {
            e.printStackTrace();
            return MoreObjects.toStringHelper(super.getClass())
                    .add("contentType", this.contentType)
                    .add("name", this.name).add("format", this.format)
                    .add("description", this.description)
                    .add("content", this.content)
                    .toString();
        }
    }

    protected Element accumulateWithoutPrefix(Element element, String elementName, String value) {
        if (Strings.isNullOrEmpty(value)) {
            return null;
        } else {
            Element newElement = new Element(elementName, EdXmlConstant.EDXML_URI);
            newElement.setText(value);
            element.addContent(newElement);
            return newElement;
        }
    }

    protected Element accumulateWithoutPrefix(Element element, String attrName) {
        Element newElement = new Element(attrName, EdXmlConstant.EDXML_URI);
        element.addContent(newElement);
        return newElement;
    }

    protected Element accumulate(Element element, String elementName, String value) {
        if (Strings.isNullOrEmpty(value)) {
            return null;
        } else {
            Element newElement = new Element(elementName, "edXML", EdXmlConstant.EDXML_URI);
            newElement.setText(value);
            element.addContent(newElement);
            return newElement;
        }
    }

}
