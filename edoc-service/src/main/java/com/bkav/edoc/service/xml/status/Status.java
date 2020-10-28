package com.bkav.edoc.service.xml.status;

import com.bkav.edoc.service.xml.base.header.Header;

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
