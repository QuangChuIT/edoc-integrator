package com.bkav.edoc.util;

import com.bkav.edoc.service.commonutil.Checker;
import com.bkav.edoc.service.database.cache.AttachmentCacheEntry;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.util.EdocDocumentServiceUtil;
import com.bkav.edoc.service.xml.base.attachment.Attachment;
import com.bkav.edoc.service.xml.base.header.Error;
import com.bkav.edoc.service.xml.base.header.Report;
import com.bkav.edoc.service.xml.base.header.TraceHeaderList;
import com.bkav.edoc.service.xml.ed.Ed;
import com.bkav.edoc.service.xml.ed.header.MessageHeader;
import com.bkav.edoc.service.xml.ed.parser.EdXmlParser;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EdocUtil {
    private static final Checker CHECKER = new Checker();

    public static void processSendDoc(InputStream inputStream, List<Error> errorList, StringBuilder documentId) {
        try {

        } catch (Exception e) {
            LOGGER.error(e);
        }

    }


    private final static Logger LOGGER = Logger.getLogger(EdocUtil.class);
}
