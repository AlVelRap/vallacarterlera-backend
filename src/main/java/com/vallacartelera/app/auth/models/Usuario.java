package com.vallacartelera.app.auth.models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonView;
import com.vallacartelera.app.views.Views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class Usuario implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 30, unique = true)
	@JsonView({ Views.Auth.class })
	@NotNull(groups= {Views.Auth.class})
	private String username;

	@NotNull(groups= {Views.Auth.class})
	@Column(length = 100)
	@JsonView({ Views.Auth.class })
	private String password;

	@NotNull
	@Column(length = 30)
	private String salt;

	private boolean enabled;

	public Usuario() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
