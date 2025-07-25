package com.example.demo.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.DTO.request.ChangePasswordDTO;
import com.example.demo.DTO.request.ProfileSetupRequest;
import com.example.demo.DTO.request.UserDTO;
import com.example.demo.Enums.UserType;
import com.example.demo.Models.UserProfile;

import jakarta.validation.Valid;

@Service
public interface UserService {
		
	    ChangePasswordDTO changePassword(ChangePasswordDTO changePasswordRequestDTO);
	    
	   UserProfile setupProfile(String username, ProfileSetupRequest request);

//	    List<UserDTO> getAllUsers();

		UserDTO getUserById(Long id);

		boolean deleteUser(Long id);

		UserDTO updateUserDetails(Long userId, @Valid UserDTO userDTO);

		UserDTO updateUserRoles(Long userId, UserType roles);

		UserDTO updateUserProfileImage(Long userId,String filePath, MultipartFile file);
}
