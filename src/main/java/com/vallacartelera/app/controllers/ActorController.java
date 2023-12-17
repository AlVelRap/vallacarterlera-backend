package com.vallacartelera.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.vallacartelera.app.errors.models.ErrorMessage;
import com.vallacartelera.app.models.Actor;
import com.vallacartelera.app.services.IActorService;
import com.vallacartelera.app.views.Views;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private IActorService actorService;

	@Operation(description = "Returns a List of all Actors in DB. Without List of Movies.", summary = "List all Actors. Without Movie's List", responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = Actor.class))) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@JsonView({ Views.GetAllActor.class })
	@GetMapping(path = "/actors", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> showAll() {
		return new ResponseEntity<List<Actor>>(actorService.findAll(), HttpStatus.OK);
	}

	@Operation(description = "Returns an Actor. With List of Movies.", summary = "Show One Actor. With Movie's List.", responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Actor.class)) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@GetMapping(path = "/actors/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView({ Views.GetActor.class })
	public ResponseEntity<?> showOne(@PathVariable(value = "id") Long id) {
		return new ResponseEntity<Actor>(actorService.findById(id), HttpStatus.OK);
	}

	/**
	 * This method create an Actor
	 */
	@Operation(description = "Creates an Actor without connecting it to any movie.", summary = "Create an Actor.", responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Actor.class)) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@PostMapping(path = "/actors", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@Valid @JsonView(Views.GetAllActor.class) @RequestBody Actor actor,
			BindingResult result) {
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(err -> {
				return "Field '" + err.getField() + "' " + err.getDefaultMessage();
			}).collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		response.put("message", "Actor created successfully!");
		response.put("actor", actorService.save(actor));
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	/**
	 * Add an actor to a movie. With the name of the actor or with the Id. If Name
	 * or Id already exists in DB will add it to the movie, if not, this method will
	 * create and add to movie that Actor.
	 */
	@Operation(description = "Add an actor to a movie. With the name of the actor or with the Id. If Name or Id already exists in DB will add it to the movie, if not, this method will create and add to movie that Actor.", summary = "Add Actor to Movie.", responses = {
			@ApiResponse(responseCode = "201", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Actor.class)) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@PostMapping(path = "/movies/{id}/actors", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView({ Views.PostActor.class })
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createMovieActor(@PathVariable(value = "id") Long movieId,
			@JsonView(Views.GetAllActor.class) @RequestBody Actor actor) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", "Actor added to Movie with ID: " + movieId + " successfully!");
		response.put("actor", actorService.addActorToMovie(movieId, actor));
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@Operation(description = "Update an Actor with its Id.", summary = "Update Actor.", responses = {
			@ApiResponse(responseCode = "200", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Actor.class)) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@PutMapping(path = "/actors/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView(Views.GetAllActor.class)
	public ResponseEntity<?> update(@PathVariable Long id,
			@Valid @JsonView(Views.GetAllActor.class) @RequestBody Actor actor, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(err -> {
				return "Field '" + err.getField() + "' " + err.getDefaultMessage();
			}).collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		Actor actorActual = actorService.findById(id);

		actorActual.setName(actor.getName());

		response.put("message", "Actor with ID:" + id + " was updated succesfully!");
		response.put("session", actorService.save(actorActual));

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	/**
	 * Delete an Actor from DB.
	 */
	@Operation(description = "Delete an Actor from DB.", summary = "Delete Actor.", responses = {
			@ApiResponse(responseCode = "200", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Actor.class)) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@DeleteMapping("/actors/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		actorService.delete(id);
		response.put("message", "Actor deleted successfully!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	/**
	 * Delete an actor from a movie, but not delete Actor from DB.
	 */
	@Operation(description = "Delete an actor from a movie, but not delete Actor from DB.", summary = "Delete Actor from Movie.", responses = {
			@ApiResponse(responseCode = "200", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Actor.class)) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@DeleteMapping("movies/{movieId}/actors/{actorId}")
	public ResponseEntity<?> deleteActorFromMovie(@PathVariable("movieId") Long movieId,
			@PathVariable("actorId") Long actorId) {
		Map<String, Object> response = new HashMap<>();
		actorService.deleteActorFromMovie(movieId, actorId);
		response.put("message",
				"Actor with ID: " + actorId + " deleted successfully from Movie with ID: " + movieId + "!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@Operation(description = "Delete all Actors from DB.", summary = "Delete All Actors.", responses = {
			@ApiResponse(responseCode = "200", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Actor.class)) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@DeleteMapping("/actors")
	public ResponseEntity<?> deleteAll() {
		Map<String, Object> response = new HashMap<>();
		actorService.deleteAll();
		response.put("message", "All Actors were deleted successfully!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

}
