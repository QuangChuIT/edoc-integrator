package com.bkav.edoc.edxml.ed;

import com.bkav.edoc.edxml.base.Base;
import com.bkav.edoc.edxml.base.attachment.Attachment;
import com.bkav.edoc.edxml.base.header.Header;

import java.util.List;

public class Ed extends Base {
    public Ed() {

    }

    public Ed(Header header, List<Attachment> attachments) {
        super(header, attachments);
    }

    public Ed(Header header) {
        super(header);
    }
}
