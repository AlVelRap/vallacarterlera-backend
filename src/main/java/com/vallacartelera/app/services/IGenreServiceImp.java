package com.vallacartelera.app.services;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vallacartelera.app.dao.IGenreDao;
import com.vallacartelera.app.dao.IMovieDao;
import com.vallacartelera.app.models.Genre;
import com.vallacartelera.app.models.Movie;

@Service
public class IGenreServiceImp implements IGenreService {

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private IGenreDao genreDao;

	@Autowired
	private IMovieDao movieDao;

	@Override
	@Transactional(readOnly = true)
	public List<Genre> findAll() {
		return (List<Genre>) genreDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Genre findById(Long id) {
		return genreDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Genre save(Genre genre) {
		return genreDao.save(genre);
	}

	@Override
	@Transactional
	public Genre addGenreToMovie(Long idMovie, Genre genreRequest) {
		Genre genre = movieDao.findById(idMovie).map(movie -> {
			Long genreId = null;
			String genreName = null;
			genreId = genreRequest.getId();
			genreName = genreRequest.getName();

			// If genreId exists
			if (genreId != null) {
				Genre _genre = genreDao.findById(genreId).orElse(null);
				movie.addGenre(_genre);
				movieDao.save(movie);
				return _genre;
			}

			// If we have a name and the genre already exists in DB.
			if (genreName != null) {
				Genre _genre = genreDao.findByName(genreName);
				if (_genre != null) {
					movie.addGenre(_genre);
					movieDao.save(movie);
					return _genre;
				}
			}

			movie.addGenre(genreRequest);
			return genreDao.save(genreRequest);
		}).orElse(null);
		return genre;
	}

	@Override
	@Transactional
	public void delete(Long id) {
		genreDao.deleteById(id);
	}

	@Override
	@Transactional
	public void deleteAll() {
		genreDao.deleteAll();
	}

	@Override
	@Transactional
	public void deleteGenreFromMovie(Long movieId, Long genreId) {

		logger.info("Actor id:" + genreId + ", Movie Id: " + movieId);

		Movie movie = movieDao.findById(movieId).orElse(null);

		if (movie != null) {
			movie.removeActor(genreId);
			movieDao.save(movie);
		}

	}

}
