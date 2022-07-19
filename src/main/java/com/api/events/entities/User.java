package com.api.events.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "users")
public class User extends AuditModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty
	private long id;
	
	@Column(nullable = false, unique = true)
	@JsonProperty
	private String username;


	//addition

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(nullable = false)
	private String password;

	@OneToMany(targetEntity = Event.class,cascade =CascadeType.ALL)
	@JoinColumn(name = "userEvents_fk", referencedColumnName = "id")
	private List<Event> events;




	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}



	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}






}
