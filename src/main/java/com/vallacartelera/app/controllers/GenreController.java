package com.vallacartelera.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	@JsonView({ Views.GetGenre.class })
	public ResponseEntity<?> showAll() {
		return new ResponseEntity<List<Genre>>(genreService.findAll(), HttpStatus.OK);
	}

	/**
	 * Returns a Genre. With List of Movies.
	 */
	@GetMapping("/genres/{id}")
	@JsonView({ Views.GetGenre.class })
	public ResponseEntity<?> showOne(@PathVariable(value = "id") Long id) {
		return new ResponseEntity<Genre>(genreService.findById(id), HttpStatus.OK);
	}

	/**
	 * This method create a Genre
	 */
	@PostMapping("/genres")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@Valid @RequestBody Genre genre, BindingResult result) {
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(err -> {
				return "Field '" + err.getField() + "' " + err.getDefaultMessage();
			}).collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		response.put("message", "Genre created successfully!");
		response.put("genre", genreService.save(genre));
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
		Map<String, Object> response = new HashMap<>();

		response.put("message", "Genre added to Movie with ID: " + movieId + " successfully!");
		response.put("genre", genreService.addGenreToMovie(movieId, genre));
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

		genreService.delete(id);

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

		genreService.deleteGenreFromMovie(movieId, genreId);

		response.put("message",
				"Genre with ID: " + genreId + " deleted successfully from Movie with ID: " + movieId + "!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/genres")
	public ResponseEntity<?> deleteAll() {
		Map<String, Object> response = new HashMap<>();

		genreService.deleteAll();

		response.put("message", "All Genres were deleted successfully!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

}
