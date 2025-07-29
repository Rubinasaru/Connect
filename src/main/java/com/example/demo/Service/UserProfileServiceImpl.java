package com.example.demo.Service;

import com.example.demo.Models.User;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.request.OtpVerificationRequest;

import com.example.demo.Models.UserProfile;
import com.example.demo.Repository.ProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserProfileServiceImpl implements UserProfileService{
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private otpService otpService;

    @Transactional
    public void verifyEmail(OtpVerificationRequest otpVerificationRequest) throws Exception {
        // Check if user exists first
        User user = userRepository.findByEmail(otpVerificationRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + otpVerificationRequest.getEmail() + "!"));

        // Check if email is already verified
        if(user.isEmailVerified()){
            throw new IllegalArgumentException("Email is already verified!");
        }

        // Verify OTP
        if (!otpService.verifyOtp(otpVerificationRequest.getEmail(), otpVerificationRequest.getOtp())) {
            throw new IllegalArgumentException("Invalid or expired OTP!");
        }

        // Update user verification status
        user.setEmailVerified(true);
        userRepository.save(user);
    }

}
