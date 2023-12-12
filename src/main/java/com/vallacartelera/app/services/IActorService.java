package com.vallacartelera.app.services;

import java.util.List;

import com.vallacartelera.app.models.Actor;

public interface IActorService {
	public List<Actor> findAll();

	public Actor findById(Long id);

	public Actor save(Actor actor);

	public Actor addActorToMovie(Long idMovie, Actor actor);

	public void delete(Long id);

	public void deleteAll();

	public void deleteActorFromMovie(Long movieId, Long actorId);
}
