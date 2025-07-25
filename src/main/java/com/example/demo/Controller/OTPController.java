package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.request.OtpRequest;
import com.example.demo.DTO.response.ResponseObject;
import com.example.demo.Service.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Otp Controller", description = "OTP related information")
@RequiredArgsConstructor
public class OTPController {
	@Autowired
    private AuthenticationService authService;

    @PostMapping("/user/generateOtp")
    @Operation(summary = "Generate OTP for email verification")
    public ResponseEntity<ResponseObject> generateOtp(@Valid @RequestBody OtpRequest otpRequest){
        try {
            authService.generateOtpForRegistration(otpRequest);
            return ResponseEntity.ok(
                    ResponseObject.success("OTP sent successfully!", "OTP sent to " + otpRequest.getEmail())
            );
        } catch (Exception e) {
            System.err.println("Error in generate OTP: " + e.getMessage());
            return ResponseEntity.badRequest().body(
                    ResponseObject.failure("Failed to send OTP: " + e.getMessage())
            );
        }
    }
}
