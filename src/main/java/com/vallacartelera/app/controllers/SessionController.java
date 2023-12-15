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
import com.vallacartelera.app.models.Session;
import com.vallacartelera.app.services.ISessionService;
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
@Tag(name = "Session")
public class SessionController {

	// TODO:
	// - Finish PUT method
	// - Finish showSessionsForMovie and showSessionsForCinema methods

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private ISessionService sessionService;

	/**
	 * Returns All Sessions.
	 */
	@Operation(description = "Returns a List of all Sessions in DB.", summary = "List all Sessions", responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = Session.class))) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@GetMapping(path = "/sessions", produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView({ Views.GetSession.class })
	public ResponseEntity<?> showAllSessions() {
		return new ResponseEntity<List<Session>>(sessionService.findAll(), HttpStatus.OK);
	}

	@Operation(description = "Returns a Session.", summary = "Show One Session", responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Session.class)) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@GetMapping(path = "/sessions/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView({ Views.GetSession.class })
	public ResponseEntity<?> showOneSession(@PathVariable Long id) {
		return new ResponseEntity<Session>(sessionService.findById(id), HttpStatus.OK);
	}

	@Operation(description = "Returns a List of all Sessions for a Movie.", summary = "List all Sessions for Movie", responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = Session.class))) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@GetMapping(path = "/sessions/movies/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView({ Views.GetSessionForMovie.class })
	public ResponseEntity<?> showSessionsForMovie(@PathVariable Long id) {
		return new ResponseEntity<List<Session>>(sessionService.findByMovieId(id), HttpStatus.OK);
	}

	@Operation(description = "Returns a List of all Sessions for a Cinema.", summary = "List all Sessions for Cinema", responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = Session.class))) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@GetMapping(path = "/sessions/cinemas/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView({ Views.GetSessionForCinema.class })
	public ResponseEntity<?> showSessionsForCinema(@PathVariable Long id) {
		return new ResponseEntity<List<Session>>(sessionService.findByCinemaId(id), HttpStatus.OK);
	}

	@Operation(description = "Creates a Session.", summary = "Create Session", responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Session.class)) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@PostMapping(path = "/sessions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@Valid @RequestBody Session session, BindingResult result) {
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(err -> {
				return "Field '" + err.getField() + "' " + err.getDefaultMessage();
			}).collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		response.put("message", "Session created successfully!");
		response.put("session", sessionService.save(session));
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@Operation(description = "Update a Session with its Id.", summary = "Update Session", responses = {
			@ApiResponse(responseCode = "200", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Session.class)) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@PutMapping(path = "/sessions/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@Valid @RequestBody Session session, BindingResult result) {
		// TODO
		return null;
	}

	@Operation(description = "Delete a Session from DB.", summary = "Delete Session", responses = {
			@ApiResponse(responseCode = "200", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Session.class)) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@DeleteMapping(path = "/sessions/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		sessionService.delete(id);

		response.put("message", "Session deleted successfully!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@Operation(description = "Delete all Sessions from DB.", summary = "Delete All Sessions", responses = {
			@ApiResponse(responseCode = "200", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Session.class)) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@DeleteMapping(path = "/sessions", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteAll() {
		Map<String, Object> response = new HashMap<>();

		sessionService.deleteAll();

		response.put("message", "All Sessions were deleted successfully!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

}
