package com.bkav.edoc.service.database.exception;

public class NoSuchCounterException extends RuntimeException {
    public NoSuchCounterException() {
        super();
    }

    public NoSuchCounterException(String message) {
        super(message);
    }

    public NoSuchCounterException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchCounterException(Throwable cause) {
        super(cause);
    }

    protected NoSuchCounterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
