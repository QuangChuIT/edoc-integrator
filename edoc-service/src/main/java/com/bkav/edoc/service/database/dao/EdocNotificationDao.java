package com.bkav.edoc.service.database.dao;

import java.sql.SQLException;
import java.util.List;

public interface EdocNotificationDao {

    List<Long> getDocumentIdsByOrganId(String organId);

    boolean checkAllowWithDocument(long documentId, String organId);

    void setNotificationTaken(long documentId, String organId) throws SQLException;
}
