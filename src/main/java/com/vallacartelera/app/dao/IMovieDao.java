package com.vallacartelera.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vallacartelera.app.models.Movie;

@Repository
public interface IMovieDao extends JpaRepository<Movie, Long> {
	public List<Movie> findBySessions_Cinema_Id(Long id);
	
	public Movie findByIdAndSessions_Cinema_Id(Long movie_id,Long cinema_id);
	
	public List<Movie> findByCast_Id(Long actorId);
	
	public List<Movie> findByGenre_Id(Long genreId);
}
