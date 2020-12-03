package com.bkav.edoc.web.vpcp;

import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.entity.EdocDocumentDetail;
import com.bkav.edoc.service.database.util.*;
import com.bkav.edoc.service.xml.base.header.Organization;
import com.bkav.edoc.service.xml.base.parser.ParserException;
import com.bkav.edoc.service.xml.base.util.UUidUtils;
import com.bkav.edoc.service.xml.ed.Ed;
import com.bkav.edoc.service.xml.ed.header.MessageHeader;
import com.bkav.edoc.service.xml.ed.parser.EdXmlParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class TestVPCP {
    public static void main(String[] args) throws IOException, ParserException {
        //parse data from edxml
        File file = new File("/home/quangcv/IdeaProjects/edoc-integrator/edoc-service/src/main/resources/edoc_new.edxml");
        InputStream inputStream = new FileInputStream(file);
        Ed ed = EdXmlParser.getInstance().parse(inputStream);
        System.out.println("Parser success document from file ");
        //Get message header
        MessageHeader messageHeader = (MessageHeader) ed.getHeader().getMessageHeader();
        //filter list organ of current organ on esb
        List<Organization> thisOrganizations = filterOrgan(messageHeader.getToes());
        messageHeader.setToes(thisOrganizations);
        // create document
        EdocDocument edocDocument = MapperUtil.modelToEdocDocument(messageHeader);
        edocDocument.setReceivedExt(true);
        edocDocument.setDocumentExtId(UUidUtils.generate());
        EdocDocumentServiceUtil.createDocument(edocDocument);
        // create document detail
        EdocDocumentDetail documentDetail = MapperUtil.modelToDocumentDetail(messageHeader);
        documentDetail.setDocument(edocDocument);
        EdocDocumentServiceUtil.createDocumentDetail(documentDetail);
        // create trace header list
    }

    private static List<Organization> filterOrgan(List<Organization> organizations) {
        String currentOrgan = com.bkav.edoc.web.util.PropsUtil.get("edoc.root.organDomain");
        return organizations.stream()
                .filter(organ -> organ.getOrganId().contains(currentOrgan))
                .collect(Collectors.toList());
    }
}
