package nc.lab2.ilchenko.movies.controllers.utils;

import nc.lab2.ilchenko.movies.model.Movie;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class CustomResponse {
    private static final Logger logger = Logger.getLogger(CustomResponse.class);
    private static final MediaType DEF_MEDIA_TYPE = MediaType.TEXT_PLAIN;

    public static ResponseEntity<?> getResponse(Movie movie, String responseType) {
        try {
            String textBody;
            MediaType contentType = getAvailableContentType(responseType);
            if (contentType.equals(MediaType.APPLICATION_JSON)) {
                textBody = new JSONObject(movie).toString();
            } else {
                textBody = XML.toString(new JSONObject(movie));
            }
            return ResponseEntity
                    .status(200)
                    .contentType(contentType)
                    .body(textBody);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(400)
                    .contentType(DEF_MEDIA_TYPE)
                    .body(e.getMessage());
        }
    }

    public static ResponseEntity<?> getBadRequest(String message) {
        return ResponseEntity
                .badRequest()
                .body(message);
    }

    private static MediaType getAvailableContentType(String type) {
        switch (type.toLowerCase()) {
            case "json":
                return MediaType.APPLICATION_JSON;
            case "xml":
                return MediaType.APPLICATION_XML;
            default:
                logger.warn("Incorrect or not available response type:" + type);
                throw new IllegalArgumentException("Incorrect or not available response type:" + type);
        }
    }
}
