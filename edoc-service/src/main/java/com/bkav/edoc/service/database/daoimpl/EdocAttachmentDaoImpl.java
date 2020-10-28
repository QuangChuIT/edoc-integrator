package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.EdocAttachmentDao;
import com.bkav.edoc.service.database.entity.EdocAttachment;
import com.bkav.edoc.service.database.services.EdocAttachmentService;
import com.bkav.edoc.service.database.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class EdocAttachmentDaoImpl extends RootDaoImpl<EdocAttachment, Long> implements EdocAttachmentDao {

    public EdocAttachmentDaoImpl() {
        super(EdocAttachment.class);
    }

    public List<EdocAttachment> getAttachmentsByDocumentId(long documentId) {
        List<EdocAttachment> result = null;
        Session currentSession = openCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ea FROM EdocAttachment ea where ea.document.id=:documentId");
            Query<EdocAttachment> query = currentSession.createQuery(sql.toString());
            query.setParameter("documentId", documentId);
            result = query.list();
        } catch (Exception e) {
            LOGGER.error("Error get attachments for document with id " + documentId + " cause " + e.getMessage());
        } finally {
            currentSession.close();
        }
        return result;
    }

    @Override
    public List<EdocAttachment> getAllAttachmentsList() {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<EdocAttachment> query = builder.createQuery(EdocAttachment.class);
        Root<EdocAttachment> root = query.from(EdocAttachment.class);
        query.select(root);
        Query<EdocAttachment> q = session.createQuery(query);
        List<EdocAttachment> list = q.getResultList();
        session.getTransaction().commit();
        session.close();
        return list;
    }

    //KienNDc-InsertAttachment
    @Override
    public boolean insertAttachments(EdocAttachment edocAttachment) {
        try (Session session = openCurrentSession()) {
            session.beginTransaction();
            persist(edocAttachment);
            session.flush();
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateAttachment(EdocAttachment attachment) {
        try (Session session = openCurrentSession()) {
            session.beginTransaction();
            saveOrUpdate(attachment);
            session.flush();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean deleteAttachment(EdocAttachment attachment) {
        boolean result = false;
        Session session = openCurrentSession();
        try {
            session.beginTransaction();
            delete(attachment);
            session.getTransaction().commit();
            result = true;
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                closeCurrentSession();
            }
        }
        return result;
    }

    private static final Logger LOGGER = Logger.getLogger(EdocAttachmentService.class);
}
