package com.vallacartelera.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.vallacartelera.app.models.Actor;
import com.vallacartelera.app.models.Genre;
import com.vallacartelera.app.services.IGenreService;
import com.vallacartelera.app.views.Views;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

//@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
@Tag(name = "Genre")
public class GenreController {

	// TODO:
	// - Check all errors and returns, and optimize
	// - Review response of: POST methods

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private IGenreService genreService;

	/**
	 * Returns a List of all Genres in DB. Without List of Movies.
	 */
	@GetMapping("/genres")
	@JsonView({ Views.GetGenre.class }) // Review this View
	public ResponseEntity<?> showAll() {
		List<Genre> genres = null;
		Map<String, Object> response = new HashMap<>();

		try {
			genres = genreService.findAll();
		} catch (DataAccessException e) {
			return dataAccessErrorResponse(e);
		}

		if (genres == null) {
			response.put("message", "There are no Genres in DB");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<List<Genre>>(genres, HttpStatus.OK);
	}

	/**
	 * Returns a Genre. With List of Movies.
	 */
	@GetMapping("/genres/{id}")
	@JsonView({ Views.GetActor.class })
	public ResponseEntity<?> showOne(@PathVariable(value = "id") Long id) {
		Genre genre = null;
		Map<String, Object> response = new HashMap<>();

		try {
			genre = genreService.findById(id);
		} catch (DataAccessException e) {
			return dataAccessErrorResponse(e);
		}

		if (genre == null) {
			response.put("message", "Genre with ID: ".concat(id.toString().concat(" doesn´t exists in the DB!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Genre>(genre, HttpStatus.OK);
	}

	/**
	 * This method create a Genre
	 */
	@PostMapping("/genres")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@Valid @RequestBody Genre genre, BindingResult result) {
		Genre genreNew = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(err -> {
				return "Field '" + err.getField() + "' " + err.getDefaultMessage();
			}).collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			genreNew = genreService.save(genre);
		} catch (DataAccessException e) {
			return dataAccessErrorResponse(e);
		}

		response.put("message", "Genre created successfully!");
		response.put("genre", genreNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	/**
	 * Add an actor to a movie. With the name of the actor or with the Id. If Name
	 * or Id already exists in DB will add it to the movie, if not, this method will
	 * create and add to movie that Actor.
	 */
	@PostMapping("/movies/{id}/genres")
	@JsonView({ Views.PostActor.class })
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createMovieActor(@PathVariable(value = "id") Long movieId, @RequestBody Genre genre) {
		Genre genreNew = null;
		Map<String, Object> response = new HashMap<>();

		try {
			genreNew = genreService.addGenreToMovie(movieId, genre);
		} catch (DataAccessException e) {
			return dataAccessErrorResponse(e);
		}

		response.put("message", "Genre added to Movie with ID: " + movieId + " successfully!");
		response.put("genre", genreNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/genres/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Actor actor, BindingResult result) {
		// TODO
		return null;
	}

	/**
	 * Delete an Actor from DB.
	 */
	@DeleteMapping("/genres/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {

		Map<String, Object> response = new HashMap<>();

		try {
			genreService.delete(id);
		} catch (DataAccessException e) {
			return dataAccessErrorResponse(e);
		}

		response.put("message", "Genre deleted successfully!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	/**
	 * Delete an actor from a movie, but not delete Actor from DB.
	 */
	@DeleteMapping("movies/{movieId}/genres/{genreId}")
	public ResponseEntity<?> deleteActorFromMovie(@PathVariable("movieId") Long movieId,
			@PathVariable("genreId") Long genreId) {

		Map<String, Object> response = new HashMap<>();

		try {
			genreService.deleteGenreFromMovie(movieId, genreId);
		} catch (DataAccessException e) {
			return dataAccessErrorResponse(e);
		}

		response.put("message",
				"Genre with ID: " + genreId + " deleted successfully from Movie with ID: " + movieId + "!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/genres")
	public ResponseEntity<?> deleteAll() {

		Map<String, Object> response = new HashMap<>();

		try {
			genreService.deleteAll();
		} catch (DataAccessException e) {
			return dataAccessErrorResponse(e);
		}

		response.put("message", "All Genres were deleted successfully!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	private ResponseEntity<Map<String, Object>> dataAccessErrorResponse(DataAccessException error) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", "Error when querying the database");
		response.put("error", error.getMessage() + ": " + error.getMostSpecificCause().getMessage());
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
