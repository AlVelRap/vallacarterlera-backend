package com.vallacartelera.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vallacartelera.app.models.Session;
@Repository
public interface ISessionDao extends JpaRepository<Session, Long> {

}
