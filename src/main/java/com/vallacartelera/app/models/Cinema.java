package com.vallacartelera.app.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.vallacartelera.app.views.Views;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "cinemas")
@JsonView({ Views.GetSession.class, Views.GetCinema.class, Views.GetAllCinemas.class,
		Views.GetOneCinemasForMovie.class })
public class Cinema implements Serializable {

	@Id
	@JsonView({ Views.GetSession.class, Views.GetCinema.class, Views.GetMovie.class, Views.GetAllCinemas.class,
			Views.GetOneCinemasForMovie.class, Views.GetSessionForMovie.class })
	private Long id;

	@NotEmpty
	@Column(unique = true)
	private String name;
	private String phone;
	private String location;

	@OneToMany(mappedBy = "cinema", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonView({ Views.GetCinema.class, Views.GetOneCinemasForMovie.class })
	private List<Session> sessions;

	public Cinema() {
		this.sessions = new ArrayList<Session>();
	}

	public List<Session> getSessions() {
		return sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}

	public void addSession(Session session) {
		this.sessions.add(session);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
