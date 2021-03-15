package nc.lab2.ilchenko.movies.model.converters;

import nc.lab2.ilchenko.movies.model.Movie;
import nc.lab2.ilchenko.movies.utils.Strings;
import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
        private static final String WIDTH_CELL_LEFT = "3000";
        private static final String WIDTH_CELL_RIGHT = "6000";

        public static void insert(XWPFDocument doc, List<Movie> movies) {
            XWPFParagraph paragraph = doc.createParagraph();

            XWPFRun runMovies = paragraph.createRun();
            paragraph.setAlignment(ParagraphAlignment.CENTER);
            runMovies.setText("Movies");
            runMovies.setBold(true);
            runMovies.setFontSize(FONT_SIZE);
            runMovies.setFontFamily(FONT_FAMILY);

            if (movies.isEmpty()) {
                XWPFRun runTemplateMovies = paragraph.createRun();
                runTemplateMovies.setText(":" + Strings.Movie.NOT_FOUND);
                runTemplateMovies.setFontFamily(FONT_FAMILY);
                runTemplateMovies.setFontSize(FONT_SIZE);
            } else {
                for (Movie movie: movies) {
                    printMovie(doc, movie);
                    doc.createParagraph();
                }
            }
        }

        public static void printMovie(XWPFDocument doc, Movie movie) {
            XWPFTable table = doc.createTable();
            XWPFTableRow row = table.getRow(0);
            row.addNewTableCell();
            printRow(row, "id", movie.getId());
            printRow(table.createRow(), "title", movie.getTitle());
            printRow(table.createRow(), "director", movie.getDirector());
            printRow(table.createRow(), "year", movie.getYear());
        }

        private static void printRow(XWPFTableRow row,
                                     String textLeftCell,
                                     String textRightCell) {
            row.getCell(0).setText(textLeftCell);
            row.getCell(0).setWidth(WIDTH_CELL_LEFT);

            row.getCell(1).setText(textRightCell);
            row.getCell(1).setWidth(WIDTH_CELL_RIGHT);
        }
    }
}
