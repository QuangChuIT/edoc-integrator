package com.bkav.edoc.adapter.entity;

import java.io.Serializable;
import java.util.List;

public class BaseResp implements Serializable {
    private String status;
    private String code;
    private List<Error> errors;

    public BaseResp(String status, String code, String description, List<Error> errors) {
        this.status = status;
        this.code = code;
        this.errors = errors;
    }

    public BaseResp() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "BaseResp{" +
                "status='" + status + '\'' +
                ", code='" + code + '\'' +
                ", errors=" + errors +
                '}';
    }
}
