package com.bkav.edoc.sdk.edxml.entity;

import com.bkav.edoc.sdk.edxml.util.ArchiveUtils;
import com.bkav.edoc.sdk.edxml.util.AttachmentGlobalUtils;
import com.bkav.edoc.sdk.edxml.util.EdxmlUtils;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
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
        this.format = AttachmentGlobalUtils.getFileExtension(fileName);
        this.name = fileName;
    }

    public Attachment(String contentId, String fileName, File content) {
        this.format = AttachmentGlobalUtils.getFileExtension(fileName);
        this.name = fileName;
        this.content = content;
        this.contentId = contentId;
    }

    public Attachment(String contentId, String fileName, String description, File file) {
        this.format = AttachmentGlobalUtils.getFileExtension(fileName);
        this.name = fileName;
        this.description = description;
        this.content = file;
        this.contentId = contentId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentTransferEncoded() {
        return contentTransferEncoded;
    }

    public void setContentTransferEncoded(String contentTransferEncoded) {
        this.contentTransferEncoded = contentTransferEncoded;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getContent() {
        return content;
    }

    public void setContent(File content) {
        this.content = content;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
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

    public static Attachment getData(Element element, String filePath) throws IOException {
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
                    attachment.setContentId(EdxmlUtils.getContentId(children.getText()));
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
                file = ArchiveUtils.unzip(file);
                attachment.setContent(file);
            }
            InputStream inputStream = new FileInputStream(file);
            attachment.setInputStream(inputStream);
        } catch (Exception e) {
            throw new IOException("Error parse content file of attachment with id " + attachment.getContentId());
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
}
