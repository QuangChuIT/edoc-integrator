package com.bkav.edoc.service.database.services;

import com.bkav.edoc.service.database.daoimpl.MailReceiverAdminDaoImpl;
import com.bkav.edoc.service.database.entity.MailReceiverAdmin;

import java.util.List;

public class MailReceiverAdminService {
    private final MailReceiverAdminDaoImpl mailReceiverDao = new MailReceiverAdminDaoImpl();

    public List<MailReceiverAdmin> getAllMailReceiver() {
        return mailReceiverDao.getAllMailReceiver();
    }
}
