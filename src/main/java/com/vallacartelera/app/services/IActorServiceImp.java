package com.vallacartelera.app.services;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vallacartelera.app.dao.IActorDao;
import com.vallacartelera.app.dao.IMovieDao;
import com.vallacartelera.app.errors.exceptions.ResourceNotFoundException;
import com.vallacartelera.app.models.Actor;
import com.vallacartelera.app.models.Movie;

@Service
public class IActorServiceImp implements IActorService {

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private IActorDao actorDao;

	@Autowired
	private IMovieDao movieDao;

	@Override
	@Transactional(readOnly = true)
	public List<Actor> findAll() {
		return (List<Actor>) actorDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Actor findById(Long id) {
//		return actorDao.findById(id).orElse(null);
		return actorDao.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Actor with ID: " + id + " doesn´t exists in the DB!"));
	}

	@Override
	@Transactional
	public Actor save(Actor actor) {
		return actorDao.save(actor);
	}

	@Override
	@Transactional
	public Actor addActorToMovie(Long idMovie, Actor actorRequest) {
		Actor actor = movieDao.findById(idMovie).map(movie -> {
			Long actorId = null;
			String actorName = null;
			actorId = actorRequest.getId();
			actorName = actorRequest.getName();

			// If ActorId exists
			if (actorId != null) {
				Actor _actor = actorDao.findById(actorId).orElse(null);
				movie.addActor(_actor);
				movieDao.save(movie);
				return _actor;
			}

			// If we have a name and the actor already exists in DB.
			if (actorName != null) {
				Actor _actor = actorDao.findByName(actorName);
				if (_actor != null) {
					movie.addActor(_actor);
					movieDao.save(movie);
					return _actor;
				}
			}

			movie.addActor(actorRequest);
			return actorDao.save(actorRequest);
		}).orElse(null);
		return actor;
	}

	@Override
	@Transactional
	public void delete(Long id) {
		actorDao.deleteById(id);
	}

	@Override
	@Transactional
	public void deleteAll() {
		actorDao.deleteAll();
	}

	@Override
	@Transactional
	public void deleteActorFromMovie(Long movieId, Long actorId) {

		logger.info("Actor id:" + actorId + ", Movie Id: " + movieId);

		Movie movie = movieDao.findById(movieId).orElse(null);

		if (movie != null) {
			movie.removeActor(actorId);
			movieDao.save(movie);
		}

	}

}
