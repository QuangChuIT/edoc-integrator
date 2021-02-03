package com.bkav.edoc.web.controller;

import com.bkav.edoc.service.database.cache.AttachmentCacheEntry;
import com.bkav.edoc.service.database.entity.EdocAttachment;
import com.bkav.edoc.service.database.util.CounterServiceUtil;
import com.bkav.edoc.service.database.util.EdocAttachmentServiceUtil;
import com.bkav.edoc.service.database.util.MapperUtil;
import com.bkav.edoc.service.resource.EdXmlConstant;
import com.bkav.edoc.service.util.AttachmentGlobalUtil;
import com.bkav.edoc.web.OAuth2Constants;
import com.bkav.edoc.web.auth.CookieUtil;
import com.bkav.edoc.web.payload.ApiError;
import com.bkav.edoc.web.payload.Response;
import com.bkav.edoc.web.util.MessageSourceUtil;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.util.*;

@RestController
public class AttachmentRestController {
    public final AttachmentGlobalUtil attachmentGlobalUtil = new AttachmentGlobalUtil();

    private final MessageSourceUtil messageSourceUtil;

    public AttachmentRestController(MessageSourceUtil messageSourceUtil) {
        this.messageSourceUtil = messageSourceUtil;
    }


    /*
     * MultipartFile Upload
     */
    @RequestMapping(method = RequestMethod.POST, value = "/attachment/-/document/upload")
    public ResponseEntity<?> uploadMultipartFile(@RequestParam("uploadFile") List<MultipartFile> files, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();
        try {
            String fromOrgan = CookieUtil.getValue(request, OAuth2Constants.ORGANIZATION);
            List<AttachmentCacheEntry> attachmentCacheEntries = new ArrayList<>();
            String SEPARATOR = EdXmlConstant.SEPARATOR;
            String rootPath = attachmentGlobalUtil.getAttachmentPath();
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                String extension = AttachmentGlobalUtil.getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
                String contentType = AttachmentGlobalUtil.getContentType(extension);
                EdocAttachment edocAttachment = new EdocAttachment();
                edocAttachment.setOrganDomain(fromOrgan);
                edocAttachment.setName(file.getOriginalFilename());
                edocAttachment.setType(contentType);
                edocAttachment.setSize(String.valueOf(file.getSize()));
                Calendar cal = Calendar.getInstance();
                long counter = CounterServiceUtil.increment(EdocAttachment.class.getName());
                String dataPath = fromOrgan + SEPARATOR +
                        cal.get(Calendar.YEAR) + SEPARATOR +
                        (cal.get(Calendar.MONTH) + 1) + SEPARATOR +
                        cal.get(Calendar.DAY_OF_MONTH) + SEPARATOR +
                        counter + "_" + (i + 1);
                String specPath = rootPath +
                        (rootPath.endsWith(SEPARATOR) ? "" : SEPARATOR) +
                        dataPath;
                InputStream fileInputStream = file.getInputStream();
                attachmentGlobalUtil.saveFile(specPath, fileInputStream);
                fileInputStream.close();
                edocAttachment.setFullPath(dataPath);
                edocAttachment.setCreateDate(new Date());
                edocAttachment = EdocAttachmentServiceUtil.addAttachment(edocAttachment);
                AttachmentCacheEntry attachmentCacheEntry = MapperUtil.modelToAttachmentCache(edocAttachment);
                attachmentCacheEntries.add(attachmentCacheEntry);
            }
            return new ResponseEntity<>(attachmentCacheEntries, HttpStatus.OK);
        } catch (Exception e) {
            String uploadAttachmentsError = messageSourceUtil.getMessage("edoc.message.attachment.upload.error", null);
            LOGGER.error(uploadAttachmentsError + e.getMessage());
            errors.add(uploadAttachmentsError);
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, uploadAttachmentsError, errors);
            return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/attachment/-/delete/{edocAttachmentId}")
    public ResponseEntity<Response> deleteAttachment(@PathVariable("edocAttachmentId") String edocAttachmentId) {
        List<String> errors = new ArrayList<>();
        try {
            long attachmentId = Long.parseLong(edocAttachmentId);
            EdocAttachment edocAttachment = EdocAttachmentServiceUtil.findById(attachmentId);
            if (edocAttachment == null) {
                String notFoundMessage = messageSourceUtil.getMessage("edoc.message.attachment.not.found",
                        new Object[]{String.valueOf(attachmentId)});
                errors.add(notFoundMessage);
                Response response = new Response(400, errors, notFoundMessage);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            String filePath = edocAttachment.getFullPath();
            String SEPARATOR = EdXmlConstant.SEPARATOR;
            String rootPath = attachmentGlobalUtil.getAttachmentPath();
            String specPath = rootPath +
                    (rootPath.endsWith(SEPARATOR) ? "" : SEPARATOR) +
                    filePath;
            File file = new File(specPath);
            if (file.exists()) {
                if (!file.delete()) {
                    String deleteFailMessage = messageSourceUtil.getMessage("edoc.message.attachment.delete.fail", new Object[]{String.valueOf(attachmentId)});
                    errors.add(deleteFailMessage);
                    Response response = new Response(500, errors, deleteFailMessage);
                    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            boolean delete = EdocAttachmentServiceUtil.deleteAttachment(edocAttachment);
            if (!delete) {
                String deleteAttachmentDbFail = messageSourceUtil.getMessage("edoc.message.attachment.delete.database.fail", new Object[]{String.valueOf(attachmentId)});
                errors.add(deleteAttachmentDbFail);
                Response response = new Response(500, errors, deleteAttachmentDbFail);
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            String message = messageSourceUtil.getMessage("edoc.message.attachment.delete.success", null);
            Response response = new Response(200, errors, message);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            String deleteAttachmentError = messageSourceUtil.getMessage("edoc.message.attachment.delete.error", new Object[]{String.valueOf(edocAttachmentId)});
            LOGGER.error(deleteAttachmentError + " cause " + e.getMessage());
            errors.add(deleteAttachmentError + " cause " + e.getMessage());
            Response response = new Response(500, errors, deleteAttachmentError);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private final static Logger LOGGER = Logger.getLogger(AttachmentRestController.class);
}
