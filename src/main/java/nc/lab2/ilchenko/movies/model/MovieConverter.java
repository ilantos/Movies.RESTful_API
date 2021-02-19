package nc.lab2.ilchenko.movies.model;

import java.util.List;

public interface MovieConverter {
    String convert(Movie movie, String type);
    String convert(List<Movie> movies, String type);
}
