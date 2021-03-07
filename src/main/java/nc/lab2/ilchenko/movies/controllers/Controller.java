package nc.lab2.ilchenko.movies.controllers;

import nc.lab2.ilchenko.movies.model.Movie;
import nc.lab2.ilchenko.movies.model.converters.MovieConverter;
import nc.lab2.ilchenko.movies.services.MovieService;
import nc.lab2.ilchenko.movies.services.ServiceException;
import nc.lab2.ilchenko.movies.utils.Strings;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public abstract class Controller {
    private static final Logger logger = Logger.getLogger(Controller.class);
    protected MovieService service;
    protected MovieConverter<?> converter;
    protected MediaType responseType;

    protected static final String PATH_GET_BY_ID = "/movie/id/{id}";
    protected static final String PATH_GET_BY_TITLE = "/movie/title/{title}";
    protected static final String PATH_SEARCH_BY_TITLE
            = "/movie/search/{title}";

    @Autowired
    public Controller(MovieService service) {
        this.service = service;
    }

    @GetMapping(PATH_GET_BY_ID)
    public ResponseEntity<?> movieById(@PathVariable String id)
            throws ServiceException {
        logger.info("User get movie by id:" + id);
        Movie movie = service.getById(id);
        String textResponse = (String) converter.convert(movie);
        return ResponseEntity
                .status(200)
                .contentType(responseType)
                .body(textResponse);
    }

    @GetMapping(PATH_GET_BY_TITLE)
    public ResponseEntity<?> movieByTitle(@PathVariable String title)
            throws ServiceException {
        logger.info("User get movie by title:" + title);
        Movie movie = service.getByTitle(title);
        String textResponse = (String) converter.convert(movie);
        return ResponseEntity
                .status(200)
                .contentType(responseType)
                .body(textResponse);
    }

    @GetMapping(PATH_SEARCH_BY_TITLE)
    public ResponseEntity<?> moviesByTitle(@PathVariable String title)
            throws ServiceException {
        logger.info("User search movies by title:" + title);
        List<Movie> movies = service.searchByTitle(title);
        String textResponse = (String) converter.convert(movies);
        return ResponseEntity
                .status(200)
                .contentType(responseType)
                .body(textResponse);
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND,
            reason = Strings.Movie.NOT_FOUND)
    @ExceptionHandler(ServiceException.class)
    protected void serviceError() {
    }
}
