package com.bkav.edoc.service.database.dao;

import com.bkav.edoc.service.database.entity.EdocDocument;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface EdocNotificationDao {

    List<Long> getDocumentIdsByOrganId(String organId);

    boolean checkAllowWithDocument(long documentId, String organId);

    void setNotificationTaken(long documentId, String organId) throws SQLException;

    List<String> getReceiverIdNotTaken(Date fromDate, Date toDate);

    List<EdocDocument> getDocumentNotTakenByReceiverId(String receiverId);
}
