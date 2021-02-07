package fr.tse.fise3.info6.start_up_poc.utils;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.util.Collection;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import fr.tse.fise3.info6.start_up_poc.domain.Log;
import fr.tse.fise3.info6.start_up_poc.domain.User;

import javax.servlet.http.HttpServletResponse;

public class LogPDFExporter {

    private User user;

    private Collection<Log> logs;

    public LogPDFExporter(User user, Collection<Log> logs) {
        this.user = user;
        this.logs = logs;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Log ID", font));

        table.addCell(cell);

        cell.setPhrase(new Phrase("Start datetime", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("End Datetime", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Project", font));
        table.addCell(cell);
    }

    private Double writeTableData(PdfPTable table) {
        Long countHours = Long.valueOf(0);
        for (Log log : logs) {
            table.addCell(String.valueOf(log.getId()));
            table.addCell(String.valueOf(log.getStart()));
            table.addCell(String.valueOf(log.getEnd()));
            table.addCell(log.getProject().getTitle());
            countHours += Duration.between(log.getStart(),log.getEnd()).toMinutes();
        }
        return countHours/60.0;
    }

    public ByteArrayInputStream export() throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("List of logs", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f, 1.5f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        Double countHours = writeTableData(table);

        document.add(table);

        font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(12);
        font.setColor(Color.BLACK);

        p = new Paragraph("Number of hours : " + String.valueOf(countHours), font);
        p.setAlignment(Paragraph.ALIGN_RIGHT);

        document.add(p);

        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}
