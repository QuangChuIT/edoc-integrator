package com.bkav.edoc.web.controller;

import com.bkav.edoc.service.database.entity.EdocAttachment;
import com.bkav.edoc.service.database.services.EdocAttachmentService;
import com.bkav.edoc.service.util.PropsUtil;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class AttachmentController {

    private final EdocAttachmentService attachmentService = new EdocAttachmentService();

    @RequestMapping(value = "/attachment/-/edoc-attachment/{attachmentId}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable Long attachmentId) {
        if (attachmentId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        EdocAttachment attachment = attachmentService.getAttachmentById(attachmentId);

        if (attachment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String fileName = attachment.getName();

        String contentType = attachment.getType();

        String specPath = attachment.getFullPath();

        String fullPath = PropsUtil.get("edxml.attachment.dir");

        String filePath = fullPath + "/attachment/" + specPath;

        File file = new File(filePath);
        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment;filename=" + fileName)
                    .contentType(MediaType.valueOf(contentType)).contentLength(file.length())
                    .body(resource);
        } catch (Exception e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/attachment/-/download/{attachmentId}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadAttachment(@PathVariable Long attachmentId) throws IOException {
        HttpHeaders responseHeader = new HttpHeaders();
        try {
            if (attachmentId == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            EdocAttachment attachment = attachmentService.getAttachmentById(attachmentId);

            if (attachment == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            String specPath = attachment.getFullPath();

            String fullPath = PropsUtil.get("edxml.attachment.dir");

            String filePath = fullPath + "/attachment/" + specPath;

            File file = new File(filePath);

            byte[] data = FileUtils.readFileToByteArray(file);

            String filename = URLEncoder.encode(attachment.getName(), StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
            // Set mimeType response
            responseHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            // Config information file response
            responseHeader.set("Content-disposition", "attachment; filename=" + filename);
            responseHeader.setContentLength(data.length);
            InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
            InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
            return new ResponseEntity<>(inputStreamResource, responseHeader, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, responseHeader, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private final static Logger logger = Logger.getLogger(AttachmentController.class);

}