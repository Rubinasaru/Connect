package com.example.demo.DTO.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyOtpDTO {
    @NotBlank(message = "Email is required")
    @JsonProperty("Email")
    private String email;

    @NotBlank(message = "OTP is required")
    @JsonProperty("Code")
    private String otp;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}
    
    
}
