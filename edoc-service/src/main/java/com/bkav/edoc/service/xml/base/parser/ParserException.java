package com.bkav.edoc.service.xml.base.parser;

public class ParserException extends Exception{
    private static final long serialVersionUID = -4815601217568350998L;

    public ParserException(String error) {
        super(error);
    }

    public ParserException(String error, Throwable throwable) {
        super(error, throwable);
    }
}
