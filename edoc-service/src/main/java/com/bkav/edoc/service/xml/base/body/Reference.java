package com.bkav.edoc.service.xml.base.body;

import com.bkav.edoc.service.xml.base.BaseElement;
import com.bkav.edoc.service.xml.base.util.BaseXmlUtils;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.jdom2.Element;

import java.util.Iterator;
import java.util.List;

public class Reference extends BaseElement {
    private String contentId;
    private String contentType;
    private String attachmentName;
    private String description;

    public Reference() {
    }

    public Reference(String contentId, String contentType, String attachmentName, String description) {
        this.contentId = contentId;
        this.contentType = contentType;
        this.attachmentName = attachmentName;
        this.description = description;
    }

    public String getContentId() {
        return this.contentId;
    }

    public void setContentId(String paramString) {
        this.contentId = paramString;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String paramString) {
        this.contentType = paramString;
    }

    public String getAttachmentName() {
        return this.attachmentName;
    }

    public void setAttachmentName(String paramString) {
        this.attachmentName = paramString;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String paramString) {
        this.description = paramString;
    }

    public static Reference fromContent(Element element) {
        Reference localReference = new Reference();
        List<Element> elements = element.getChildren();
        if (elements != null && elements.size() != 0) {
            for (Element thisElement : elements) {
                if ("Description".equals(thisElement.getName())) {
                    localReference.setDescription(thisElement.getText());
                }

                if ("AttachmentName".equals(thisElement.getName())) {
                    localReference.setAttachmentName(thisElement.getText());
                }

                if ("ContentId".equals(thisElement.getName())) {
                    localReference.setContentId(BaseXmlUtils.resolveContentId(thisElement.getText()));
                }

                if ("ContentType".equals(thisElement.getName())) {
                    localReference.setContentType(thisElement.getText());
                }
            }
            return localReference;
        } else {
            return null;
        }
    }

    public void accumulate(Element element) {
        Element reference = this.accumulate(element, "Reference");
        String str = "cid:" + this.contentId;
        this.accumulate(reference, "ContentId", str);
        this.accumulate(reference, "ContentType", this.contentType);
        this.accumulate(reference, "Description", this.description);
        this.accumulate(reference, "AttachmentName", this.attachmentName);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("contentId", this.contentId).add("contentType", this.contentType).add("attachmentName", this.attachmentName).add("Description", this.description).toString();
    }
}
