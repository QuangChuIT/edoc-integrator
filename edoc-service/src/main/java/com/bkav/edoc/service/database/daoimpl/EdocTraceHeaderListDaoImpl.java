package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.EdocTraceHeaderListDao;
import com.bkav.edoc.service.database.entity.EdocTraceHeaderList;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Arrays;
import java.util.List;

public class EdocTraceHeaderListDaoImpl extends RootDaoImpl<EdocTraceHeaderList, Long> implements EdocTraceHeaderListDao {

    public EdocTraceHeaderListDaoImpl() {
        super(EdocTraceHeaderList.class);
    }

    public EdocTraceHeaderList getTraceHeaderListByDocId(long documentId) {
        Session currentSession = openCurrentSession();
        EdocTraceHeaderList edocTraceHeaderList = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT eth FROM EdocTraceHeaderList eth where eth.document.id=:documentId");
            Query<EdocTraceHeaderList> query = currentSession.createQuery(sql.toString(), EdocTraceHeaderList.class);
            query.setParameter("documentId", documentId);
            List<EdocTraceHeaderList> result = query.list();
            if (result != null && result.size() > 0) {
                edocTraceHeaderList = result.get(0);
                LOGGER.info("Found trace header list with document id " + documentId + " in database !!!!!!!!!!!!!!!!!!!!");
            } else {
                LOGGER.error("Not found trace header list with document id " + documentId + " in database !!!!!!!!!!!!!!!!!!!!");
            }
        } catch (Exception e) {
            LOGGER.error("Error get trace header list with document id " + documentId + " cause " + Arrays.toString(e.getStackTrace()));
        } finally {
            this.closeCurrentSession(currentSession);
        }
        return edocTraceHeaderList;
    }

    private final static Logger LOGGER = Logger.getLogger(EdocTraceHeaderListDaoImpl.class);
}
