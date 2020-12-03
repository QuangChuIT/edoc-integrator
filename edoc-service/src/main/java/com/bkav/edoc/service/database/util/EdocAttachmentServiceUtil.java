package com.bkav.edoc.service.database.util;

import com.bkav.edoc.service.database.cache.AttachmentCacheEntry;
import com.bkav.edoc.service.database.entity.EdocAttachment;
import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.services.EdocAttachmentService;
import com.bkav.edoc.service.xml.base.attachment.Attachment;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class EdocAttachmentServiceUtil {
    private static final EdocAttachmentService attachmentService = new EdocAttachmentService();

    public static EdocAttachment addAttachment(EdocAttachment edocAttachment) {
        return attachmentService.addAttachment(edocAttachment);
    }

    public static void updateAttachment(EdocAttachment attachment) {
        attachmentService.updateAttachment(attachment);
    }

    public static EdocAttachment findById(Long attachmentId) {
        return attachmentService.getAttachmentById(attachmentId);
    }

    public static boolean deleteAttachment(EdocAttachment attachment) {
        return attachmentService.deleteAttachment(attachment);
    }
}
