package com.bkav.edoc.service.database.dao;

import com.bkav.edoc.service.database.entity.Counter;
import com.bkav.edoc.service.database.exception.NoSuchCounterException;

public interface CounterDao {

    void reset(String name, int size) throws NoSuchCounterException;

    void rename(String oldName, String newName) throws NoSuchCounterException;

    long increment(String name, int size) throws NoSuchCounterException;

    Counter findByName(String name) throws NoSuchCounterException;
}
