package com.bkav.edoc.web.email;

import com.bkav.edoc.service.database.entity.EdocDocument;
import com.bkav.edoc.service.database.util.EdocDynamicContactServiceUtil;
import com.bkav.edoc.service.xml.base.util.DateUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class WriteDocumentsToPDF {
    private static String fontFileName = "/fonts/vuArial.ttf";

    public void WriteDocumentsToPDF(List<EdocDocument> documents, OutputStream outputStream, String organName) throws DocumentException, IOException {
        Document document = new Document();
        int stt = 1;
        PdfWriter.getInstance(document, outputStream);
        URL path = WriteDocumentsToPDF.class.getResource(fontFileName);
        BaseFont bf = BaseFont.createFont(path.getPath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(bf,16);
        document.open();
        Paragraph preface = new Paragraph();
        preface.add(new Paragraph("Danh sách các văn bản chưa được nhận về của đơn vị " + organName + " tới ngày " +
                DateUtils.format(new Date(), DateUtils.VN_DATE_FORMAT), font));
        addEmptyLine(preface, 3);
        document.add(preface);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{0.5f, 3.5f, 1.5f, 1.25f, 1.25f, 1.25f});
        addTableHeader(table, path);
        for (EdocDocument doc: documents) {
            addRows(table, doc, stt, path);
            stt++;
        }
        document.add(table);
        document.close();
    }

    private void addTableHeader(PdfPTable table, URL path) throws IOException, DocumentException {
        BaseFont bf = BaseFont.createFont(String.valueOf(path), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(bf,12);

        Stream.of("STT", "Trích yếu", "Đơn vị gửi", "Ký hiệu", "Ngày phát hành", "Ngày đồng bộ lên trục")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setPhrase(new Phrase(columnTitle, font));
                    table.addCell(header);
                });

        table.setHeaderRows(1);
    }

    private void addRows(PdfPTable table, EdocDocument document, int stt, URL path) throws IOException, DocumentException {
        String fromOrgan = EdocDynamicContactServiceUtil.getNameByOrganId(document.getFromOrganDomain());
        BaseFont bf = BaseFont.createFont(String.valueOf(path), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(bf,12);
        Stream.of(String.valueOf(stt), document.getSubject(), fromOrgan, document.getDocCode(),
                DateUtils.format(document.getPromulgationDate(), DateUtils.VN_DATE_FORMAT),
                DateUtils.format(document.getSentDate(), DateUtils.VN_DATE_FORMAT))
                .forEach(cellTable -> {
                    PdfPCell cell = new PdfPCell();
                    cell.setPhrase(new Phrase(cellTable, font));
                    table.addCell(cell);
                });
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
