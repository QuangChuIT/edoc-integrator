package com.bkav.edoc.service.xml.status.parser;

import com.bkav.edoc.service.xml.base.header.Header;
import com.bkav.edoc.service.xml.base.parser.ParserException;
import com.bkav.edoc.service.xml.base.util.BaseXmlUtils;
import com.bkav.edoc.service.xml.status.Status;
import com.bkav.edoc.service.xml.status.header.MessageStatus;
import org.jdom2.Document;
import org.jdom2.Element;

import java.io.*;

public class StatusXmlParser {
    public StatusXmlParser() {
    }

    public static Header parseHeader(InputStream inputStream) throws ParserException {
        Header header = null;
        try {
            Document document = BaseXmlUtils.getDocument(inputStream);
            MessageStatus messageStatus = new MessageStatus();
            Element headerElement = Header.getContent(document);
            if (headerElement != null) {
                header = Header.fromContent(headerElement, messageStatus);
            }
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return header;
    }

    public static Status parse(InputStream inputStream) throws ParserException {
        Status status = null;
        try {
            Document document = BaseXmlUtils.getDocument(inputStream);
            Element header = Header.getContent(document);
            if (header != null) {
                status = new Status(Header.fromContent(header, new MessageStatus()));
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
        Status status = StatusXmlParser.parse(inputStream);
        MessageStatus messageStatus = (MessageStatus) status.getHeader().getMessageHeader();
        System.out.println(messageStatus);
    }
}
