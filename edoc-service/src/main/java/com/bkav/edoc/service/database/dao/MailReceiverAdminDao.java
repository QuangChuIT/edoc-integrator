package com.bkav.edoc.service.database.dao;

import com.bkav.edoc.service.database.entity.MailReceiverAdmin;

import java.util.List;

public interface MailReceiverAdminDao {
    List<MailReceiverAdmin> getAllMailReceiver();
}
