package com.example.demo.DTO.response;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class AuthenticationResponse {
	@Getter
	@Setter
    private String token;
    
    public AuthenticationResponse(String token) {
        this.token = token;
    }
}
