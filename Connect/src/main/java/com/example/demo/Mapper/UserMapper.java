package com.example.demo.Mapper;

import org.springframework.stereotype.Component;

import com.example.demo.DTO.request.UserDTO;
import com.example.demo.Models.User;

@Component
public class UserMapper {

	 public UserDTO toDTO(User user) {
	        return new UserDTO(
	            user.getId(),
	            user.getUsername(),
	            user.getProfile().getEmail(),
	            user.getProfile() != null ? user.getProfile().getProfileImgUrl() : null
	        );
	    }
}
