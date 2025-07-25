package com.example.demo.Controller;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.DTO.request.ChangePasswordDTO;
import com.example.demo.DTO.request.OtpVerificationRequest;
import com.example.demo.DTO.request.ProfileSetupRequest;
import com.example.demo.DTO.request.RegisterRequest;
import com.example.demo.DTO.request.UserDTO;
import com.example.demo.DTO.response.ResponseObject;
import com.example.demo.Enums.UserType;
import com.example.demo.Models.UserProfile;
import com.example.demo.Service.UserProfileService;
import com.example.demo.Service.UserService;
import com.example.demo.utils.FileValidator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Tag(name="User Controller",description="User related information")
public class UserController {
	
	@Autowired
    private UserService userService;
	
	@Autowired
	private UserProfileService userProfileService;

	private FileValidator fileValidator;
    
    //Set up the user Profile with other informations 
    @PutMapping("user/setupprofile")
    public ResponseEntity<ResponseObject> setupProfile(

            @AuthenticationPrincipal UserDetails userDetails,
         @Valid @RequestBody ProfileSetupRequest request) {

        String username = userDetails.getUsername();

        return ResponseEntity.ok(
                ResponseObject.success("Profile updated successfully!", userService.setupProfile(username,request))
        );
    }

//    public ResponseEntity<ResponseObject> setupProfile(@RequestBody ProfileSetupRequest request,
//                                               Principal principal) {
//        String email = principal.getName(); // From JWT Auth
//        return ResponseEntity.ok(ResponseObject.success("Profile updated successfully!", userService.setupProfile(email, request)));
//    }
    
    //Update the user information in Profile
    @PutMapping("/user/updateUserDetails/{id}")
    @Operation(summary = "Update user details")
    public ResponseEntity<ResponseObject> updateUserDetails(@PathVariable("id") Long id, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUserDetails(id, userDTO);
        return ResponseEntity.ok(ResponseObject.success("User details updated successfully!", updatedUser));
    }
    
    @PostMapping("/user/verifyEmail")
    @Operation(summary = "Verify email with OTP")
    public ResponseEntity<ResponseObject> verifyEmail(@Valid @RequestBody OtpVerificationRequest otpVerificationRequest) throws Exception {
        userProfileService.verifyEmail(otpVerificationRequest);
        return ResponseEntity.ok(
                ResponseObject.success("Email verified successfully!", null)
        );
    }
    
    
    @PutMapping(value = "/user/{userId}/profileImage", consumes = "multipart/form-data")
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

