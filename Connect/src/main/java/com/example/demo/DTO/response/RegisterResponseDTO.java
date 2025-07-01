package com.example.demo.DTO.response;

import com.example.demo.Models.User;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;

@Hidden
@Getter
@Setter
public class RegisterResponseDTO {
    private Long id;
    private String Username;
    private String Password;

    public RegisterResponseDTO(User user) {
        this.id = user.getId();
        this.Username = user.getUsername();
        this.Password = user.getPassword();
    }
}