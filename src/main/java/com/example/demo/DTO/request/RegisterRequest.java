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

	@Column(unique = true)
	@Size(min = 3, max = 20, message = "Username must be 3–20 characters")
	@JsonProperty("Username")
    private String username;
	@JsonProperty("Password")
	@Pattern(
			regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
			message = "Password must be at least 8 characters and include a number, an uppercase, a lowercase, and a special character"
	)
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
