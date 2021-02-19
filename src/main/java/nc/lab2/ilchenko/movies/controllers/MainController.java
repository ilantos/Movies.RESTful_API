package nc.lab2.ilchenko.movies.controllers;

import nc.lab2.ilchenko.movies.controllers.utils.CustomResponse;
import nc.lab2.ilchenko.movies.model.Movie;
import nc.lab2.ilchenko.movies.services.MovieService;
import nc.lab2.ilchenko.movies.services.ServiceException;
import nc.lab2.ilchenko.movies.utils.Strings;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movie")
public class MainController {
    private static final Logger logger = Logger.getLogger(MainController.class);
    private static final String RESPONSE_TYPE_DEF = "json";

    @Autowired
    private MovieService service;
    @Autowired
    private CustomResponse response;

    public MainController(MovieService service) {
        this.service = service;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> movieById(@PathVariable(value = "id") String id,
                                       @RequestParam(value = "response",
                                                     defaultValue = RESPONSE_TYPE_DEF) String responseType) {
        logger.info("User get movie by id:" + id);
        try {
            Movie movie = service.getById(id);
            return response.buildResponse(movie, responseType);
        } catch (ServiceException e) {
            return response.getBadRequest(
                    Strings.Movie.NOT_FOUND);
        }
    }

    @GetMapping("title/{title}")
    public ResponseEntity<?> movieByTitle(@PathVariable(value = "title") String title,
                                          @RequestParam(
                                                  value = "response",
                                                  defaultValue = RESPONSE_TYPE_DEF) String responseType) {
        logger.info("User get movie by title:" + title);
        try {
            Movie movie = service.getByTitle(title);
            return response.buildResponse(movie, responseType);
        } catch (ServiceException e) {
            return response.getBadRequest(
                    Strings.Movie.NOT_FOUND);
        }
    }

    @GetMapping("/search/{title}")
    public ResponseEntity<?> moviesByTitle(@PathVariable(value = "title") String title,
                                           @RequestParam(
                                                   value = "response",
                                                   defaultValue = RESPONSE_TYPE_DEF) String responseType) {
        logger.info("User search movies by title:" + title);
        try {
            List<Movie> movies = service.searchByTitle(title);
            return response.buildResponse(movies, responseType);
        } catch (ServiceException e) {
            return response.getBadRequest(
                    Strings.Movie.NOT_FOUND);
        }
    }
}
