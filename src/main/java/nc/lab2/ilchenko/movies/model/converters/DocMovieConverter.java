package nc.lab2.ilchenko.movies.model.converters;

import nc.lab2.ilchenko.movies.model.Movie;
import nc.lab2.ilchenko.movies.utils.Strings;
import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.List;

@Component
public class DocMovieConverter implements MovieConverter<InputStream> {
    private static final Logger logger
            = Logger.getLogger(DocMovieConverter.class);

    @Override
    public InputStream convert(Movie movie) {
        return get(Collections.singletonList(movie));
    }

    @Override
    public InputStream convert(List<Movie> movies) {
        return get(movies);
    }

    //TODO Нужно сообщить о вероятной ошибке
    // 1) заменить на Optional<InputStream>?
    // 2) возвращать null если что-то не получилось
    // 3) выбрасывать сделанное проверяемое исключение
    // 4) выбрасывать непроверяемое исключение
    public InputStream get(List<Movie> movies) {
        try {
            logger.info("Getting MSWord document...");
            XWPFDocument doc = new XWPFDocument();
            DocMovieConverter.Template.insert(doc, movies);
            ByteArrayOutputStream bais = new ByteArrayOutputStream();
            doc.write(bais);
            return new ByteArrayInputStream(bais.toByteArray());
        } catch (IOException e) {
            logger.error("Cannot create MSWord document", e);
            throw new DocConverterException("Cannot create MSWord document", e);
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
            if (movies.isEmpty()) {
                runTemplateMovies.setText(Strings.Movie.NOT_FOUND);
            } else {
                runTemplateMovies.setText(movies.toString());
            }
            runTemplateMovies.setFontFamily(FONT_FAMILY);
            runTemplateMovies.setFontSize(FONT_SIZE);
        }
    }
}
