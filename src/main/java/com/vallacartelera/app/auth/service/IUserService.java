package com.vallacartelera.app.auth.service;

import org.springframework.http.ResponseEntity;

import com.vallacartelera.app.auth.models.Usuario;

public interface IUserService {
	public Usuario register(Usuario user);
	
	public ResponseEntity<?> login(Usuario user);
}
