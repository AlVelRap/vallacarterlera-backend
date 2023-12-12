package com.vallacartelera.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vallacartelera.app.dao.ICinemaDao;
import com.vallacartelera.app.errors.exceptions.ResourceNotFoundException;
import com.vallacartelera.app.models.Cinema;

@Service
public class ICinemaServiceImp implements ICinemaService {

	@Autowired
	private ICinemaDao cinemaDao;

	@Override
	@Transactional(readOnly = true)
	public List<Cinema> findAll() {
		List<Cinema> cinemaList = cinemaDao.findAll();
		if (cinemaList.isEmpty()) {
			throw new ResourceNotFoundException("There are no Cinemas in DB");
		}
		return cinemaList;

	}

	@Override
	@Transactional(readOnly = true)
	public Cinema findById(Long id) {
		return cinemaDao.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Cinema with ID: " + id + " doesn´t exists in the DB!"));
	}

	@Override
	public List<Cinema> findAllByMovieId(Long id) {
		List<Cinema> cinemaList = cinemaDao.findBySessions_Movie_Id(id);
		if (cinemaList.isEmpty()) {
			throw new ResourceNotFoundException("There are no Cinemas in DB");
		}
		return cinemaList;
	}

	@Override
	public Cinema findByMovieIdAndCinemaId(Long cinemaId, Long movieId) {
		Cinema cinema = cinemaDao.findByIdAndSessions_Movie_Id(cinemaId, movieId);
		if (cinema == null) {
			throw new ResourceNotFoundException(
					"There are no Cinema with ID: " + cinemaId + " for Movie with ID: " + movieId);
		}
		return cinema;
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
