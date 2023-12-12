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
import com.vallacartelera.app.models.Cinema;
import com.vallacartelera.app.services.ICinemaService;
import com.vallacartelera.app.views.Views;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

//@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
@Tag(name = "Cinema")
public class CinemaController {

	// TODO:
	// - Finish PUT method

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private ICinemaService cinemaService;

	/**
	 * Return list of Cinemas, without Sessions
	 */
	@GetMapping("/cinemas")
	@JsonView({ Views.GetAllCinemas.class })
	public ResponseEntity<?> showAll() {
		List<Cinema> cinema = null;
		Map<String, Object> response = new HashMap<>();

		try {
			cinema = cinemaService.findAll();
		} catch (DataAccessException e) {
			return dataAccessErrorResponse(e);
		}

		if (cinema == null) {
			response.put("message", "There are no Cinemas in DB");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<List<Cinema>>(cinema, HttpStatus.OK);
	}

	/**
	 * Return data from one Cinema, with Sessions
	 */
	@GetMapping("/cinemas/{id}")
	@JsonView({ Views.GetCinema.class })
	public ResponseEntity<?> showCinema(@PathVariable Long id) {
		Cinema cinema = null;
		Map<String, Object> response = new HashMap<>();

		try {
			cinema = cinemaService.findById(id);
		} catch (DataAccessException e) {
			return dataAccessErrorResponse(e);
		}

		if (cinema == null) {
			response.put("message", "Cinema with ID: ".concat(id.toString().concat(" doesn´t exists in the DB!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Cinema>(cinema, HttpStatus.OK);
	}

	/**
	 * Return a List of Cinemas that have One Movie. Without Sessions
	 */
	@GetMapping("/movies/{id}/cinemas")
	@JsonView({ Views.GetAllCinemas.class })
	public ResponseEntity<?> showAllCinemasForMovie(@PathVariable Long id) {
		List<Cinema> cinema = null;
		Map<String, Object> response = new HashMap<>();

		try {
			cinema = cinemaService.findAllByMovieId(id);
		} catch (DataAccessException e) {
			return dataAccessErrorResponse(e);
		}

		if (cinema == null) {
			response.put("message", "There are no Cinemas for Movie with ID: " + id);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<List<Cinema>>(cinema, HttpStatus.OK);
	}

	/**
	 * Return a Cinema that have a Movie. With Sessions
	 */
	@GetMapping("/movies/{movie_id}/cinemas/{cinema_id}")
	@JsonView({ Views.GetOneCinemasForMovie.class })
	public ResponseEntity<?> showAllSessionsForCinemaForMovie(@PathVariable Long cinema_id,
			@PathVariable Long movie_id) {
		Cinema cinema = null;
		Map<String, Object> response = new HashMap<>();

		try {
			cinema = cinemaService.findByMovieIdAndCinemaId(cinema_id, movie_id);
		} catch (DataAccessException e) {
			return dataAccessErrorResponse(e);
		}

		if (cinema == null) {
			response.put("message", "There are no Sessions for Movie with ID: " + movie_id + " in Cinema " + cinema_id);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Cinema>(cinema, HttpStatus.OK);
	}

	@PostMapping("/cinemas")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@Valid @RequestBody Cinema cinema, BindingResult result) {
		Cinema cinemaNew = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(err -> {
				return "Field '" + err.getField() + "' " + err.getDefaultMessage();
			}).collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			cinemaNew = cinemaService.save(cinema);
		} catch (DataAccessException e) {
			return dataAccessErrorResponse(e);
		}

		response.put("message", "Cinema created successfully!");
		response.put("cinema", cinemaNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/cinemas/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Cinema cinema, BindingResult result) {
		// TODO
		return null;
	}

	/**
	 * Delete one Cinema with the id
	 */
	@DeleteMapping("/cinemas/{id}")
	public ResponseEntity<?> deleteOne(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		try {
			cinemaService.delete(id);
		} catch (DataAccessException e) {
			return dataAccessErrorResponse(e);
		}

		response.put("message", "Cinema deleted successfully!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	/**
	 * Delete All Cinemas from DB
	 */
	@DeleteMapping("/cinemas")
	public ResponseEntity<?> deleteAll() {
		Map<String, Object> response = new HashMap<>();

		try {
			cinemaService.deleteAll();
		} catch (DataAccessException e) {
			return dataAccessErrorResponse(e);
		}

		response.put("message", "All Cinemas were deleted successfully!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	private ResponseEntity<Map<String, Object>> dataAccessErrorResponse(DataAccessException error) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", "Error when querying the database");
		response.put("error", error.getMessage() + ": " + error.getMostSpecificCause().getMessage());
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
