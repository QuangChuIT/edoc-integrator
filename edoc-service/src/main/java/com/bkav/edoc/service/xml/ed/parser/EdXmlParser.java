package com.bkav.edoc.service.xml.ed.parser;

import com.bkav.edoc.service.xml.base.body.Body;
import com.bkav.edoc.service.xml.base.header.Header;
import com.bkav.edoc.service.xml.base.header.IMessageHeader;
import com.bkav.edoc.service.xml.base.parser.AbstractXmlParser;
import com.bkav.edoc.service.xml.base.parser.ParserException;
import com.bkav.edoc.service.xml.base.util.BaseXmlUtils;
import com.bkav.edoc.service.xml.ed.Ed;
import com.bkav.edoc.service.xml.ed.header.MessageHeader;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;

import java.io.*;

public class EdXmlParser extends AbstractXmlParser<Ed> {

    private static final EdXmlParser INSTANCE = new EdXmlParser();

    public EdXmlParser() {
    }

    public static EdXmlParser getInstance() {
        return INSTANCE;
    }

    public Ed parse(InputStream inputStream) throws ParserException {
        Ed ed;
        try {
            Document document = BaseXmlUtils.getDocument(inputStream);
            MessageHeader messageHeader = new MessageHeader();
            Element header = Header.getContent(document);
            Element body = Body.getContent(document);
            ed = new Ed();
            if (header != null) {
                ed = new Ed(Header.fromContent(header, messageHeader), this.parseAttachment(document));
            }
            if (body != null) {
                ed.setBody(Body.fromContent(body));
            }
            LOGGER.info("Parse document success !!!!!!!!!!");
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                LOGGER.info("Error to parse document !!!!!!!!!!!!!!");
                throw new ParserException("Error occurs during parsing", e);
            }
        }

        return ed;
    }

    public Class<? extends IMessageHeader> getMessageHeader() {
        return MessageHeader.class;
    }

    public static void main(String[] args) throws FileNotFoundException, ParserException {
        File file = new File("D:/SendDocument.000.00.00.G22.1a12bb37-8150-4662-ae4f-beeca5d8aca0.edxml");
        InputStream inputStream = new FileInputStream(file);
        Ed ed = EdXmlParser.getInstance().parse(inputStream);
        System.out.println(ed.getHeader().getMessageHeader());
    }

    private static final Logger LOGGER = Logger.getLogger(EdXmlParser.class);
}
