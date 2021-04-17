package com.bkav.edoc.web.vpcp;

import com.bkav.edoc.service.database.cache.AttachmentCacheEntry;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.database.util.EdocDocumentServiceUtil;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.bkav.edoc.service.xml.base.attachment.Attachment;
import com.bkav.edoc.service.xml.base.header.Error;
import com.bkav.edoc.service.xml.base.header.Organization;
import com.bkav.edoc.service.xml.base.header.TraceHeaderList;
import com.bkav.edoc.service.xml.base.parser.ParserException;
import com.bkav.edoc.service.xml.ed.Ed;
import com.bkav.edoc.service.xml.ed.header.MessageHeader;
import com.bkav.edoc.service.xml.ed.parser.EdXmlParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainChecker {
    public static void main(String[] args) throws FileNotFoundException, ParserException {
        String filePath = "D:\\1b490110-3f40-48a0-a91d-dd807a135613.edxml";
        EdocDocument document = null;
        // TODO insert to database
        //parse data from edxml
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        Ed ed = EdXmlParser.getInstance().parse(inputStream);
        System.out.println("Parser success document from file " + filePath);
        //Get message header
        MessageHeader messageHeader = (MessageHeader) ed.getHeader().getMessageHeader();
        // create trace header list
        //Get trace header list
        TraceHeaderList traceHeaderList = ed.getHeader().getTraceHeaderList();
        //Get attachment
        List<Attachment> attachments = ed.getAttachments();
        //filter list organ of current organ on esb
        List<Organization> thisOrganizations = filterOrgan(messageHeader.getToes());
        messageHeader.setToes(thisOrganizations);
        StringBuilder documentEsbId = new StringBuilder();
        List<Error> errors = new ArrayList<>();
        List<AttachmentCacheEntry> attachmentCacheEntries = new ArrayList<>();
        // only check exist with new document
        document = EdocDocumentServiceUtil.addDocument(messageHeader,
                traceHeaderList, attachments, documentEsbId, attachmentCacheEntries, errors);
        System.out.println(document);

    }

    private static List<Organization> filterOrgan(List<Organization> organizations) {
        List<String> organs = new ArrayList<>();
        List<Organization> result = new ArrayList<>();
        organizations.forEach(o -> organs.add(o.getOrganId()));
        List<EdocDynamicContact> dynamicContacts = EdocDynamicContactServiceUtil.getContactsByMultipleDomains(organs);
        if (dynamicContacts.size() > 0) {
            List<EdocDynamicContact> agencies = dynamicContacts.stream().filter(EdocDynamicContact::getAgency).collect(Collectors.toList());
            if (agencies.size() > 0) {
                List<String> organDomains = agencies.stream().map(EdocDynamicContact::getDomain).collect(Collectors.toList());
                for (Organization domain : organizations) {
                    if (organDomains.contains(domain.getOrganId())) {
                        result.add(domain);
                    }
                }
            }
        }
        return result;
    }
}
