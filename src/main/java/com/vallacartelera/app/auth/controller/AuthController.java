package com.vallacartelera.app.auth.controller;

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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.vallacartelera.app.auth.models.Usuario;
import com.vallacartelera.app.auth.service.IUserService;
import com.vallacartelera.app.views.Views;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = "/api")
@Tag(name = "Usuario")
public class AuthController {
	// TODO:
	// - Check all errors and returns, and optimize

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private IUserService userService;

	@PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> register(
			@Validated(Views.Auth.class) @JsonView(Views.Auth.class) @RequestBody Usuario user,
			BindingResult result) {
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(err -> {
				return "Field '" + err.getField() + "' " + err.getDefaultMessage();
			}).collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		response.put("message", "User created successfully!");
		response.put("user", userService.register(user));
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> login(
			@Validated(Views.Auth.class) @JsonView(Views.Auth.class) @RequestBody Usuario user,
			BindingResult result) {
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(err -> {
				return "Field '" + err.getField() + "' " + err.getDefaultMessage();
			}).collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		return userService.login(user);
	}
}
