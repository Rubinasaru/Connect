package com.example.demo.Controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import com.example.demo.DTO.response.UserInfoResponse;
import com.example.demo.Models.User;
import com.example.demo.Models.UserProfile;
import com.example.demo.Repository.ProfileRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.FileService;
import com.example.demo.Service.JwtService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @Autowired
    private FileService fileService;

    //Set up the user Profile with other informations

    @GetMapping
    public ResponseEntity<ResponseObject> getAllUsers(){
                return ResponseEntity.ok(ResponseObject.success("All users are fetched.",userService.getAllUsers()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String query,
                                                     @RequestHeader("Authorization") String token) {
        String username = jwtService.getUserNameFromJwtToken(token.replace("Bearer ", ""));
        List<UserDTO> results = userService.searchUsersByInterestOrName(query, username);
        return ResponseEntity.ok(results);
    }



    @PutMapping("/setupprofile")
    public ResponseEntity<ResponseObject> setupProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ProfileSetupRequest request) {

        // for userDetails if null
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

        // get or create the profile for this user
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


//    @PostMapping(value = "/uploadProfileImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<String> uploadProfileImage(@RequestParam("file") MultipartFile file) {
//        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
//        Path uploadPath = Paths.get("uploads");
//
//        try {
//            if (!Files.exists(uploadPath)) {
//                Files.createDirectories(uploadPath);
//            }
//            Path filePath = uploadPath.resolve(fileName);
//            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//            // Return just the filename
//            return ResponseEntity.ok(fileName);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image upload failed");
//        }
//    }

    @PostMapping(value = "/uploadProfileImage" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProfileImage(@RequestParam("file") MultipartFile file) throws IOException {
        String uploadDir = "uploads/";
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filepath = Paths.get(uploadDir + filename);
        Files.createDirectories(filepath.getParent());
        Files.copy(file.getInputStream(), filepath);

        // Return the full URL to the uploaded image
        String fileDownloadUri = "http://localhost:1012/uploads/" + filename;
        return ResponseEntity.ok(fileDownloadUri);
    }



    @GetMapping("/uploads/{filename}")
    public ResponseEntity<Resource> getProfileImage(@PathVariable String filename) throws MalformedURLException {
        Path path = Paths.get("uploads").resolve(filename).normalize();
        Resource resource = (Resource) new UrlResource(path.toUri());

//        if (!resource.exists()) {
//            return ResponseEntity.notFound().build();
//        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // or detect type dynamically
                .body(resource);
    }




}