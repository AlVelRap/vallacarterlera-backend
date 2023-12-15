package com.vallacartelera.app.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.vallacartelera.app.views.Views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "sessions")
@JsonView({ Views.GetSession.class, Views.GetSessionForCinema.class, Views.GetSessionForMovie.class,
		Views.GetCinema.class, Views.GetMovie.class, Views.GetOneMovieForCinema.class,
		Views.GetOneCinemasForMovie.class })
public class Session implements Serializable {

	// TODO:
	// - Review field SessionDate and its format

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cinema_id")
	@JsonView({ Views.GetSession.class, Views.GetMovie.class, Views.GetSessionForMovie.class })
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Cinema cinema;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "movie_id")
	@JsonView({ Views.GetSession.class, Views.GetCinema.class, Views.GetSessionForCinema.class })
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Movie movie;

	@NotNull
	@Column(name = "late_session")
	private Boolean lateSession = false;

	@Temporal(TemporalType.TIMESTAMP)
	// Reactivate in the future
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "Europe/Madrid")
	@Column(name = "session_date")
	private LocalDateTime sessionDate; // Check this LocalDateTime

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

//	public String getHour() {
//		return hour;
//	}
//
//	public void setHour(String hour) {
//		this.hour = hour;
//	}

	public LocalDateTime getSessionDate() {
		return sessionDate;
	}

	public void setSessionDate(LocalDateTime sessionDate) {
		this.sessionDate = sessionDate;
	}

	public Boolean getLateSession() {
		return lateSession;
	}

	public void setLateSession(Boolean lateSession) {
		this.lateSession = lateSession;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
