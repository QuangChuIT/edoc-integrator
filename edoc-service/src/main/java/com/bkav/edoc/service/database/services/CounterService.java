package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.daoimpl.CounterDaoImpl;
import com.bkav.edoc.service.database.exception.NoSuchCounterException;

public class CounterService {
    private final CounterDaoImpl counterDao = new CounterDaoImpl();

    public long increment() throws NoSuchCounterException {
        return counterDao.increment(_SYS_COUNTER_NAME, _SYS_COUNTER_INCREMENT);
    }

    public long increment(String name) throws NoSuchCounterException {
        return counterDao.increment(name, _SYS_COUNTER_INCREMENT);
    }

    public long increment(String name, int size) throws NoSuchCounterException {
        return counterDao.increment(name, size);
    }

    public void rename(String oldName, String newName)
            throws NoSuchCounterException {
        counterDao.rename(oldName, newName);

    }

    public void reset(String name) throws NoSuchCounterException {
        counterDao.reset(name, 0);
    }

    public void reset(String name, int size) throws NoSuchCounterException {
        counterDao.reset(name, size);
    }

    private static final String _SYS_COUNTER_NAME = "edoc.service.counter";
    private static final int _SYS_COUNTER_INCREMENT = 1;
}
