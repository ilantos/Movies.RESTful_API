package nc.lab2.ilchenko.movies.controllers;

import nc.lab2.ilchenko.movies.model.converters.MovieConverter;
import nc.lab2.ilchenko.movies.services.MovieService;

import nc.lab2.ilchenko.movies.services.ServiceException;
import nc.lab2.ilchenko.movies.utils.Strings;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("xml")
public class XMLController extends Controller {
    public XMLController(MovieService service,
                         @Qualifier("XMLMovieConverter")
                                 MovieConverter<?> converter) {
        super(service);
        this.converter = converter;
        this.responseType = MediaType.APPLICATION_XML;
    }

    @Override
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<?> handlerServiceError() {
        JSONObject response = new JSONObject();
        response.put("response", Strings.Movie.NOT_FOUND);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(responseType)
                .body(XML.toString(response));
    }
}
