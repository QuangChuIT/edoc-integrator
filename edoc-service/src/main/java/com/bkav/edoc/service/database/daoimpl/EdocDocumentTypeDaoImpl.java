package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.EdocDocumentTypeDao;
import com.bkav.edoc.service.database.entity.EdocDocumentType;

public class EdocDocumentTypeDaoImpl extends RootDaoImpl<EdocDocumentType, Integer> implements EdocDocumentTypeDao {
    public EdocDocumentTypeDaoImpl() {
        super(EdocDocumentType.class);
    }
}
