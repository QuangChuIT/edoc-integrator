package com.bkav.edoc.sdk.edxml.entity;

import com.bkav.edoc.sdk.edxml.util.EdxmlUtils;
import com.google.common.base.MoreObjects;
import org.jdom2.Element;

public class X509Data {
    private String x509SubjectName;
    private String x509Certificate;

    public X509Data() {
    }

    public X509Data(String x509SubjectName, String x509Certificate) {
        this.x509SubjectName = x509SubjectName;
        this.x509Certificate = x509Certificate;
    }

    public String getX509SubjectName() {
        return this.x509SubjectName;
    }

    public void setX509SubjectName(String x509SubjectName) {
        this.x509SubjectName = x509SubjectName;
    }

    public String getX509Certificate() {
        return this.x509Certificate;
    }

    public void setX509Certificate(String x509Certificate) {
        this.x509Certificate = x509Certificate;
    }

    public static X509Data getData(Element elementNode) {
        return new X509Data(EdxmlUtils.getString(elementNode, "X509SubjectName"),
                EdxmlUtils.getString(elementNode, "X509Certificate"));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("X509SubjectName",
                this.x509SubjectName).add("X509Certificate", this.x509Certificate).toString();
    }

}
