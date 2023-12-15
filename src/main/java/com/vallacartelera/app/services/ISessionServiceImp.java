package com.vallacartelera.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vallacartelera.app.dao.ISessionDao;
import com.vallacartelera.app.errors.exceptions.ResourceNotFoundException;
import com.vallacartelera.app.models.Session;

@Service
public class ISessionServiceImp implements ISessionService {

	@Autowired
	private ISessionDao sessionDao;

	@Override
	@Transactional(readOnly = true)
	public List<Session> findAll() {
		List<Session> sessionList = sessionDao.findAll();
		if (sessionList.isEmpty()) {
			throw new ResourceNotFoundException("There are no Sessions in DB");
		}
		return sessionList;
	}

	@Override
	@Transactional(readOnly = true)
	public Session findById(Long id) {
		return sessionDao.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Session with ID: " + id + " doesn´t exists in the DB!"));
	}
	

	@Override
	public List<Session> findByMovieId(Long movieId) {
		List<Session> sessionList = sessionDao.findByMovie_Id(movieId);
		if (sessionList.isEmpty()) {
			throw new ResourceNotFoundException("There are no Sessions in DB");
		}
		return sessionList;
	}

	@Override
	public List<Session> findByCinemaId(Long cinemaId) {
		List<Session> sessionList = sessionDao.findByCinema_Id(cinemaId);
		if (sessionList.isEmpty()) {
			throw new ResourceNotFoundException("There are no Sessions in DB");
		}
		return sessionList;
	}

	@Override
	@Transactional
	public Session save(Session session) {
		return sessionDao.save(session);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		sessionDao.deleteById(id);
	}

	@Override
	@Transactional
	public void deleteAll() {
		sessionDao.deleteAll();
	}

}
