package com.vallacartelera.app.services;

import java.util.List;

import com.vallacartelera.app.models.Cinema;

public interface ICinemaService {
	public List<Cinema> findAll();

	public Cinema findById(Long id);

	public List<Cinema> findAllByMovieId(Long id);

	public Cinema findByMovieIdAndCinemaId(Long cinema_id, Long movie_id);

	public Cinema save(Cinema cinema);

	public void delete(Long id);

	public void deleteAll();
}
