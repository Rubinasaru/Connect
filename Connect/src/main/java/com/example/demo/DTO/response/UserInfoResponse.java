package com.example.demo.DTO.response;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;
import lombok.NoArgsConstructor;

@Hidden
@Data
@NoArgsConstructor
public class UserInfoResponse {
    private String jwtToken;
    private String username;

    public UserInfoResponse(String jwtToken, String username) {
        this.jwtToken = jwtToken;
        this.username = username;
    }
}
