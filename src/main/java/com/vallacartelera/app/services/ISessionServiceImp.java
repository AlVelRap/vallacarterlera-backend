package com.vallacartelera.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vallacartelera.app.dao.ISessionDao;
import com.vallacartelera.app.models.Session;

@Service
public class ISessionServiceImp implements ISessionService {

	@Autowired
	private ISessionDao sessionDao;

	@Override
	@Transactional(readOnly = true)
	public List<Session> findAll() {
		return (List<Session>) sessionDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Session findById(Long id) {
		return sessionDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Session save(Session session) {
		return sessionDao.save(session);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		sessionDao.deleteById(id);
	}

	@Override
	@Transactional
	public void deleteAll() {
		sessionDao.deleteAll();
	}

}
