package com.vallacartelera.app.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonView;
import com.vallacartelera.app.views.Views;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "movies")
@JsonView({ Views.GetSession.class, Views.GetMovie.class, Views.GetAllMovies.class, Views.GetOneMovieForCinema.class })
public class Movie implements Serializable {

	@Id
	@JsonView({ Views.GetSession.class, Views.GetMovie.class, Views.GetAllMovies.class, Views.GetCinema.class,
			Views.GetOneMovieForCinema.class, Views.GetActor.class })
	private Long id;
	@NotEmpty
	@JsonView({ Views.GetMovie.class, Views.GetAllMovies.class, Views.GetCinema.class, Views.GetSession.class,
			Views.GetActor.class })
	private String title;
	private Integer year;
	private String director;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "movies_genres", joinColumns = { @JoinColumn(name = "movie_id") }, inverseJoinColumns = {
			@JoinColumn(name = "genre_id") })
	private Set<Genre> genre;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "movies_actors", joinColumns = { @JoinColumn(name = "movie_id") }, inverseJoinColumns = {
			@JoinColumn(name = "actor_id") })
	private Set<Actor> cast;

	@NotEmpty
	@Column(name = "age_rating")
	private String ageRating;
	@NotEmpty
	@Column(name = "age_rating_img")
	private String ageRatingImg;
	@NotEmpty
	private String img;
	@Lob
	@NotEmpty
	@Column(length = 5000)
	private String description;

	@OneToMany(mappedBy = "movie", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonView({ Views.GetMovie.class, Views.GetOneMovieForCinema.class })
	private List<Session> sessions;

	public Movie() {
		this.sessions = new ArrayList<Session>();
		this.cast = new HashSet<Actor>();
		this.genre = new HashSet<Genre>();
	}

	public void addActor(Actor actor) {
		this.cast.add(actor);
		actor.getMovies().add(this);
	}

	public void removeActor(Long actorId) {
		Actor actor = this.cast.stream().filter(act -> act.getId() == actorId).findFirst().orElse(null);
		if (actor != null) {
			this.cast.remove(actor);
			actor.getMovies().remove(this);
		}
	}

	public void addGenre(Genre genereAdd) {
		this.genre.add(genereAdd);
		genereAdd.getMovies().add(this);
	}

	public void removeGenre(Long genereId) {
		Genre genere = this.genre.stream().filter(gen -> gen.getId() == genereId).findFirst().orElse(null);
		if (genere != null) {
			this.genre.remove(genere);
			genere.getMovies().remove(this);
		}
	}

	// Getters and Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public Set<Actor> getCast() {
		return cast;
	}

	public void setCast(Set<Actor> cast) {
		this.cast = cast;
	}

	public String getAgeRating() {
		return ageRating;
	}

	public void setAgeRating(String ageRating) {
		this.ageRating = ageRating;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Session> getSessions() {
		return sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}

	public Set<Genre> getGenre() {
		return genre;
	}

	public void setGenre(Set<Genre> genre) {
		this.genre = genre;
	}

	public String getAgeRatingImg() {
		return ageRatingImg;
	}

	public void setAgeRatingImg(String ageRatingImg) {
		this.ageRatingImg = ageRatingImg;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
