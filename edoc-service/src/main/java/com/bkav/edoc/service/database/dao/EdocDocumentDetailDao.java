package com.bkav.edoc.service.database.dao;

import com.bkav.edoc.service.database.entity.EdocDocumentDetail;

import java.util.List;


public interface EdocDocumentDetailDao {


    List<EdocDocumentDetail> getAttachmentsByDocumentId(long documentId);

    EdocDocumentDetail getDocumentDetail(long docId);
}
