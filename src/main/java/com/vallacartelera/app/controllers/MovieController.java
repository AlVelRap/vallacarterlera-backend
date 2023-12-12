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
import com.vallacartelera.app.models.Movie;
import com.vallacartelera.app.services.IMovieService;
import com.vallacartelera.app.views.Views;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

//@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
@Tag(name = "Movie")
public class MovieController {

	// TODO:
	// - Finish PUT method
	// - Add GET method to find by Genre, like the Actor method

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private IMovieService movieService;

	/**
	 * Return a List of Movies. Without Sessions
	 */
	@GetMapping("/movies")
	@JsonView({ Views.GetAllMovies.class })
	public ResponseEntity<?> showAllMovies() {
		return new ResponseEntity<List<Movie>>(movieService.findAll(), HttpStatus.OK);
	}

	/**
	 * Return a Movie. With Sessions
	 */
	@GetMapping("/movies/{id}")
	@JsonView({ Views.GetMovie.class })
	public ResponseEntity<?> showMovie(@PathVariable Long id) {
		return new ResponseEntity<Movie>(movieService.findById(id), HttpStatus.OK);
	}

	/**
	 * Return list of Movies from one Cinema. Without Sessions
	 */
	@GetMapping("/cinemas/{id}/movies")
	@JsonView({ Views.GetAllMovies.class })
	public ResponseEntity<?> showAllMoviesForCinema(@PathVariable Long id) {
		return new ResponseEntity<List<Movie>>(movieService.findAllByCinemaId(id), HttpStatus.OK);
	}

	/**
	 * Return data from a Movie from one Cinema with all sessions for that cinema
	 */
	@GetMapping("/cinemas/{cinema_id}/movies/{movie_id}")
	@JsonView({ Views.GetOneMovieForCinema.class })
	public ResponseEntity<?> showAllSessionsForMovieForCinema(@PathVariable Long cinema_id,
			@PathVariable Long movie_id) {
		return new ResponseEntity<Movie>(movieService.findByMovieIdAndCinemaId(cinema_id, movie_id), HttpStatus.OK);
	}

	/**
	 * Return a List of Movies that have an Actor. Without Sessions
	 */
	@GetMapping("/actors/{id}/movies")
	@JsonView({ Views.GetAllMovies.class })
	public ResponseEntity<?> showOneActorMovies(@PathVariable(value = "id") Long id) {
		return new ResponseEntity<List<Movie>>(movieService.findByActorId(id), HttpStatus.OK);
	}

	/**
	 * Return a List of Movies that have an Actor. Without Sessions
	 */
	@GetMapping("/genres/{id}/movies")
	@JsonView({ Views.GetAllMovies.class })
	public ResponseEntity<?> showOneGenreMovies(@PathVariable(value = "id") Long id) {
		return new ResponseEntity<List<Movie>>(movieService.findByGenreId(id), HttpStatus.OK);
	}

	@PostMapping("/movies")
	@ResponseStatus(HttpStatus.CREATED)
	@JsonView({ Views.GetMovie.class })
	public ResponseEntity<?> create(@Valid @RequestBody Movie movie, BindingResult result) {
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(err -> {
				return "El campo '" + err.getField() + "' " + err.getDefaultMessage();
			}).collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		response.put("message", "Movie created successfully!");
		response.put("movie", movieService.save(movie));
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/movies/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Movie movie, BindingResult result) {
		// TODO
		return null;
	}

	@DeleteMapping("/movies/{id}")
	public ResponseEntity<?> deleteOne(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		movieService.delete(id);

		response.put("message", "Movie deleted successfully!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/movies")
	public ResponseEntity<?> deleteAll() {
		Map<String, Object> response = new HashMap<>();

		movieService.deleteAll();

		response.put("message", "All Sessions were deleted successfully!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

}
