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
import com.vallacartelera.app.models.Session;

import com.vallacartelera.app.services.ISessionService;
import com.vallacartelera.app.views.Views;

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
	 *  Returns All Sessions.
	 * */
	@GetMapping("/sessions")
	@JsonView({ Views.GetSession.class })
	public ResponseEntity<?> showAllSessions() {
		List<Session> session = null;
		Map<String, Object> response = new HashMap<>();

		try {
			session = sessionService.findAll();
		} catch (DataAccessException e) {
			return dataAccessErrorResponse(e);
		}

		if (session == null) {
			response.put("message", "There are no Sessions in DB");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<List<Session>>(session, HttpStatus.OK);
	}

	@GetMapping("/sessions/{id}")
	@JsonView({ Views.GetSession.class })
	public ResponseEntity<?> showOneSession(@PathVariable Long id) {
		Session session = null;
		Map<String, Object> response = new HashMap<>();

		try {
			session = sessionService.findById(id);
		} catch (DataAccessException e) {
			return dataAccessErrorResponse(e);
		}

		if (session == null) {
			response.put("message", "Session with ID: ".concat(id.toString().concat(" doesn´t exists in the DB!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Session>(session, HttpStatus.OK);
	}
	
	@GetMapping("/sessions/movies/{id}")
	@JsonView({ Views.GetSession.class })
	public ResponseEntity<?> showSessionsForMovie(@PathVariable Long id) {
		// TODO
		return null;
	}
	@GetMapping("/sessions/cinemas/{id}")
	@JsonView({ Views.GetSession.class })
	public ResponseEntity<?> showSessionsForCinema(@PathVariable Long id) {
		// TODO
		return null;
	}

	@PostMapping("/sessions")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@Valid @RequestBody Session session, BindingResult result) {
		Session sessionNew = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(err -> {
				return "Field '" + err.getField() + "' " + err.getDefaultMessage();
			}).collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			sessionNew = sessionService.save(session);
		} catch (DataAccessException e) {
			return dataAccessErrorResponse(e);
		}

		response.put("message", "Session created successfully!");
		response.put("session", sessionNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/sessions/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Session session, BindingResult result) {
		// TODO
		return null;
	}
	
	@DeleteMapping("/sessions/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {

		Map<String, Object> response = new HashMap<>();

		try {
			sessionService.delete(id);
		} catch (DataAccessException e) {
			return dataAccessErrorResponse(e);
		}

		response.put("message", "Session deleted successfully!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/sessions")
	public ResponseEntity<?> deleteAll() {

		Map<String, Object> response = new HashMap<>();

		try {
			sessionService.deleteAll();
		} catch (DataAccessException e) {
			return dataAccessErrorResponse(e);
		}

		response.put("message", "All Sessions were deleted successfully!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	private ResponseEntity<Map<String, Object>> dataAccessErrorResponse(DataAccessException error) {
		Map<String, Object> response = new HashMap<>();
		response.put("message", "Error when querying the database");
		response.put("error", error.getMessage() + ": " + error.getMostSpecificCause().getMessage());
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
