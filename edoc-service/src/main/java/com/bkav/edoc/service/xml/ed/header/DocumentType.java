package com.bkav.edoc.service.xml.ed.header;

import com.bkav.edoc.service.xml.base.BaseElement;
import com.bkav.edoc.service.xml.base.util.BaseXmlUtils;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.jdom2.Element;

public class DocumentType extends BaseElement {
    private int type;
    private int typeDetail;
    private String typeName;

    public DocumentType() {
    }

    public DocumentType(int type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    public DocumentType(int type, int typeDetail, String typeName) {
        this.type = type;
        this.typeDetail = typeDetail;
        this.typeName = typeName;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTypeDetail() {
        return this.typeDetail;
    }

    public void setTypeDetail(int typeDetail) {
        this.typeDetail = typeDetail;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public static DocumentType fromContent(Element element) {
        return new DocumentType(BaseXmlUtils.getInt(element, "Type", 2),
                BaseXmlUtils.getInt(element, "TypeDetail", -1),
                BaseXmlUtils.getString(element, "TypeName"));
    }

    public void accumulate(Element element) {
        Element documentType = this.accumulate(element, "DocumentType");
        this.accumulate(documentType, "Type", this.type);
        this.accumulate(documentType, "TypeDetail", this.typeDetail);
        this.accumulate(documentType, "TypeName", this.typeName);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass()).add("Type",
                this.type).add("TypeDetail",
                this.typeDetail).add("TypeName", this.typeName).toString();
    }
}
