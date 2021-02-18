package nc.lab2.ilchenko.movies.controllers;

import nc.lab2.ilchenko.movies.controllers.utils.CustomResponse;
import nc.lab2.ilchenko.movies.model.Movie;
import nc.lab2.ilchenko.movies.services.MovieService;
import nc.lab2.ilchenko.movies.services.ServiceException;
import nc.lab2.ilchenko.movies.utils.Strings;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movie")
public class MainController {
    private static final Logger logger = Logger.getLogger(MainController.class);
    private MovieService service;
    private static final String RESPONSE_TYPE_DEF = "json";

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
            return CustomResponse.getResponse(movie, responseType);
        } catch (ServiceException e) {
            return CustomResponse.getBadRequest(
                    Strings.Controller.MOVIE_NOT_FOUND);
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
            return CustomResponse.getResponse(movie, responseType);
        } catch (ServiceException e) {
            return CustomResponse.getBadRequest(
                    Strings.Controller.MOVIE_NOT_FOUND);
        }
    }
}
