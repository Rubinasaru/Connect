package com.example.demo.DTO.response;

import com.example.demo.Models.User;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Hidden
public class RegisterResponseDTO {
    private Long id;
    private String Email;

	private String Username;
    private String Password;

    public RegisterResponseDTO(User user) {
        this.id = user.getId();
        this.Email = user.getEmail();
		this.Username= user.getUsername();
        this.Password = user.getPassword();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}
    
    
}