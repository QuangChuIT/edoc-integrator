package com.bkav.edoc.edxml.base.header;

import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class BaseMessageHeader extends Header implements IMessageHeader {

    private Organization from;
    private List<Organization> toes;
    private String subject;
    private List<ResponseFor> responseFors;

    public BaseMessageHeader() {
    }

    public Organization getFrom() {
        return from;
    }

    public void setFrom(Organization from) {
        this.from = from;
    }

    public List<Organization> getToes() {
        return toes;
    }

    public void setToes(List<Organization> toes) {
        this.toes = toes;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<ResponseFor> getResponseFors() {
        return responseFors;
    }

    public void setResponseFors(List<ResponseFor> responseFors) {
        this.responseFors = responseFors;
    }

    public void addTo(Organization organization) {
        if (this.toes == null) {
            this.toes = new ArrayList<>();
        }

        this.toes.add(organization);
    }

    public BaseMessageHeader fromContent(Element paramElement) {
        BaseMessageHeader baseMessageHeader = new BaseMessageHeader();
        List<Element> elementChildren = paramElement.getChildren();
        if (elementChildren != null && elementChildren.size() != 0) {
            for (Element children : elementChildren) {
                if ("From".equals(children.getName())) {
                    baseMessageHeader.setFrom(Organization.fromContent(children));
                }

                if ("To".equals(children.getName())) {
                    baseMessageHeader.addTo(Organization.fromContent(children));
                }

                if ("Subject".equals(children.getName())) {
                    baseMessageHeader.setSubject(children.getText());
                }
            }
        }
        return baseMessageHeader;
    }

    public void accumulate(Element element) {
        Element messageHeader = this.accumulate(element, "MessageHeader");
        this.accumulate(messageHeader, this.from, "From");
        for (Organization localOrganization : this.toes) {
            this.accumulate(messageHeader, localOrganization, "To");
        }
        this.accumulate(messageHeader, "Subject", this.subject);
    }

    @Override
    public String toString() {
        return "BaseMessageHeader{" +
                "from=" + from +
                ", toes=" + toes +
                ", subject='" + subject + '\'' +
                ", responseFors=" + responseFors +
                '}';
    }
}
