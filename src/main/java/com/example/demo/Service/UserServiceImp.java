package com.example.demo.Service;


import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.DTO.request.ChangePasswordDTO;
import com.example.demo.DTO.request.ProfileSetupRequest;
import com.example.demo.DTO.request.UserDTO;
import com.example.demo.Enums.UserType;
import com.example.demo.Models.User;
import com.example.demo.Models.UserProfile;
import com.example.demo.Repository.ProfileRepository;
import com.example.demo.Repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileService FileService;

    @Override
    @Transactional
    public ChangePasswordDTO changePassword(ChangePasswordDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password does not match");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        return dto;
    }

    @Override
    public UserProfile setupProfile(String username, ProfileSetupRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getProfile() != null) {
            throw new IllegalStateException("Profile already exists for this user.");
        }
        UserProfile profile = new UserProfile();
        profile.setUser(user);
        profile.setDepartment(request.getDepartment());
        profile.setInterest(request.getInterest());
        profile.setRole(request.getRole());
        profile.setFirstName(request.getFirstName());
        profile.setMiddleName(request.getMiddleName());
        profile.setLastName(request.getLastName());
        profile.setProfileImgUrl(request.getProfileImgUrl());

        user.setProfile(profile);

        profileRepository.save(profile);
        userRepository.save(user);
        return profile;
    }


    @Override
    public List<UserDTO> getAllUsers() {
    	 List<User> users = userRepository.findAll();
    	    return users.stream()
    	                .map(this::mapToDTO)
    	                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDTO getUserById(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.map(this::mapToDTO).orElseThrow(()-> new RuntimeException("User not found"));
    }

    @Override
    @Transactional
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public UserDTO updateUserDetails(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));


        // Update User entity
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());

        // Update UserProfile entity
        UserProfile profile = user.getProfile();
        if (profile == null) {
            profile = new UserProfile();
            profile.setUser(user);
        }

        profile.setFirstName(dto.getFirstName());
        profile.setMiddleName(dto.getMiddleName());
        profile.setLastName(dto.getLastName());
        profile.setRole(dto.getRole());
        profile.setDepartment(dto.getDepartment());
        profile.setProfileImgUrl(dto.getProfileImgUrl());
        profile.setInterest(Collections.singletonList(dto.getInterest()));

        user.setProfile(profile);
        userRepository.save(user);

        return mapToDTO(user);
    }

    @Override
    public UserDTO updateUserRoles(Long userId, UserType roles) {
        UserProfile user = profileRepository.findById(userId).orElseThrow();
        user.setRole(roles);
        return mapToDTO(profileRepository.save(user));
    }

    @Override
    public UserDTO updateUserProfileImage(Long userId,String filePath, MultipartFile file) {
        UserProfile profile = profileRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        try {
            String fileName = FileService.uploadImage(filePath, file);
            profile.setProfileImgUrl(fileName);
        } catch (IOException e) {
            throw new RuntimeException("Could not store image", e);
        }
        return mapToDTO(profileRepository.save(profile));
    }

    public UserDTO mapToDTO(User user) {
        UserProfile profile = user.getProfile();

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());

        if (profile != null) {
            dto.setFirstName(profile.getFirstName());
            dto.setMiddleName(profile.getMiddleName());
            dto.setLastName(profile.getLastName());
            dto.setRole(profile.getRole());
            dto.setDepartment(profile.getDepartment());
            dto.setInterest(profile.getInterest().toString());
            dto.setProfileImgUrl(profile.getProfileImgUrl());
        }

        return dto;
    }

    private UserDTO mapToDTO(UserProfile profile) {
        UserDTO dto= new UserDTO();
        dto.setDepartment(profile.getDepartment());
        dto.setInterest(profile.getInterest().toString());
        dto.setRole(profile.getRole());
        dto.setFirstName(profile.getFirstName());
        dto.setMiddleName(profile.getMiddleName());
        dto.setLastName(profile.getLastName());
        dto.setProfileImgUrl(profile.getProfileImgUrl());
        return dto;
    }

}