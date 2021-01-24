package com.bkav.edoc.edxml.status;

import com.bkav.edoc.edxml.base.header.Header;

public class Status {
    private Header header;

    public Status() {
    }

    public Status(Header header) {
        this.header = header;
    }

    public Header getHeader() {
        return this.header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }
}
