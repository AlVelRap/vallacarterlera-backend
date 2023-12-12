package com.vallacartelera.app.services;

import java.util.List;

import com.vallacartelera.app.models.Session;

public interface ISessionService {
	public List<Session> findAll();

	public Session findById(Long id);

	public Session save(Session session);

	public void delete(Long id);
	
	public void deleteAll();
}
