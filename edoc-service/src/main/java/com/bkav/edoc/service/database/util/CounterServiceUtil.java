package com.bkav.edoc.service.database.util;

import com.bkav.edoc.service.database.entity.Counter;
import com.bkav.edoc.service.database.entity.EdocAttachment;
import com.bkav.edoc.service.database.exception.NoSuchCounterException;
import com.bkav.edoc.service.database.services.CounterService;

public class CounterServiceUtil {
    private final static CounterService counterService = new CounterService();

    public static long increment() throws NoSuchCounterException {
        return counterService.increment();
    }

    public static long increment(String name) throws NoSuchCounterException {
        return counterService.increment(name);
    }

    public static long increment(String name, int size) throws NoSuchCounterException {
        return counterService.increment(name, size);
    }

    public static void rename(String oldName, String newName)
            throws NoSuchCounterException {
        counterService.rename(oldName, newName);
    }

    public static void reset(String name) throws NoSuchCounterException {
        counterService.reset(name);
    }

    public static void reset(String name, int size) throws NoSuchCounterException {
        counterService.reset(name, size);
    }

    public static void main(String[] args) {
        long counter = CounterServiceUtil.increment(EdocAttachment.class.getName());
        System.out.println(counter);

    }
}
