package nc.lab2.ilchenko.movies.save.system;

import nc.lab2.ilchenko.movies.model.Movie;

import java.util.List;

public interface SaveMovies {
    void save(List<Movie> movies);
}
