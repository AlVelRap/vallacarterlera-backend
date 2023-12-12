package com.vallacartelera.app.services;

import java.util.List;

import com.vallacartelera.app.models.Movie;

public interface IMovieService {
	public List<Movie> findAll();

	public Movie findById(Long id);
	
	public List<Movie> findByActorId(Long actorId);
	
	public List<Movie> findByGenreId(Long genreId);
	
	public List<Movie> findAllByCinemaId(Long id);
	
	public Movie findByMovieIdAndCinemaId(Long cinema_id,Long movie_id);

	public Movie save(Movie movie);

	public void delete(Long id);
	
	public void deleteAll();
}
