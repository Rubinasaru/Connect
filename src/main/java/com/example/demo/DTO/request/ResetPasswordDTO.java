package com.example.demo.DTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordDTO {

	private String username;

	private String otp;

	@Size(min = 6, max = 100)
	private String newPassword;

	private String confirmPassword;

	public boolean isPasswordMatch() {
		return newPassword != null && newPassword.equals(confirmPassword);
	}
}
