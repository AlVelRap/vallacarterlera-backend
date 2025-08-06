package com.vallacartelera.app.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vallacartelera.app.auth.models.Usuario;

@Repository
public interface IUserDao extends JpaRepository<Usuario, Long>{
	public Usuario findByUsername(String username);
}
