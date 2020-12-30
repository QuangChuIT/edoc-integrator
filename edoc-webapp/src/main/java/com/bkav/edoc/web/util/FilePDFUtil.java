package com.bkav.edoc.web.util;

import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.web.email.WriteDocumentsToPDF;
import com.itextpdf.text.DocumentException;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class FilePDFUtil {
    private static final WriteDocumentsToPDF WRITE_DOCUMENTS_TO_PDF = new WriteDocumentsToPDF();

    public static void WriteDocumentsToPDF (List<EdocDocument> documents, OutputStream outputStream) throws IOException, DocumentException, FontFormatException {
        WRITE_DOCUMENTS_TO_PDF.WriteDocumentsToPDF(documents, outputStream);
    }
}
