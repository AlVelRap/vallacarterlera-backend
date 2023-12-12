package com.vallacartelera.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vallacartelera.app.models.Genre;

@Repository
public interface IGenreDao extends JpaRepository<Genre, Long> {
	public Genre findByName(String name);

}
