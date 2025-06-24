import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;

public class ResumeGenerator {
    public static void generatePDF(ResumeData data) {
        Document doc = new Document();
        try {
            PdfWriter.getInstance(doc, new FileOutputStream("MyResume.pdf"));
            doc.open();

            Font header = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
            Font subHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font body = FontFactory.getFont(FontFactory.HELVETICA, 12);

            doc.add(new Paragraph(data.name, header));
            doc.add(new Paragraph(data.email + " | " + data.phone, body));
            doc.add(Chunk.NEWLINE);

            doc.add(new Paragraph("Summary", subHeader));
            doc.add(new Paragraph(data.summary, body));

            doc.add(new Paragraph("Education", subHeader));
            doc.add(new Paragraph(data.education, body));

            doc.add(new Paragraph("Experience", subHeader));
            doc.add(new Paragraph(data.experience, body));

            doc.add(new Paragraph("Skills", subHeader));
            doc.add(new Paragraph(data.skills, body));

            doc.close();
            System.out.println("Resume PDF Generated!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
