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
import org.springframework.http.MediaType;
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
import com.vallacartelera.app.services.IActorService;
import com.vallacartelera.app.views.Views;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

//@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
@Tag(name = "Actor")
public class ActorController {

	// TODO:
	// - Check all errors and returns, and optimize
	// - Review response of: POST methods
	// - Swagger Annotations

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private IActorService actorService;

	@Operation(
			description = "Returns a List of all Actors in DB. Without List of Movies.",
			summary = "List all Actors. Without Movie's List",
			responses = {
					@ApiResponse(
							responseCode = "200",
							description = "Success", 
							content = {
									@Content(
											mediaType = MediaType.APPLICATION_JSON_VALUE,
											schema = @Schema(implementation = Actor.class)
											)
									} 
							),
					@ApiResponse(responseCode = "500"),
					@ApiResponse(responseCode = "404")
			}
			)
	@JsonView({ Views.GetAllActor.class })
	@GetMapping(path = "/actors",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> showAll() {
		List<Actor> actors = null;
		Map<String, Object> response = new HashMap<>();

		try {
			actors = actorService.findAll();
		} catch (DataAccessException e) {
			return dataAccessErrorResponse(e);
		}

		if (actors == null) {
			response.put("message", "There are no Actors in DB");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<List<Actor>>(actors, HttpStatus.OK);
	}

	/**
	 * Returns a an Actors. With List of Movies.
	 */
	@Operation(
			description = "Returns an Actor. With List of Movies.",
			summary = "An Actor. With Movie's List.",
			responses = {
					@ApiResponse(responseCode = "200",description = "Success"),
					@ApiResponse(responseCode = "500"),
					@ApiResponse(responseCode = "404")
			})
	@GetMapping(path="/actors/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView({ Views.GetActor.class })
	public ResponseEntity<?> showOne(@PathVariable(value = "id") Long id) {
		return new ResponseEntity<Actor>(actorService.findById(id), HttpStatus.OK);
	}

	/**
	 * This method create an Actor
	 */
	@PostMapping(path = "/actors", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@Valid @RequestBody Actor actor, BindingResult result) {
		Actor actorNew = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(err -> {
				return "Field '" + err.getField() + "' " + err.getDefaultMessage();
			}).collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			actorNew = actorService.save(actor);
		} catch (DataAccessException e) {
			return dataAccessErrorResponse(e);
		}

		response.put("message", "Actor created successfully!");
		response.put("actor", actorNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	/**
	 * Add an actor to a movie. With the name of the actor or with the Id. If Name
	 * or Id already exists in DB will add it to the movie, if not, this method will
	 * create and add to movie that Actor.
	 */
	@PostMapping("/movies/{id}/actors")
	@JsonView({ Views.PostActor.class })
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createMovieActor(@PathVariable(value = "id") Long movieId, @RequestBody Actor actor) {
		Actor actorNew = null;
		Map<String, Object> response = new HashMap<>();

		try {
			actorNew = actorService.addActorToMovie(movieId, actor);
		} catch (DataAccessException e) {
			return dataAccessErrorResponse(e);
		}

		response.put("message", "Actor added to Movie with ID: " + movieId + " successfully!");
		response.put("actor", actorNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/actors/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Actor actor, BindingResult result) {
		// TODO
		return null;
	}

	/**
	 * Delete an Actor from DB.
	 */
	@DeleteMapping("/actors/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {

		Map<String, Object> response = new HashMap<>();

		try {
			actorService.delete(id);
		} catch (DataAccessException e) {
			return dataAccessErrorResponse(e);
		}

		response.put("message", "Actor deleted successfully!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	/**
	 * Delete an actor from a movie, but not delete Actor from DB.
	 */
	@DeleteMapping("movies/{movieId}/actors/{actorId}")
	public ResponseEntity<?> deleteActorFromMovie(@PathVariable("movieId") Long movieId,
			@PathVariable("actorId") Long actorId) {

		Map<String, Object> response = new HashMap<>();

		try {
			actorService.deleteActorFromMovie(movieId, actorId);
		} catch (DataAccessException e) {
			return dataAccessErrorResponse(e);
		}

		response.put("message",
				"Actor with ID: " + actorId + " deleted successfully from Movie with ID: " + movieId + "!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/actors")
	public ResponseEntity<?> deleteAll() {

		Map<String, Object> response = new HashMap<>();

		try {
			actorService.deleteAll();
		} catch (DataAccessException e) {
			return dataAccessErrorResponse(e);
		}

		response.put("message", "All Actors were deleted successfully!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	private ResponseEntity<Map<String, Object>> dataAccessErrorResponse(DataAccessException error) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", "Error when querying the database");
		response.put("error", error.getMessage() + ": " + error.getMostSpecificCause().getMessage());
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
