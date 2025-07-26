package com.example.demo.Controller;

import java.io.IOException;

import com.example.demo.Repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    
    //Set up the user Profile with other informations

    @PutMapping("/setupprofile")
    public ResponseEntity<ResponseObject> setupProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ProfileSetupRequest request) {

        // Debug check: see if authentication is missing
        if (userDetails == null) {
            // Fallback: get authentication manually
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ResponseObject.failure("Unauthorized: Could not extract user details from token."));
            }

            userDetails = (UserDetails) authentication.getPrincipal();
        }

        System.out.println("Authenticated UserDetails: " + userDetails);
        String username = userDetails.getUsername();

        if (profileRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.ok(ResponseObject.failure("Email (" + request.getEmail() + ") is already in use!"));
        }

        return ResponseEntity.ok(
                ResponseObject.success("Profile updated successfully!", userService.setupProfile(username, request))
        );
    }

//    public ResponseEntity<ResponseObject> setupProfile(@RequestBody ProfileSetupRequest request,
//                                               Principal principal) {
//        String email = principal.getName(); // From JWT Auth
//        return ResponseEntity.ok(ResponseObject.success("Profile updated successfully!", userService.setupProfile(email, request)));
//    }
    
    //Update the user information in Profile
    @PutMapping("updateUserDetails/{id}")
    @Operation(summary = "Update user details")
    public ResponseEntity<ResponseObject> updateUserDetails(@PathVariable("id") Long id, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUserDetails(id, userDTO);
        return ResponseEntity.ok(ResponseObject.success("User details updated successfully!", updatedUser));
    }
    
    @PostMapping("/verifyEmail")
    @Operation(summary = "Verify email with OTP")
    public ResponseEntity<ResponseObject> verifyEmail(@Valid @RequestBody OtpVerificationRequest otpVerificationRequest) throws Exception {
        userProfileService.verifyEmail(otpVerificationRequest);
        return ResponseEntity.ok(
                ResponseObject.success("Email verified successfully!", null)
        );
    }
    
    
    @PutMapping(value = "/{userId}/profileImage", consumes = "multipart/form-data")
    @Operation(summary = "Upload or update user profile image")
    public ResponseEntity<ResponseObject> updateUserProfileImage(
            @PathVariable Long id,
            @RequestParam("filePath") String filePath,
            @RequestParam("file") MultipartFile file) throws IOException {
        fileValidator.validateFile(file);
        UserDTO updateUserProfileImage = userService.updateUserProfileImage(id,filePath, file);
        return ResponseEntity.ok(ResponseObject.success("Profile image uploaded successfully!", updateUserProfileImage));
    }
		
}

