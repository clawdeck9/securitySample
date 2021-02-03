package com.cluster9.securitySample.Entities;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

@Entity
public class AppUser {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String password;
	private String name;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Collection<AppRole> roles = new ArrayList<AppRole>();

	public AppUser() {
	}
	public AppUser(String name, String password) {
		this.password = password;
		this.name = name;
	}
	public AppUser(String password, String name, Collection<AppRole> roles) {
		this.password = password;
		this.name = name;
		this.roles = roles;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection<AppRole> getRoles() {
		return roles;
	}

	public void setRoles(Collection<AppRole> roles) {
		this.roles = roles;
	}
}
