package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.MailReceiverAdminDao;
import com.bkav.edoc.service.database.entity.MailReceiverAdmin;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class MailReceiverAdminDaoImpl extends RootDaoImpl<MailReceiverAdmin, Long> implements MailReceiverAdminDao {

    public MailReceiverAdminDaoImpl() {
        super(MailReceiverAdmin.class);
    }

    public List<MailReceiverAdmin> getAllMailReceiver() {
        List<MailReceiverAdmin> emails = new ArrayList<>();
        Session session = openCurrentSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<MailReceiverAdmin> query = builder.createQuery(MailReceiverAdmin.class);
            Root<MailReceiverAdmin> root = query.from(MailReceiverAdmin.class);
            query.select(root);
            Query<MailReceiverAdmin> q = session.createQuery(query);
            return q.getResultList();
        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
            closeCurrentSession(session);
        }
        return emails;
    }

    private final static Logger LOGGER = Logger.getLogger(MailReceiverAdminDaoImpl.class);
}
