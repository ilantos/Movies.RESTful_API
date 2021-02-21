package nc.lab2.ilchenko.movies.controllers.utils;

import nc.lab2.ilchenko.movies.model.Movie;
import nc.lab2.ilchenko.movies.model.MovieConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CustomResponse {
    private static final Logger logger = Logger.getLogger(CustomResponse.class);
    private static final MediaType DEF_MEDIA_TYPE = MediaType.TEXT_PLAIN;

    private Map<String, MediaType> responseTypes = new HashMap<>();
    private MovieConverter converter;

    @Autowired
    public CustomResponse(MovieConverter converter) {
        this.converter = converter;
    }

    @PostConstruct
    public void initialize() {
        logger.trace("initializing map...");
        responseTypes.put("json", MediaType.APPLICATION_JSON);
        responseTypes.put("xml", MediaType.APPLICATION_XML);
    }

    public ResponseEntity<?> buildResponse(Movie movie, String responseType) {
        String textBody = converter.convert(movie, responseType);
        return buildResponse(textBody, responseType);
    }

    public ResponseEntity<?> buildResponse(List<Movie> movies, String responseType) {
        String textBody = converter.convert(movies, responseType);
        return buildResponse(textBody, responseType);

    }

    public ResponseEntity<?> getBadRequest(String message) {
        return ResponseEntity
                .badRequest()
                .body(message);
    }

    private ResponseEntity<?> buildResponse(String bodyText, String type) {
        String clearType = type.toLowerCase();
        if (responseTypes.containsKey(clearType)) {
            MediaType mediaType = responseTypes.get(clearType);
            return ResponseEntity
                    .status(200)
                    .contentType(mediaType)
                    .body(bodyText);
        } else {
            return ResponseEntity
                    .status(400)
                    .contentType(DEF_MEDIA_TYPE)
                    .body("Incorrect or not available response type:" + type);
        }
    }
}
