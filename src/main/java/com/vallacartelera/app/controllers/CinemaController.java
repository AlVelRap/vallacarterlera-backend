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
import com.vallacartelera.app.models.Cinema;
import com.vallacartelera.app.services.ICinemaService;
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
	@Operation(description = "Returns a List of all Cinemas in DB. Without List of Sessions.", summary = "List all Cinemas. Without Session's List", responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = Cinema.class))) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@GetMapping(path = "/cinemas", produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView({ Views.GetAllCinemas.class })
	public ResponseEntity<?> showAll() {
		return new ResponseEntity<List<Cinema>>(cinemaService.findAll(), HttpStatus.OK);
	}

	/**
	 * Return data from one Cinema, with Sessions
	 */
	@Operation(description = "Returns a Cinema. With List of Sessions.", summary = "Show One Cinema. With Session's List.", responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Cinema.class)) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@GetMapping(path = "/cinemas/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView({ Views.GetCinema.class })
	public ResponseEntity<?> showCinema(@PathVariable Long id) {
		return new ResponseEntity<Cinema>(cinemaService.findById(id), HttpStatus.OK);
	}

	/**
	 * Return a List of Cinemas that have One Movie. Without Sessions
	 */
	@Operation(description = "Returns a list of all Cinemas for which a Movie has Sessions. Without List of Sessions.", summary = "List all Cinemas for Movie. Without Session's List", responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = Cinema.class))) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@GetMapping(path = "/movies/{id}/cinemas", produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView({ Views.GetAllCinemas.class })
	public ResponseEntity<?> showAllCinemasForMovie(@PathVariable Long id) {
		return new ResponseEntity<List<Cinema>>(cinemaService.findAllByMovieId(id), HttpStatus.OK);
	}

	/**
	 * Return a Cinema that have a Movie. With Sessions
	 */
	@Operation(description = "Returns one Cinema for which a Movie has Sessions. With List of Sessions.", summary = "Show One Cinema for Movie. With Session's List.", responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Cinema.class)) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@GetMapping(path = "/movies/{movie_id}/cinemas/{cinema_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView({ Views.GetOneCinemasForMovie.class })
	public ResponseEntity<?> showAllSessionsForCinemaForMovie(@PathVariable Long cinema_id,
			@PathVariable Long movie_id) {
		return new ResponseEntity<Cinema>(cinemaService.findByMovieIdAndCinemaId(cinema_id, movie_id), HttpStatus.OK);
	}

	@Operation(description = "Creates a Cinema.", summary = "Create Cinema.", responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Cinema.class)) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@PostMapping(path = "/cinemas", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> create(@Valid @JsonView(Views.GetAllCinemas.class) @RequestBody Cinema cinema,
			BindingResult result) {
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(err -> {
				return "Field '" + err.getField() + "' " + err.getDefaultMessage();
			}).collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		response.put("message", "Cinema created successfully!");
		response.put("cinema", cinemaService.save(cinema));
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@Operation(description = "Update a Cinema with its Id.", summary = "Update Cinema.", responses = {
			@ApiResponse(responseCode = "200", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Cinema.class)) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@JsonView({ Views.GetAllCinemas.class })
	@PutMapping(path = "/cinemas/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@PathVariable Long id,
			@Valid @JsonView(Views.GetAllCinemas.class) @RequestBody Cinema cinema, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(err -> {
				return "Field '" + err.getField() + "' " + err.getDefaultMessage();
			}).collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		Cinema cinemaActual = cinemaService.findById(id);

		cinemaActual.setLocation(cinema.getLocation());
		cinemaActual.setName(cinema.getName());
		cinemaActual.setPhone(cinema.getPhone());

		response.put("message", "Cinema with ID:" + id + " was updated succesfully!");
		response.put("cinema", cinemaService.save(cinemaActual));

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	/**
	 * Delete one Cinema with the id
	 */
	@Operation(description = "Delete a Cinema from DB.", summary = "Delete Cinema.", responses = {
			@ApiResponse(responseCode = "200", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Cinema.class)) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@DeleteMapping(path = "/cinemas/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteOne(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		cinemaService.delete(id);

		response.put("message", "Cinema deleted successfully!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	/**
	 * Delete All Cinemas from DB
	 */
	@Operation(description = "Delete all Cinemas from DB.", summary = "Delete All Cinemas.", responses = {
			@ApiResponse(responseCode = "200", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Cinema.class)) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@DeleteMapping(path = "/cinemas", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteAll() {
		Map<String, Object> response = new HashMap<>();

		cinemaService.deleteAll();

		response.put("message", "All Cinemas were deleted successfully!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

}
