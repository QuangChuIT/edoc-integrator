package com.bkav.edoc.service.database.dao;

import com.bkav.edoc.service.database.entity.EdocDocument;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public interface EdocDocumentDao {

    EdocDocument checkExistDocument(String edXmlDocumentId);

    boolean checkExistDocument(String subject, String codeNumber, String codeNotation,
                               Date promulgationDate, String fromOrganDomain,
                               String toOrganDomain, List<String> attachmentNames);

    EdocDocument searchDocumentByOrganDomainAndCode(String toOrganDomain, String code);

    List<EdocDocument> findByOranDomain(String organId, String mode, int start, int size);

    List<EdocDocument> getAllDocumentList();

    List<EdocDocument> getDocuments(String organId, int start, int size);

    EdocDocument addNewDocument(EdocDocument edocDocument);

    boolean removeDocument(long documentId);
}
