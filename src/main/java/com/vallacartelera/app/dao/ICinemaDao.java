package com.vallacartelera.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vallacartelera.app.models.Cinema;

@Repository
public interface ICinemaDao extends JpaRepository<Cinema, Long> {
	public List<Cinema> findBySessions_Movie_Id(Long id);

	public Cinema findByIdAndSessions_Movie_Id(Long cinema_id, Long movie_id);
}
