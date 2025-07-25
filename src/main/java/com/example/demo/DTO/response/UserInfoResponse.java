package com.example.demo.DTO.response;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;
import lombok.NoArgsConstructor;

@Hidden
@Data
public class UserInfoResponse {
    private String jwtToken;
    private String username;

    public UserInfoResponse(String jwtToken, String username) {
        this.jwtToken = jwtToken;
        this.username = username;
    }

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
    
    
    
}
