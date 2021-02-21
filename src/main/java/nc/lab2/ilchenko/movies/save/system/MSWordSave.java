package nc.lab2.ilchenko.movies.save.system;

import nc.lab2.ilchenko.movies.model.Movie;
import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

@Component
public class MSWordSave implements SaveMovies {
    private static final Logger logger = Logger.getLogger(MSWordSave.class);
    @Value("${path.output.doc}")
    private String path;

    @Override
    public synchronized void save(List<Movie> movies) {
        logger.info("Saving movies into MSWord document...");
        XWPFDocument doc = null;
        try {
            logger.debug("Getting MSWord doc xml to append info ...");
            doc = getOutputDoc();
            Template.insert(doc, movies);
        } catch (IOException e) {
            logger.error("Cannot read from MSWord document", e);
        }

        try (FileOutputStream fos = new FileOutputStream(path)) {
            if (doc != null) {
                doc.write(fos);
            } else {
                logger.error("XML MSWord document is null");
            }
        } catch (IOException e) {
            logger.error("Cannot write to MSWord document", e);
        }
    }

    private XWPFDocument getOutputDoc() throws IOException {
        File output = new File(path);
        if (!Files.exists(output.toPath())) {
            createMSWordDoc();
        }

        FileInputStream fis = new FileInputStream(path);
        XWPFDocument document = new XWPFDocument(fis);
        fis.close();
        return document;
    }

    private void createMSWordDoc() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            XWPFDocument doc = new XWPFDocument();
            doc.write(fos);
        }
    }

    private static class Template {
        private static final int FONT_SIZE = 12;
        private static final String FONT_FAMILY = "Calibri";
        private static final DateTimeFormatter TIME_FORMAT =
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

        public static void insert(XWPFDocument doc, List<Movie> movies) {
            XWPFParagraph paragraph = doc.createParagraph();

            XWPFRun runTime = paragraph.createRun();
            runTime.setText("Movies get at: ");
            runTime.setBold(true);
            runTime.setFontSize(FONT_SIZE);
            runTime.setFontFamily(FONT_FAMILY);

            XWPFRun runTemplateTime = paragraph.createRun();
            runTemplateTime.setText(LocalDateTime.now().format(TIME_FORMAT));
            runTemplateTime.setFontFamily(FONT_FAMILY);
            runTemplateTime.setFontSize(FONT_SIZE);

            XWPFRun runMovies = paragraph.createRun();
            runMovies.addBreak();
            runMovies.setText("Movies: ");
            runMovies.setBold(true);
            runMovies.setFontSize(FONT_SIZE);
            runMovies.setFontFamily(FONT_FAMILY);

            XWPFRun runTemplateMovies = paragraph.createRun();
            runTemplateMovies.setText(movies.toString());
            runTemplateMovies.setFontFamily(FONT_FAMILY);
            runTemplateMovies.setFontSize(FONT_SIZE);
        }
    }
}
