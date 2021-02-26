package com.bkav.edoc.service.xml.ed;

import com.bkav.edoc.service.xml.base.Base;
import com.bkav.edoc.service.xml.base.attachment.Attachment;
import com.bkav.edoc.service.xml.base.header.Header;

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
