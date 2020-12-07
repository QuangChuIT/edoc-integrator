package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.daoimpl.EdocDocumentTypeDaoImpl;
import com.bkav.edoc.service.database.entity.EdocDocumentType;
import com.bkav.edoc.service.memcached.MemcachedKey;
import com.bkav.edoc.service.memcached.MemcachedUtil;

import java.util.List;

public class EdocTypeService {
    private final static EdocDocumentTypeDaoImpl EDOC_TYPE_DAO = new EdocDocumentTypeDaoImpl();

    public List<EdocDocumentType> getAllTypes() {
        return EDOC_TYPE_DAO.findAll();
    }

    public EdocDocumentType findById(int documentTypeId) {
        EdocDocumentType documentType;
        String cacheKey = MemcachedKey.getKey(String.valueOf(documentTypeId), EdocDocumentType.class.getName());

        documentType = (EdocDocumentType) MemcachedUtil.getInstance().read(cacheKey);

        if (documentType == null) {
            documentType = EDOC_TYPE_DAO.findById(documentTypeId);
            if (documentType != null) {
                MemcachedUtil.getInstance().create(cacheKey, MemcachedKey.CHECK_ALLOW_TIME_LIFE, documentType);
            }
        }
        return documentType;
    }
}
