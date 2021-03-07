package nc.lab2.ilchenko.movies.model.converters;

import nc.lab2.ilchenko.movies.model.Movie;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class XMLMovieConverter implements MovieConverter<String> {
    @Override
    public String convert(Movie movie) {
        JSONObject root = new JSONObject();
        root.put("movie", new JSONObject(movie));
        return XML.toString(root);
    }

    @Override
    public String convert(List<Movie> movies) {
        JSONArray jsonMovies = new JSONArray(movies);
        JSONObject rootElement = new JSONObject();
        rootElement.put("movie", jsonMovies);
        JSONObject root = new JSONObject();
        root.put("movies", rootElement);
        return XML.toString(root);
    }
}
