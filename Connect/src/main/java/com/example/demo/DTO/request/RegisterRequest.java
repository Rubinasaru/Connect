package com.example.demo.DTO.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {
	 @NotNull(message = "Username cannot be null")
	    @NotBlank(message = "Username cannot be blank")
	    @JsonProperty("Username")
	    private String username;

	    @NotNull(message = "Password cannot be null")
	    @NotBlank(message = "Password cannot be blank")
	    @JsonProperty("Password")
	    private String password;

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
