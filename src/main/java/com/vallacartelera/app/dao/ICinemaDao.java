package com.vallacartelera.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vallacartelera.app.models.Cinema;

@Repository
public interface ICinemaDao extends JpaRepository<Cinema, Long> {
	public List<Cinema> findBySessions_Movie_Id(Long id);

	@Query(value = "select distinct c from Cinema c join fetch c.sessions s where c.id = ?1 and s.movie.id = ?2")
	public Cinema findByIdAndSessions_Movie_Id(Long cinema_id, Long movie_id);
}
