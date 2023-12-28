package com.vallacartelera.app.services;

import java.util.List;

import com.vallacartelera.app.models.Session;

public interface ISessionService {
	public List<Session> findAll();

	public Session findById(Long id);

	public List<Session> findByMovieId(Long movieId);

	public List<Session> findByCinemaId(Long cinemaId);

	public List<Session> findSessionsForMovieAndForCinema(Long cinemaId, Long movieId);

	public Session save(Session session);

	public void delete(Long id);

	public void deleteAll();
}
