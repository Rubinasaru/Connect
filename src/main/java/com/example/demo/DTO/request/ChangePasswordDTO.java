package com.example.demo.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordDTO {
	 @NotBlank(message = "Old password is required!")
	    @Size(min = 8, message = "Old password must be at least 8 characters long!")
	    @Pattern(
	            regexp = "^(?=.*[A-Z])(?=.*[@$!%*?&])(?=.*\\d).+$",
	            message = "Old password must contain at least one uppercase letter, one special character, and one number!"
	    )
	    @Schema(example = "string")
	    private String oldPassword;

	    @NotBlank(message = "New password is required!")
	    @Size(min = 8, message = "New password must be at least 8 characters long!")
	    @Pattern(
	            regexp = "^(?=.*[A-Z])(?=.*[@$!%*?&])(?=.*\\d).+$",
	            message = "New password must contain at least one uppercase letter, one special character, and one number!"
	    )
	    @Schema(example = "string")
	    private String newPassword;

	    @NotBlank(message = "Confirm new password is required!")
	    @Size(min = 8, message = "Confirm new password must be at least 8 characters long!")
	    @Pattern(
	            regexp = "^(?=.*[A-Z])(?=.*[@$!%*?&])(?=.*\\d).+$",
	            message = "Confirm new password must contain at least one uppercase letter, one special character, and one number!"
	    )
	    @Schema(example = "string")
	    private String confirmNewPassword;

	    public boolean isPasswordMatch() {
	        return newPassword != null && newPassword.equals(confirmNewPassword);
	    }

		public String getOldPassword() {
			return oldPassword;
		}

		public void setOldPassword(String oldPassword) {
			this.oldPassword = oldPassword;
		}

		public String getNewPassword() {
			return newPassword;
		}

		public void setNewPassword(String newPassword) {
			this.newPassword = newPassword;
		}

		public String getConfirmNewPassword() {
			return confirmNewPassword;
		}

		public void setConfirmNewPassword(String confirmNewPassword) {
			this.confirmNewPassword = confirmNewPassword;
		}
		
}
