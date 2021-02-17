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

    @Override
    public Movie getById(String id) throws ServiceException {
        String url = URL + API + apiKey + GET_BY_ID + id;
        logger.info("Request to OMDb api:" + url);

        RestTemplate template = new RestTemplate();
        String responseText = template.getForObject(url, String.class);
        logger.debug("Response from OMDb:" + responseText);

        JSONObject json = null;
        try {
            json = new JSONObject(responseText);
        } catch (JSONException e) {
            logger.error(Strings.Service.INVALID_FORMAT_SERVICE, e);
            throw new ServiceException(
                    Strings.Service.INVALID_FORMAT_SERVICE,
                    e);
        }
        if (json.getString("Response").equals("False")) {
            logger.warn(Strings.Service.CANNOT_GET_MOVIE
                    + Strings.Service.MESSAGE_SERVICE
                    + json.getString("Error"));
            throw new ServiceException(json.getString("Error"));
        }
        return new Movie(
                json.getString("imdbID"),
                json.getString("Title"),
                json.getString("Director"),
                json.getString("Year")
        );
    }
}
