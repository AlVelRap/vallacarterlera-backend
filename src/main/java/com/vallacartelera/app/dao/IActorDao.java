package com.vallacartelera.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vallacartelera.app.models.Actor;

@Repository
public interface IActorDao extends JpaRepository<Actor, Long> {
	public Actor findByName(String name);

}
