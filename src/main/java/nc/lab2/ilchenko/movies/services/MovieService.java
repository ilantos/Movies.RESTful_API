package nc.lab2.ilchenko.movies.services;

import nc.lab2.ilchenko.movies.model.Movie;

import java.util.List;

public interface MovieService {
    Movie getById(String id) throws ServiceException;
    Movie getByTitle(String title) throws ServiceException;
    List<Movie> searchByTitle(String title) throws ServiceException;
}
