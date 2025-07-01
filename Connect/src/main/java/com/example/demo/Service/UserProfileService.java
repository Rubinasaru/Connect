package com.example.demo.Service;

import com.example.demo.DTO.request.OtpVerificationRequest;
public interface UserProfileService {

	 void verifyEmail(OtpVerificationRequest otpVerificationRequest) throws Exception;
}
