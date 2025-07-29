package com.example.demo.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import com.example.demo.DTO.response.UserInfoResponse;
import com.example.demo.Models.User;
import com.example.demo.Models.UserProfile;
import com.example.demo.Repository.ProfileRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.DTO.request.OtpVerificationRequest;
import com.example.demo.DTO.request.ProfileSetupRequest;
import com.example.demo.DTO.request.UserDTO;
import com.example.demo.DTO.response.ResponseObject;
import com.example.demo.Service.UserProfileService;
import com.example.demo.Service.UserService;
import com.example.demo.utils.FileValidator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
@Tag(name="User Controller",description="User related information")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserProfileService userProfileService;

    private FileValidator fileValidator;
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    //Set up the user Profile with other informations

    @GetMapping
    public ResponseEntity<ResponseObject> getAllUsers(){
                return ResponseEntity.ok(ResponseObject.success("All users are fetched.",userService.getAllUsers()));
    }

    @PutMapping("/setupprofile")
    public ResponseEntity<ResponseObject> setupProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ProfileSetupRequest request) {

        // Fallback for userDetails if null
        if (userDetails == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ResponseObject.failure("Unauthorized: Could not extract user details from token."));
            }
            userDetails = (UserDetails) authentication.getPrincipal();
        }

        String username = userDetails.getUsername();

        // Get the User associated with the username
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseObject.failure("User not found: " + username));
        }

        User user = userOptional.get();

        // Now get or create the profile for this user
        Optional<UserProfile> profileOptional = profileRepository.findByUser(user);

        UserProfile existingProfile = profileOptional.orElse(new UserProfile());
        existingProfile.setUser(user); // link the user if new profile

        // Allow the same user to reuse their own email
//        if (!request.getUsername().equalsIgnoreCase(existingProfile.getUsername())
//                && userRepository.existsByUsername(request.getUsername())) {
//            return ResponseEntity.ok(ResponseObject.failure("Username (" + request.getUsername() + ") is already in use!"));
//        }

        // Update profile fields
        existingProfile.setRole(request.getRole());
        existingProfile.setFirstName(request.getFirstName());
        existingProfile.setLastName(request.getLastName());
        existingProfile.setMiddleName(request.getMiddleName());
        existingProfile.setDepartment(request.getDepartment());
        existingProfile.setInterest(new ArrayList<>(request.getInterest()));
        existingProfile.setProfileImgUrl(request.getProfileImgUrl());


        existingProfile.setUser(user);

        user.setProfileCompleted();
        user.setProfile(existingProfile);
        userRepository.save(user);

        String updatedToken = jwtService.generateToken(user);
        System.out.println(jwtService.decodeJWT(updatedToken));
        return ResponseEntity.ok(
                ResponseObject.success("Profile setup sucessfully", new UserInfoResponse(updatedToken,userDetails.getUsername()))
        );
    }


//    public ResponseEntity<ResponseObject> setupProfile(@RequestBody ProfileSetupRequest request,
//                                               Principal principal) {
//        String email = principal.getName(); // From JWT Auth
//        return ResponseEntity.ok(ResponseObject.success("Profile updated successfully!", userService.setupProfile(email, request)));
//    }

    //Update the user information in Profile
//    @PutMapping("updateUserDetails/{id}")
//    @Operation(summary = "Update user details")
//    public ResponseEntity<ResponseObject> updateUserDetails(@PathVariable("id") Long id, @Valid @RequestBody UserDTO userDTO) {
//        UserDTO updatedUser = userService.updateUserDetails(id, userDTO);
//        return ResponseEntity.ok(ResponseObject.success("User details updated successfully!", updatedUser));
//    }

    @PostMapping("/verifyEmail")
    @Operation(summary = "Verify email with OTP")
    public ResponseEntity<ResponseObject> verifyEmail(@Valid @RequestBody OtpVerificationRequest otpVerificationRequest) throws Exception {
        userProfileService.verifyEmail(otpVerificationRequest);
        return ResponseEntity.ok(
                ResponseObject.success("Email verified successfully!", null)
        );
    }



//    @PutMapping(value = "/{userId}/profileImage", consumes = "multipart/form-data")
//    @Operation(summary = "Upload or update user profile image")
//    public ResponseEntity<ResponseObject> updateUserProfileImage(
//            @PathVariable Long id,
//            @RequestParam("filePath") String filePath,
//            @RequestParam("file") MultipartFile file) throws IOException {
//        fileValidator.validateFile(file);
//        UserDTO updateUserProfileImage = userService.updateUserProfileImage(id,filePath, file);
//        return ResponseEntity.ok(ResponseObject.success("Profile image uploaded successfully!", updateUserProfileImage));
//    }
//
}