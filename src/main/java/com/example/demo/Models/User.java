package com.example.demo.Models;


import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;


@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
public class User {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true)
	@JsonProperty("Username")
	private String username;

	@Column(nullable = false)
	@JsonProperty("Password")
	private String password;

//    @Enumerated(EnumType.STRING)
//    private AuthProvider provider;

	private Boolean profileCompleted = false;

	// One-to-one relationship with UserProfile
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private UserProfile profile;

	// Timestamp
	private LocalDate createdAt = LocalDate.now();

	public User(String username, String password) {     //,AuthProvider provider
		this.username = username;
		this.password = password;
//        this.provider=provider;
	}

	public void build() {

	}

	public User() {

	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
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

//	 public AuthProvider getProvider() {
//			return provider;
//	}
//
//	public void setProvider(AuthProvider provider) {
//			this.provider = provider;
//	}

	public Boolean getProfileCompleted() {
		return profileCompleted;
	}
	public void setProfileCompleted(Boolean profileCompleted) {
		this.profileCompleted = profileCompleted;
	}
	public UserProfile getProfile() {
		return profile;
	}
	public void setProfile(UserProfile profile) {
		this.profile = profile;
	}
	public LocalDate getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

}