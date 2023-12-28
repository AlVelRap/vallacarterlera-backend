package com.vallacartelera.app.restcontrollers;

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
import com.vallacartelera.app.models.Genre;
import com.vallacartelera.app.services.IGenreService;
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
@Tag(name = "Genre")
public class GenreRestController {

	// TODO:
	// - Check all errors and returns, and optimize
	// - Review response of: POST methods

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private IGenreService genreService;

	/**
	 * Returns a List of all Genres in DB. Without List of Movies.
	 */

	@Operation(description = "Returns a List of all Genres in DB. Without List of Movies.", summary = "List all Genres. Without Movie's List", responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = Genre.class))) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@GetMapping(path = "/genres", produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView({ Views.GetGenre.class })
	public ResponseEntity<?> showAll() {
		return new ResponseEntity<List<Genre>>(genreService.findAll(), HttpStatus.OK);
	}

	/**
	 * Returns a Genre. With List of Movies.
	 */

	@Operation(description = "Returns a Genre. With List of Movies.", summary = "Show One Genre. With Movie's List.", responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Genre.class)) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@GetMapping(path = "/genres/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView({ Views.GetGenre.class })
	public ResponseEntity<?> showOne(@PathVariable(value = "id") Long id) {
		return new ResponseEntity<Genre>(genreService.findById(id), HttpStatus.OK);
	}

	/**
	 * This method create a Genre
	 */

	@Operation(description = "Creates a Genre without connecting it to any movie.", summary = "Create an Genre.", responses = {
			@ApiResponse(responseCode = "200", description = "Success", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Genre.class)) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@PostMapping(path = "/genres", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
	 * Add a genre to a movie. With the name of the genre or with the Id. If Name
	 * or Id already exists in DB will add it to the movie, if not, this method will
	 * create and add to movie that Genre.
	 */

	@Operation(description = "Add a Genre to a movie. With the name of the genre or with the Id. If Name or Id already exists in DB will add it to the movie, if not, this method will create and add to movie that Genre.", summary = "Add Genre to Movie.", responses = {
			@ApiResponse(responseCode = "201", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Genre.class)) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@PostMapping(path = "/movies/{id}/genres", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView({ Views.PostGenre.class })
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createMovieGenre(@PathVariable(value = "id") Long movieId, @RequestBody Genre genre) {
		Map<String, Object> response = new HashMap<>();

		response.put("message", "Genre added to Movie with ID: " + movieId + " successfully!");
		response.put("genre", genreService.addGenreToMovie(movieId, genre));
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@Operation(description = "Update an Genre with its Id.", summary = "Update Genre.", responses = {
			@ApiResponse(responseCode = "200", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Genre.class)) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@PutMapping(path = "/genres/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@JsonView({Views.PostGenre.class})
	public ResponseEntity<?> update(@PathVariable Long id,@Valid @JsonView({Views.PostGenre.class}) @RequestBody Genre genre, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(err -> {
				return "Field '" + err.getField() + "' " + err.getDefaultMessage();
			}).collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		Genre genreActual = genreService.findById(id);

		genreActual.setName(genre.getName());

		response.put("message", "Genre with ID:" + id + " was updated succesfully!");
		response.put("session", genreService.save(genreActual));

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	/**
	 * Delete an Genre from DB.
	 */
	@Operation(description = "Delete an Genre from DB.", summary = "Delete Genre.", responses = {
			@ApiResponse(responseCode = "200", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Genre.class)) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@DeleteMapping(path = "/genres/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		genreService.delete(id);

		response.put("message", "Genre deleted successfully!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	/**
	 * Delete an Genre from a movie, but not delete Genre from DB.
	 */

	@Operation(description = "Delete an Genre from a movie, but not delete Genre from DB.", summary = "Delete Genre from Movie.", responses = {
			@ApiResponse(responseCode = "200", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Genre.class)) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@DeleteMapping(path = "movies/{movieId}/genres/{genreId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteGenreFromMovie(@PathVariable("movieId") Long movieId,
			@PathVariable("genreId") Long genreId) {
		Map<String, Object> response = new HashMap<>();

		genreService.deleteGenreFromMovie(movieId, genreId);

		response.put("message",
				"Genre with ID: " + genreId + " deleted successfully from Movie with ID: " + movieId + "!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}


	@Operation(description = "Delete all Genres from DB.", summary = "Delete All Genres.", responses = {
			@ApiResponse(responseCode = "200", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Genre.class)) }),
			@ApiResponse(responseCode = "500", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }),
			@ApiResponse(responseCode = "404", content = {
					@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorMessage.class)) }) })
	@DeleteMapping(path = "/genres", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteAll() {
		Map<String, Object> response = new HashMap<>();

		genreService.deleteAll();

		response.put("message", "All Genres were deleted successfully!");

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

}
