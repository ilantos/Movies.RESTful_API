package nc.lab2.ilchenko.movies.model.converters;

import nc.lab2.ilchenko.movies.model.Movie;

import java.util.List;

public interface MovieConverter<T> {
    T convert(Movie movie);
    T convert(List<Movie> movies);
}
