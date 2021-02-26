package com.bkav.edoc.service.xml;

import com.bkav.edoc.service.xml.ed.Ed;
import com.bkav.edoc.service.xml.ed.parser.EdXmlParser;
import com.bkav.edoc.service.xml.status.header.MessageStatus;
import com.bkav.edoc.service.xml.status.parser.StatusXmlParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


public class ParseEdxml {
    public static void main(String[] args) throws Exception {
        File file = new File("D:\\IdeaProjects\\edoc-integrator\\edoc-service\\src\\main\\resources\\edoc_new.edxml");
        InputStream inputStream = new FileInputStream(file);
        Ed ed = EdXmlParser.getInstance().parse(inputStream);
        System.out.println(ed.toString());

        File file2 = new File("D:\\IdeaProjects\\edoc-integrator\\edoc-service\\src\\main\\resources\\status_processing_05.edxml");
        InputStream inputStream2 = new FileInputStream(file2);
        MessageStatus ed2 = StatusXmlParser.parse(inputStream2);
        System.out.println(ed2);
    }
}
