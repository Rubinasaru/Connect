package com.example.demo.Controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.request.ForgotPasswordDTO;
import com.example.demo.DTO.request.LoginRequest;
import com.example.demo.DTO.request.RegisterRequest;
import com.example.demo.DTO.request.ResetPasswordDTO;
import com.example.demo.DTO.request.VerifyOtpDTO;
import com.example.demo.DTO.response.RegisterResponseDTO;
import com.example.demo.DTO.response.ResponseObject;
import com.example.demo.Models.User;
import com.example.demo.Models.UserProfile;
import com.example.demo.Repository.ProfileRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.AuthenticationService;
import com.example.demo.Service.otpService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth Controller", description = "Authentication related information")
@RequiredArgsConstructor
public class AuthController {

	@Autowired
	private ProfileRepository profileRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationService authService;
	
	@Autowired
	private otpService otpService;

	  @PostMapping("/login")
	    @Operation(summary = "Login a user and return JWT token")
	    public ResponseEntity<ResponseObject> login(@Valid @RequestBody LoginRequest loginRequest){
              return ResponseEntity.ok(
                      ResponseObject.success("Logged in successfully!", authService.login(loginRequest))
              );
      }


    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ResponseObject> register(@Valid @RequestBody RegisterRequest registerRequest){
        try {
            System.out.println("Register request for: " + registerRequest.getEmail());
            RegisterResponseDTO response = authService.register(registerRequest);
            return ResponseEntity.ok(
                    ResponseObject.success("User registered successfully.", response)
            );
        } catch (Exception e) {
            System.err.println("Error in register: " + e.getMessage());
            return ResponseEntity.badRequest().body(
                    ResponseObject.failure("Registration failed: " + e.getMessage() + "!")
            );
        }
    }
    
//    @PostMapping("/forgot-password")
//    @Operation(summary = "Forgot password")
//    public ResponseEntity<ResponseObject> resetPassword(@Valid @RequestBody ForgotPasswordDTO request) {
//        try {
//            System.out.println("Forgot password request for: " + request.getEmail());
//            authService.resetPassword(request);
//            return ResponseEntity.ok(
//                    ResponseObject.success("Password changed successfully!", null)
//            );
//        } catch (Exception e) {
//            System.err.println("Reset password error: " + e.getMessage());
//            return ResponseEntity.badRequest().body(
//                    ResponseObject.failure("Failed to reset password: " + e.getMessage() + "!")
//            );
//        }
//    }
    
    @PostMapping("/forgot-password")
    @Operation(summary="Forgot password")
    public ResponseEntity<ResponseObject> sendOtp( @Valid @RequestBody ForgotPasswordDTO dto) throws UnsupportedEncodingException, MessagingException {
        otpService.sendOtpEmail(dto.getEmail());
        return ResponseEntity.ok(ResponseObject.success("OTP sent to email", null));
    }

    @PostMapping("/verify-otp")
    @Operation(summary="Verification of otp")
    public ResponseEntity<ResponseObject> verifyOtp(@RequestBody @Valid VerifyOtpDTO dto) throws Exception {
        boolean isValid = otpService.verifyOtp(dto.getEmail(), dto.getOtp());
        if (!isValid) {
            throw new Exception("Invalid or expired OTP");
        }
        return ResponseEntity.ok(ResponseObject.success("OTP verified!", null));

    }

    @PostMapping("/resetPassword")
    @Operation(summary = "Reset Password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDTO request) {
        try {
            authService.resetPassword(request);
            return ResponseEntity.ok(
                    ResponseObject.success("Password changed successfully!", null)
            );
        } catch (Exception e) {
            System.err.println("Reset password error: " + e.getMessage());
            return ResponseEntity.badRequest().body(
                    ResponseObject.failure("Failed to reset password: " + e.getMessage() + "!")
            );
        }

      }





}
