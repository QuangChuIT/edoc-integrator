package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.EdocDocumentDetailDao;
import com.bkav.edoc.service.database.entity.EdocDocumentDetail;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class EdocDocumentDetailDaoImpl extends RootDaoImpl<EdocDocumentDetail, Long> implements EdocDocumentDetailDao {

    public EdocDocumentDetailDaoImpl() {
        super(EdocDocumentDetail.class);
    }


    public EdocDocumentDetail getDocumentDetail(long docId) {
        Session currentSession = openCurrentSession();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT dd.documentId, dd.content, dd.signerFullName, dd.promulgationAmount, " +
                "dd.pageAmount, dd.toPlaces FROM EdocDocumentDetail dd WHERE dd.documentId:=documentId");
        Query<EdocDocumentDetail> query = currentSession.createQuery(sql.toString(), EdocDocumentDetail.class);
        query.setParameter("documentId", docId);
        List<EdocDocumentDetail> result = query.list();
        closeCurrentSession(currentSession);
        if (result != null && result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public List<EdocDocumentDetail> getAttachmentsByDocumentId(long documentId) {
        return null;
    }
}
