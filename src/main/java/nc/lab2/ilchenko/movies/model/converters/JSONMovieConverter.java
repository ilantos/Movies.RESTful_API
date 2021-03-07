package nc.lab2.ilchenko.movies.model.converters;

import nc.lab2.ilchenko.movies.model.Movie;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JSONMovieConverter implements MovieConverter<String> {
    @Override
    public String convert(Movie movie) {
        return new JSONObject(movie).toString();
    }

    @Override
    public String convert(List<Movie> movies) {
        return new JSONArray(movies).toString();
    }
}
