package com.vallacartelera.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vallacartelera.app.models.Session;

@Repository
public interface ISessionDao extends JpaRepository<Session, Long> {
	public List<Session> findByCinema_Id(Long cinemaId);

	public List<Session> findByMovie_Id(Long movieId);

	public List<Session> findByMovie_IdAndCinema_Id(Long movieId, Long cinemaId);
}
