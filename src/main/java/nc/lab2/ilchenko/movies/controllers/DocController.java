package nc.lab2.ilchenko.movies.controllers;

import nc.lab2.ilchenko.movies.model.Movie;
import nc.lab2.ilchenko.movies.model.converters.DocConverterException;
import nc.lab2.ilchenko.movies.model.converters.MovieConverter;
import nc.lab2.ilchenko.movies.services.MovieService;
import nc.lab2.ilchenko.movies.services.ServiceException;
import nc.lab2.ilchenko.movies.utils.Strings;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController extends Controller {
    private static final Logger logger= Logger.getLogger(DocController.class);
    private HttpHeaders headers;

    @Autowired
    public DocController(MovieService service,
                         @Qualifier("docMovieConverter")
                                 MovieConverter<?> converter,
                         @Qualifier("docControllerHeaders")
                                     HttpHeaders headers) {
        super(service);
        this.converter = converter;
        this.headers = headers;
        this.responseType = MediaType.APPLICATION_OCTET_STREAM;
    }

    @Override
    public ResponseEntity<?> movieById(@PathVariable String id)
            throws ServiceException {
        logger.info("User get movie by id:" + id);
        Movie movie = service.getById(id);
        InputStream docStream = (InputStream) converter.convert(movie);
        return buildResponse(docStream);
    }

    @Override
    public ResponseEntity<?> movieByTitle(@PathVariable String title)
            throws ServiceException {
        logger.info("User get movie by title:" + title);
        Movie movie = service.getByTitle(title);
        InputStream docStream = (InputStream) converter.convert(movie);
        return buildResponse(docStream);
    }

    @Override
    public ResponseEntity<?> moviesByTitle(@PathVariable String title)
            throws ServiceException {
        logger.info("User search movies by title:" + title);
        List<Movie> movies = service.searchByTitle(title);
        InputStream docStream = (InputStream) converter.convert(movies);
        return buildResponse(docStream);
    }

    private ResponseEntity<?> buildResponse(InputStream doc,
                                            HttpHeaders headers) {
        try {
            byte[] fileBytes = doc.readAllBytes();
            return ResponseEntity
                    .status(200)
                    .contentType(responseType)
                    .headers(headers)
                    .body(fileBytes);
        } catch (IOException e) {
            logger.error("Cannot create byte array of file", e);
            return ResponseEntity
                    .status(400)
                    .body(Strings.Movie.NOT_FOUND);
        }
    }

    private ResponseEntity<?> buildResponse(InputStream doc) {
        return buildResponse(doc, this.headers);
    }

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR,
            reason = Strings.Model.MODEL_ERROR)
    @ExceptionHandler(DocConverterException.class)
    protected void documentSendError() {
    }
}
