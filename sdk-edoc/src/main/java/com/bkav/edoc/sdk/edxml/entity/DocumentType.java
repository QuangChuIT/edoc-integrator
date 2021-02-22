package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;

public class DocumentType {
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


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass()).add("Type",
                this.type).add("TypeDetail",
                this.typeDetail).add("TypeName", this.typeName).toString();
    }

}
