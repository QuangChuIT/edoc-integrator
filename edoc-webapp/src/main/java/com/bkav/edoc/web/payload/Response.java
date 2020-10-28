package com.bkav.edoc.web.payload;

import java.io.Serializable;
import java.util.List;

public class Response implements Serializable {
    private int code;
    private List<String> errors;
    private String message;

    public Response(int code) {
        this.code = code;
    }

    public Response(int code, List<String> errors, String message) {
        this.code = code;
        this.errors = errors;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
