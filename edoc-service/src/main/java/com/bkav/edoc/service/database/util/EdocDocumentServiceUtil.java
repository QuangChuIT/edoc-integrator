package com.bkav.edoc.service.database.util;

import com.bkav.edoc.service.database.cache.AttachmentCacheEntry;
import com.bkav.edoc.service.database.cache.DocumentCacheEntry;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.entity.EdocDocumentDetail;
import com.bkav.edoc.service.database.entity.pagination.PaginationCriteria;
import com.bkav.edoc.service.database.services.EdocDocumentDetailService;
import com.bkav.edoc.service.database.services.EdocDocumentService;
import com.bkav.edoc.service.xml.base.attachment.Attachment;
import com.bkav.edoc.service.xml.base.header.Error;
import com.bkav.edoc.service.xml.base.header.Organization;
import com.bkav.edoc.service.xml.base.header.TraceHeaderList;
import com.bkav.edoc.service.xml.ed.header.MessageHeader;

import java.util.Date;
import java.util.List;

public class EdocDocumentServiceUtil {
    private final static EdocDocumentService DOCUMENT_SERVICE = new EdocDocumentService();
    private final static EdocDocumentDetailService EDOC_DOCUMENT_DETAIL_SERVICE = new EdocDocumentDetailService();

    public static List<DocumentCacheEntry> getDocuments(String organId, int start, int size) {
        return DOCUMENT_SERVICE.getDocuments(organId, start, size);
    }

    public static List<DocumentCacheEntry> getDocumentsFilter(PaginationCriteria paginationCriteria, String organId, String mode) {
        return DOCUMENT_SERVICE.getDocumentsFilter(paginationCriteria, organId, mode);
    }

    public static int countDocumentsFilter(PaginationCriteria paginationCriteria, String organId, String mode) {
        return DOCUMENT_SERVICE.countDocumentsFilter(paginationCriteria, organId, mode);
    }

    public static boolean deleteDocument(long documentId) {
        return DOCUMENT_SERVICE.deleteDocument(documentId);
    }

    public static DocumentCacheEntry getDocumentById(long documentId) {
        return DOCUMENT_SERVICE.getDocById(documentId);
    }

    public static DocumentCacheEntry getDocumentByCodeAndDomain(String docCode, String organDomain) {
        return DOCUMENT_SERVICE.getDocByCodeAndDomain(docCode, organDomain);
    }

    public static EdocDocument getDocument(long documentId) {
        EdocDocument document = DOCUMENT_SERVICE.getDocument(documentId);
        return DOCUMENT_SERVICE.getDocument(documentId);
    }

    public static EdocDocument createDocument(EdocDocument document) {
        return DOCUMENT_SERVICE.addDocument(document);
    }

    public static EdocDocumentDetail createDocumentDetail(EdocDocumentDetail documentDetail) {
        return EDOC_DOCUMENT_DETAIL_SERVICE.addDocumentDetail(documentDetail);
    }

    public static void addDocumentToPending(List<String> toOrgans, long documentId) {
        DOCUMENT_SERVICE.savePendingDocumentCacheFromWeb(toOrgans, documentId);
    }

    public static void addDocumentToPendingCached(List<Organization> toOrganizations, long documentId) {
        DOCUMENT_SERVICE.savePendingDocumentCache(toOrganizations, documentId);
    }

    public static List<EdocDocument> selectForDailyCounter(Date date) {
        return DOCUMENT_SERVICE.selectForDailyCounter(date);
    }

    public static void updateDocument(long documentId) {
        DOCUMENT_SERVICE.updateDocument(documentId);
    }

    public static void updateDocument(EdocDocument edocDocument) {
        DOCUMENT_SERVICE.updateDocument(edocDocument);
    }

    public static void deleteDraftDocument(long documentDraftId) {
        DOCUMENT_SERVICE.deleteDraftDocument(documentDraftId);
    }

    public static void updateDraftToPublishDocument(long documentId) {
        DOCUMENT_SERVICE.updateDraftToPublishDocument(documentId);
    }

    public static EdocDocument addDocument(MessageHeader messageHeader, TraceHeaderList traces, List<Attachment> attachments,
                                           StringBuilder outDocumentId, List<AttachmentCacheEntry> edocAttachmentCacheEntries, List<Error> errors) {
        return DOCUMENT_SERVICE.addDocument(messageHeader, traces, attachments, outDocumentId, edocAttachmentCacheEntries, errors);
    }
}
