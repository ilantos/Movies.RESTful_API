package nc.lab2.ilchenko.movies.services;

import nc.lab2.ilchenko.movies.model.Movie;
import nc.lab2.ilchenko.movies.utils.Strings;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
public class OMDbService implements MovieService {
    private static final Logger logger = Logger.getLogger(OMDbService.class);

    @Value("${service.omdb.api.key}")
    private String apiKey;
    @Value("${service.omdb.url}")
    private String URL;
    private static final String API = "?apikey=";
    private static final String GET_BY_ID = "&i=";
    private static final String GET_BY_TITLE = "&t=";
    private static final String SEARCH_BY_TITLE = "&s=";

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Movie getById(String id) throws ServiceException {
        return getMovie(GET_BY_ID + id);
    }

    @Override
    public Movie getByTitle(String title) throws ServiceException {
        return getMovie(GET_BY_TITLE + title);
    }

    @Override
    public List<Movie> searchByTitle(String title) throws ServiceException {
        List<String> moviesId = searchByTitleGetId(title);
        List<Callable<Movie>> getMovies = new ArrayList<>();
        for (String id: moviesId) {
            getMovies.add(() -> getById(id));
        }

        List<Future<Movie>> futureMovies = new ArrayList<>();
        List<Movie> movies = new ArrayList<>();
        try {
            futureMovies = executorService.invokeAll(getMovies);
            for (Future<Movie> movie: futureMovies) {
                movies.add(movie.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new ServiceException("Cannot async get movies from service", e);
        }
        return movies;
    }

    public List<String> searchByTitleGetId(String title) throws ServiceException {
        List<String> moviesId = new ArrayList<>();
        String url = URL + API + apiKey + SEARCH_BY_TITLE + title;
        JSONObject response = getServiceResponse(url);

        JSONArray jsonMoviesId = response.getJSONArray("Search");
        for (int i = 0; i < jsonMoviesId.length(); i++) {
            moviesId.add(jsonMoviesId.getJSONObject(i).getString("imdbID"));
        }
        logger.debug("Movies id:" + moviesId.toString());
        return moviesId;
    }

    private Movie getMovie(String params) throws ServiceException {
        String url = URL + API + apiKey + params;
        logger.info("Request to OMDb api:" + url);
        JSONObject response = getServiceResponse(url);

        return new Movie(
                response.getString("imdbID"),
                response.getString("Title"),
                response.getString("Director"),
                response.getString("Year")
        );
    }

    private JSONObject getServiceResponse(String url) throws ServiceException {
        String responseText = restTemplate.getForObject(url, String.class);
        logger.debug("Thread id:" + Thread.currentThread().getId() + " | Response from OMDb:" + responseText);

        JSONObject responseJson = null;
        try {
            responseJson = new JSONObject(responseText);
        } catch (JSONException e) {
            logger.error(Strings.Service.INVALID_FORMAT, e);
            throw new ServiceException(
                    Strings.Service.INVALID_FORMAT,
                    e);
        }
        if (responseJson.getString("Response").equals("False")) {
            logger.warn(Strings.Service.CANNOT_GET_MOVIE
                    + Strings.Service.MESSAGE
                    + responseJson.getString("Error"));
            throw new ServiceException(responseJson.getString("Error"));
        }
        return responseJson;
    }
}
