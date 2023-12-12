package com.vallacartelera.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vallacartelera.app.dao.IMovieDao;
import com.vallacartelera.app.models.Movie;

@Service
public class IMovieServiceImp implements IMovieService {

	@Autowired
	private IMovieDao movieDao;

	@Override
	@Transactional(readOnly = true)
	public List<Movie> findAll() {
		return (List<Movie>) movieDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Movie> findAllByCinemaId(Long id) {
		return movieDao.findBySessions_Cinema_Id(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Movie findByMovieIdAndCinemaId(Long cinema_id, Long movie_id) {
		return movieDao.findByIdAndSessions_Cinema_Id(movie_id, cinema_id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Movie> findByActorId(Long actorId) {
		return movieDao.findByCast_Id(actorId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Movie> findByGenreId(Long genreId) {
		return movieDao.findByGenre_Id(genreId);
	}

	@Override
	@Transactional(readOnly = true)
	public Movie findById(Long id) {
		return movieDao.findById(id).orElse(null);
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
