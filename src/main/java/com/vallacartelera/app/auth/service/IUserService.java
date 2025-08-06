package com.vallacartelera.app.auth.service;

import com.vallacartelera.app.auth.models.Usuario;

public interface IUserService {
	public Usuario register(Usuario user);
	
	public String login(Usuario user);
}
