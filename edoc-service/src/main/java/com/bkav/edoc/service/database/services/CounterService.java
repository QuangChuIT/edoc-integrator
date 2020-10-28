package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.daoimpl.CounterDaoImpl;
import com.bkav.edoc.service.database.exception.NoSuchCounterException;

public class CounterService {
    private final CounterDaoImpl counterDao = new CounterDaoImpl();

    public long increment() throws NoSuchCounterException {
        counterDao.openCurrentSession();
        long counter = counterDao.increment(_SYS_COUNTER_NAME, _SYS_COUNTER_INCREMENT);
        counterDao.closeCurrentSession();
        return counter;
    }

    public long increment(String name) throws NoSuchCounterException {
        counterDao.openCurrentSession();
        long counter = counterDao.increment(name, _SYS_COUNTER_INCREMENT);
        counterDao.closeCurrentSession();
        return counter;
    }

    public long increment(String name, int size) throws NoSuchCounterException {
        counterDao.openCurrentSession();
        long counter = counterDao.increment(name, size);
        counterDao.closeCurrentSession();
        return counter;
    }

    public void rename(String oldName, String newName)
            throws NoSuchCounterException {
        counterDao.openCurrentSession();
        counterDao.rename(oldName, newName);
        counterDao.closeCurrentSession();

    }

    public void reset(String name) throws NoSuchCounterException {
        counterDao.openCurrentSession();
        counterDao.reset(name, 0);
        counterDao.closeCurrentSession();

    }

    public void reset(String name, int size) throws NoSuchCounterException {
        counterDao.openCurrentSession();
        counterDao.reset(name, size);
        counterDao.closeCurrentSession();
    }

    private static final String _SYS_COUNTER_NAME = "edoc.service.counter";
    private static final int _SYS_COUNTER_INCREMENT = 1;
}
