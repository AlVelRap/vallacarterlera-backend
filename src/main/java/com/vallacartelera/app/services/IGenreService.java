package com.vallacartelera.app.services;

import java.util.List;

import com.vallacartelera.app.models.Genre;

public interface IGenreService {
	public List<Genre> findAll();

	public Genre findById(Long id);

	public Genre save(Genre genre);

	public Genre addGenreToMovie(Long idMovie, Genre genreRequest);

	public void delete(Long id);

	public void deleteAll();

	public void deleteGenreFromMovie(Long movieId, Long genreId);
}
