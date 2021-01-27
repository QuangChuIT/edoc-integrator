package com.bkav.edoc.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Controller
@RequestMapping("/EdocService")
public class EdocController {
    private static final Logger LOGGER = Logger
            .getLogger(EdocController.class);

    /**
     * Upload single file using Spring Controller
     */
    @RequestMapping(value = "/sendEdoc", method = RequestMethod.POST)
    @ResponseBody
    public String uploadFileHandler(HttpServletRequest request,
                                    @RequestParam MultipartFile file_doc) {
        System.out.println("--------------------------------- ok -----------------------------");
        if (!file_doc.isEmpty()) {
            try {
                byte[] bytes = file_doc.getBytes();
                // Creating the directory to store file
                String rootPath = System.getProperty("user.dir");
                java.io.File dir = new java.io.File(rootPath + java.io.File.separator + "tmpFiles");

                if (!dir.exists())
                    dir.mkdirs();
                // Create the file on server
                File serverFile = new File(dir.getAbsolutePath()
                        + File.separator + "test.edxml");

                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));

                stream.write(bytes);
                stream.close();
                LOGGER.info("Server File Location="
                        + serverFile.getAbsolutePath());
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
        return "ok";
    }
}
