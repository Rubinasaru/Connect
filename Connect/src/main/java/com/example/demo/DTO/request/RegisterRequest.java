package com.example.demo.DTO.request;

import com.example.demo.Enums.AuthProvider;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class RegisterRequest {
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
