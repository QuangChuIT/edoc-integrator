package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.EdocDynamicContactDao;
import com.bkav.edoc.service.database.entity.EdocDynamicContact;
import com.bkav.edoc.service.kernel.string.StringPool;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Arrays;
import java.util.List;

public class EdocDynamicContactDaoImpl extends RootDaoImpl<EdocDynamicContact, Long> implements EdocDynamicContactDao {

    public EdocDynamicContactDaoImpl() {
        super(EdocDynamicContact.class);
    }

    public EdocDynamicContact findByDomain(String domain) {
        Session currentSession = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT edc FROM EdocDynamicContact edc where edc.domain=:domain");
            Query<EdocDynamicContact> query = currentSession.createQuery(sql.toString());
            query.setParameter("domain", domain);
            List<EdocDynamicContact> result = query.list();
            if (result != null && result.size() > 0) {
                return result.get(0);
            }
            LOGGER.warn("Not found dynamic contact for organ domain " + domain);
            return null;
        } catch (Exception e) {
            LOGGER.error("Error get dynamic contact from organ domain " + domain + " cause " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<EdocDynamicContact> getDynamicContactsByDomainFilter(String domain) {
        Session currentSession = getCurrentSession();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT edc FROM EdocDynamicContact edc where edc.domain like :domain");
        Query<EdocDynamicContact> query = currentSession.createQuery(sql.toString());
        query.setParameter("domain", StringPool.PERCENT + domain + StringPool.PERCENT);
        return query.list();
    }

    @Override
    public Long countOrgan(String organDomain) {
        Session currentSession = getCurrentSession();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(*) FROM EdocDynamicContact edc where edc.domain like :domain");
        Query<Long> query = currentSession.createQuery(sql.toString());
        query.setParameter("domain", StringPool.PERCENT + organDomain + StringPool.PERCENT);
        return query.uniqueResult();
    }

    @Override
    public boolean checkPermission(String organId, String token) {
        boolean result = false;
        Session session = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT edc from EdocDynamicContact edc where edc.domain=:domain and token=:token and edc.status=true");
            Query<EdocDynamicContact> query = session.createQuery(sql.toString());
            query.setParameter("domain", organId);
            query.setParameter("token", token);
            List<EdocDynamicContact> dynamicContacts = query.list();
            if (dynamicContacts != null && dynamicContacts.size() > 0) {
                result = true;
            }
            LOGGER.info("Check permission success for organ " + organId);
        } catch (Exception e) {
            LOGGER.error("Error when check permission for organId " + organId + " cause " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return result;
    }

    @Override
    public void updateContact(EdocDynamicContact edocDynamicContact) {
        Session session = getCurrentSession();
        try {
            session.beginTransaction();
            session.saveOrUpdate(edocDynamicContact);
            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error(e);
            session.getTransaction().rollback();
        }
    }

    @Override
    public void createContact(EdocDynamicContact contact) {
        Session session = getCurrentSession();
        try {
            session.beginTransaction();
            session.save(contact);
            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error(e);
            session.getTransaction().rollback();
        }
    }

    private static final Logger LOGGER = Logger.getLogger(EdocDynamicContactDaoImpl.class);
}
