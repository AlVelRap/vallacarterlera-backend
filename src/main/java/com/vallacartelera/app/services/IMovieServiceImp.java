package com.vallacartelera.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vallacartelera.app.dao.IMovieDao;
import com.vallacartelera.app.errors.exceptions.ResourceNotFoundException;
import com.vallacartelera.app.models.Movie;

@Service
public class IMovieServiceImp implements IMovieService {

	@Autowired
	private IMovieDao movieDao;

	@Override
	@Transactional(readOnly = true)
	public List<Movie> findAll() {
		List<Movie> movieList = movieDao.findAll();
		if (movieList.isEmpty()) {
			throw new ResourceNotFoundException("There are no Movies in DB");
		}
		return movieList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Movie> findAllByCinemaId(Long id) {
		List<Movie> movieList = movieDao.findBySessions_Cinema_Id(id);
		if (movieList.isEmpty()) {
			throw new ResourceNotFoundException("There are no Movies for Cinema with ID: " + id);
		}
		return movieList;
	}

	@Override
	@Transactional(readOnly = true)
	public Movie findByMovieIdAndCinemaId(Long cinemaId, Long movieId) {
		Movie movie = movieDao.findByIdAndSessions_Cinema_Id(movieId, cinemaId);
		if (movie == null) {
			throw new ResourceNotFoundException(
					"There are no Movie with ID: " + movieId + " for Cinema with ID: " + cinemaId);
		}
		return movie;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Movie> findByActorId(Long actorId) {
		List<Movie> movieList = movieDao.findByCast_Id(actorId);
		if (movieList.isEmpty()) {
			throw new ResourceNotFoundException("There are no Movies for Actor with ID: " + actorId);
		}
		return movieList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Movie> findByGenreId(Long genreId) {
		List<Movie> movieList = movieDao.findByGenre_Id(genreId);
		if (movieList.isEmpty()) {
			throw new ResourceNotFoundException("There are no Movies for Genre with ID: " + genreId);
		}
		return movieList;
	}

	@Override
	@Transactional(readOnly = true)
	public Movie findById(Long id) {
		return movieDao.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Movie with ID: " + id + " doesn´t exists in the DB!"));
	}

	@Override
	@Transactional
	public Movie save(Movie movie) {
		return movieDao.save(movie);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		movieDao.deleteById(id);
	}

	@Override
	@Transactional
	public void deleteAll() {
		movieDao.deleteAll();
	}

}
