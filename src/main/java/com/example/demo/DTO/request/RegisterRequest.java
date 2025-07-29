package com.example.demo.DTO.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

	@Column(unique = true,nullable = false)
	@JsonProperty("Email")
    private String email;

	@Column(unique = true,nullable = false)
	@JsonProperty("Username")
	private String username;
	@JsonProperty("Password")
    private String password;
	
//	@Enumerated(EnumType.STRING)
//    private AuthProvider provider;
//   
//	public AuthProvider getProvider() {
//		return provider;
//	}
//	public void setProvider(AuthProvider provider) {
//		this.provider = provider;
//	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
}
