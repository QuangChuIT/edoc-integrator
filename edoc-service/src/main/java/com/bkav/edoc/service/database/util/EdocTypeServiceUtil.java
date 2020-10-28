package com.bkav.edoc.service.database.util;

import com.bkav.edoc.service.database.entity.EdocDocumentType;
import com.bkav.edoc.service.database.services.EdocTypeService;

import java.util.List;

public class EdocTypeServiceUtil {
    private final static EdocTypeService EDOC_TYPE_DAO = new EdocTypeService();

    public static List<EdocDocumentType> getTypes() {
        return EDOC_TYPE_DAO.getAllTypes();
    }

    public static EdocDocumentType findById(int documentTypeId) {
        return EDOC_TYPE_DAO.findById(documentTypeId);
    }
}
