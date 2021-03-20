package com.bkav.edoc.service.database.dao;

import com.bkav.edoc.service.database.entity.EdocDocument;

import java.util.Date;
import java.util.List;

public interface EdocDocumentDao {

    EdocDocument checkExistDocument(String edXmlDocumentId);

    boolean checkExistDocument(String subject, String codeNumber, String codeNotation,
                               Date promulgationDate, String fromOrganDomain,
                               String toOrganDomain, List<String> attachmentNames);

    EdocDocument searchDocumentByOrganDomainAndCode(String fromOrganDomain, String toOrganDomain, String code);


    List<EdocDocument> getAllDocumentList();

    List<EdocDocument> getDocuments(String organId, int start, int size);

    EdocDocument addNewDocument(EdocDocument edocDocument);

    EdocDocument getDocumentByDocCode(String docCode);

    boolean removeDocument(long documentId);
}
