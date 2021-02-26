package com.bkav.edoc.service.database.dao;

import com.bkav.edoc.service.database.entity.EdocAttachment;

import java.util.List;

public interface EdocAttachmentDao {

    List<EdocAttachment> getAttachmentsByDocumentId(long documentId);

    List<EdocAttachment> getAllAttachmentsList();

    boolean insertAttachments(EdocAttachment edocAttachment);

    boolean checkAllowDownAttachment(String organDomain, long attachmentId);
}
