package com.bkav.edoc.sdk.edxml.entity;

import com.google.common.base.MoreObjects;

public class SignerInfo {
    private String competence;
    private String position;
    private String fullName;

    public SignerInfo() {
    }

    public SignerInfo(String competence, String position, String fullName) {
        this.competence = competence;
        this.position = position;
        this.fullName = fullName;
    }

    public String getCompetence() {
        return competence;
    }

    public void setCompetence(String competence) {
        this.competence = competence;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(super.getClass()).add("Competence", this.competence)
                .add("Position", this.position).add("FullName", this.fullName).toString();
    }
}
