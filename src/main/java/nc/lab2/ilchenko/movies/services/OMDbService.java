package nc.lab2.ilchenko.movies.services;

import nc.lab2.ilchenko.movies.model.Movie;
import nc.lab2.ilchenko.movies.utils.Strings;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OMDbService implements MovieService {
    private static final Logger logger = Logger.getLogger(OMDbService.class);

    @Value("${api.key.omdb}")
    private String apiKey;
    private static final String URL = "http://www.omdbapi.com/";
    private static final String API = "?apikey=";
    private static final String GET_BY_ID = "&i=";
    private static final String GET_BY_TITLE = "&t=";

    @Override
    public Movie getById(String id) throws ServiceException {
        return getMovie(GET_BY_ID + id);
    }

    @Override
    public Movie getByTitle(String title) throws ServiceException {
        return getMovie(GET_BY_TITLE + title);
    }

    private Movie getMovie(String params) throws ServiceException {
        String url = URL + API + apiKey + params;
        logger.info("Request to OMDb api:" + url);

        RestTemplate template = new RestTemplate();
        String responseText = template.getForObject(url, String.class);
        logger.debug("Response from OMDb:" + responseText);

        JSONObject responseJson = null;
        try {
            responseJson = new JSONObject(responseText);
        } catch (JSONException e) {
            logger.error(Strings.Service.INVALID_FORMAT_SERVICE, e);
            throw new ServiceException(
                    Strings.Service.INVALID_FORMAT_SERVICE,
                    e);
        }
        if (responseJson.getString("Response").equals("False")) {
            logger.warn(Strings.Service.CANNOT_GET_MOVIE
                    + Strings.Service.MESSAGE_SERVICE
                    + responseJson.getString("Error"));
            throw new ServiceException(responseJson.getString("Error"));
        }
        return new Movie(
                responseJson.getString("imdbID"),
                responseJson.getString("Title"),
                responseJson.getString("Director"),
                responseJson.getString("Year")
        );
    }
}
