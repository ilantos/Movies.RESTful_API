package nc.lab2.ilchenko.movies.controllers;

import nc.lab2.ilchenko.movies.model.converters.MovieConverter;
import nc.lab2.ilchenko.movies.services.MovieService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/json")
public class JSONController extends Controller {
    public JSONController(MovieService service,
                          @Qualifier("JSONMovieConverter")
                                  MovieConverter<?> converter) {
        super(service);
        this.converter = converter;
        this.responseType = MediaType.APPLICATION_JSON;
    }
}
