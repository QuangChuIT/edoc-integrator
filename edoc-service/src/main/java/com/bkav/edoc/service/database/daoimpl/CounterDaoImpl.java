package com.bkav.edoc.service.database.daoimpl;

import com.bkav.edoc.service.database.dao.CounterDao;
import com.bkav.edoc.service.database.entity.Counter;
import com.bkav.edoc.service.database.exception.NoSuchCounterException;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class CounterDaoImpl extends RootDaoImpl<Counter, Long> implements CounterDao {

    public CounterDaoImpl() {
        super(Counter.class);
    }

    @Override
    public void reset(String name, int size) throws NoSuchCounterException {
        Session session = getCurrentSession();
        try {
            session.beginTransaction();
            Counter counter = this.findByName(name);
            if (counter == null) {
                throw new NoSuchCounterException("Not found counter with name " + name);
            }
            counter.setCurrentId(size);
            this.update(counter);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw new NoSuchCounterException("Error when get reset counter " + e.getMessage());
        }
    }

    @Override
    public void rename(String oldName, String newName) throws NoSuchCounterException {
        Session currentSession = getCurrentSession();
        try {
            currentSession.beginTransaction();
            Counter counter = this.findByName(oldName);
            if (counter == null) {
                throw new NoSuchCounterException("Not found counter with name " + oldName);
            }
            counter.setName(newName);
            this.update(counter);
            currentSession.getTransaction().commit();
        } catch (Exception e) {
            throw new NoSuchCounterException("Error when rename counter " + e.getMessage());
        }
    }

    @Override
    public long increment(String name, int size) throws NoSuchCounterException {
        Session currentSession = getCurrentSession();
        long currentId;
        try {
            currentSession.beginTransaction();
            Counter counter = this.findByName(name);
            if (counter == null) {
                throw new NoSuchCounterException("Not found counter with name " + name);
            }
            long currentCounter = counter.getCurrentId();
            currentCounter = currentCounter + size;
            counter.setCurrentId(currentCounter);
            currentSession.update(counter);
            currentSession.getTransaction().commit();
            currentId = counter.getCurrentId();
        } catch (NoSuchCounterException e) {
            //try create counter
            Counter counter = new Counter(name, 1);
            currentSession.persist(counter);
            currentId = counter.getCurrentId();
            currentSession.getTransaction().commit();
        }
        return currentId;
    }

    @Override
    public Counter findByName(String name) throws NoSuchCounterException {
        Session session = getCurrentSession();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT counter FROM Counter counter where counter.name=:name");
            Query<Counter> query = session.createQuery(sql.toString(), Counter.class);
            query.setParameter("name", name);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new NoSuchCounterException(e.getMessage());
        }
    }
}
