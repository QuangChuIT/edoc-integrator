package com.bkav.edoc.service.xml.status.parser;

import com.bkav.edoc.service.xml.base.parser.ParserException;
import com.bkav.edoc.service.xml.base.util.BaseXmlUtils;
import com.bkav.edoc.service.xml.status.header.MessageStatus;
import org.jdom2.Document;
import org.jdom2.Element;

import java.io.*;

public class StatusXmlParser {
    public StatusXmlParser() {
    }

    public static MessageStatus parse(InputStream inputStream) throws ParserException {
        MessageStatus status = null;
        try {
            Document document = BaseXmlUtils.getDocument(inputStream);
            Element statusElement = MessageStatus.getContent(document);
            if (statusElement != null) {
                status = MessageStatus.getData(statusElement);
            }
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return status;
    }

    public static void main(String[] args) throws FileNotFoundException, ParserException {
        File file = new File("D:\\IdeaProjects\\edoc-integrator\\edoc-service\\src\\main\\resources\\status_processing_05.edxml");
        InputStream inputStream = new FileInputStream(file);
        MessageStatus status = StatusXmlParser.parse(inputStream);
        System.out.println(status);
    }
}
