package com.bkav.edoc.edxml.base.util;

import com.bkav.edoc.edxml.base.Content;
import com.bkav.edoc.edxml.base.io.Sha256CalculatingInputStream;
import com.bkav.edoc.edxml.base.io.Sha256CalculatingOutputStream;
import com.bkav.edoc.edxml.base.parser.ParserException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.DOMBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.List;
import java.util.UUID;

public class BaseXmlUtils {
    public BaseXmlUtils() {
    }

    public static String getString(Element element, String elementName) {
        if (element != null) {
            List<Element> childrenElements = element.getChildren();
            if (childrenElements != null && childrenElements.size() > 0) {
                for (Element children : childrenElements) {
                    if (elementName.equals(children.getName())) {
                        return children.getText();
                    }
                }
            }
        }
        return null;
    }

    public static int getInt(Element element, String elementName, int defaultValue) {
        try {
            String str = getString(element, elementName);
            return str == null ? defaultValue : Integer.parseInt(str);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static boolean getBoolean(Element element, String elementName, boolean defaultValue) {
        try {
            String str = getString(element, elementName);
            return str == null ? defaultValue : Boolean.parseBoolean(str);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static void deleteFolder(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File localFile : files) {
                    deleteFolder(localFile);
                }
            }

        }
        file.delete();
    }

    public static String join(List<String> list) {
        if (list != null && !list.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String str : list) {
                stringBuilder.append(str);
                stringBuilder.append(";");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            return stringBuilder.toString();
        } else {
            return "";
        }
    }

    public static String getSha256Content(File file) throws IOException {
        Sha256CalculatingInputStream sha256CalculatingInputStream = new Sha256CalculatingInputStream(new FileInputStream(file));

        sha256CalculatingInputStream.close();
        return sha256CalculatingInputStream.getHashCode();
    }

    public static String resolveContentId(String contentId) {
        if (contentId.startsWith("cid:")) {
            contentId = contentId.substring(4);
        }

        return contentId;
    }

    public static Content buildContent(Document document, String fileName, String contentId, String path) throws IOException {
        File file = new File(path, fileName + "." + UUID.randomUUID().toString() + "." + contentId);
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        Sha256CalculatingOutputStream sha256CalculatingOutputStream = new Sha256CalculatingOutputStream(new FileOutputStream(file));
        xmlOutputter.output(document, sha256CalculatingOutputStream);
        sha256CalculatingOutputStream.close();
        String str = sha256CalculatingOutputStream.getHashCode();
        return new Content(file, str);
    }

    public static Document getDocument(InputStream inputStream) throws ParserException {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
            org.w3c.dom.Document document = documentBuilder.parse(inputStream);
            DOMBuilder domBuilder = new DOMBuilder();
            return domBuilder.build(document);
        } catch (Exception e) {
            throw new ParserException("Error occurs during parsing", e);
        }
    }
}
