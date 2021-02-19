package nc.lab2.ilchenko.movies.model;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class MovieConverterImpl implements MovieConverter {
    private static final Logger logger = Logger.getLogger(MovieConverterImpl.class);

    private Map<String, Function<Movie, String>> convertMovie = new HashMap<>();
    private Map<String, Function<List<Movie>, String>> convertMovies = new HashMap<>();

    public MovieConverterImpl() {
        convertMovie.put("json", (movie) -> new JSONObject(movie).toString());
        convertMovie.put("xml", (movie) -> XML.toString(new JSONObject(movie)));

        convertMovies.put("json", (movies) -> new JSONArray(movies).toString());
        convertMovies.put("xml", (movies) -> XML.toString(new JSONArray(movies)));

    }

    @Override
    public String convert(Movie movie, String type) {
        if (convertMovie.containsKey(type)) {
            return convertMovie.get(type).apply(movie);
        } else {
            throw new RuntimeException(
                    "Isn't available media type to convert movie");
        }
    }

    public String convert(List<Movie> movies, String type) {
        if (convertMovies.containsKey(type)) {
            return convertMovies.get(type).apply(movies);
        } else {
            throw new RuntimeException(
                    "Isn't available media type to convert movies");
        }
    }
}
