package com.vallacartelera.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vallacartelera.app.dao.ICinemaDao;
import com.vallacartelera.app.models.Cinema;

@Service
public class ICinemaServiceImp implements ICinemaService {

	@Autowired
	private ICinemaDao cinemaDao;

	@Override
	@Transactional(readOnly = true)
	public List<Cinema> findAll() {
		return (List<Cinema>) cinemaDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Cinema findById(Long id) {
		return cinemaDao.findById(id).orElse(null);
	}

	@Override
	public List<Cinema> findAllByMovieId(Long id) {
		return cinemaDao.findBySessions_Movie_Id(id);
	}

	@Override
	public Cinema findByMovieIdAndCinemaId(Long cinema_id, Long movie_id) {
		return cinemaDao.findByIdAndSessions_Movie_Id(cinema_id, movie_id);
	}

	@Override
	@Transactional
	public Cinema save(Cinema cinema) {
		return cinemaDao.save(cinema);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		cinemaDao.deleteById(id);
	}

	@Override
	@Transactional
	public void deleteAll() {
		cinemaDao.deleteAll();

	}

}
