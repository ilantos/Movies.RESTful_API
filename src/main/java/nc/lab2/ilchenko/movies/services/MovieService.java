package nc.lab2.ilchenko.movies.services;

import nc.lab2.ilchenko.movies.model.Movie;

public interface MovieService {
    Movie getById(String id) throws ServiceException;
}
