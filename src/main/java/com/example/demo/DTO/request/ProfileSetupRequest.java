package com.example.demo.DTO.request;

import com.example.demo.Enums.UserType;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ProfileSetupRequest {
	@NotBlank(message = "Firstname is required!")
	@Pattern(regexp = "^[A-Za-z]+$", message = "First name must contain only letters")
	@Schema(example = "string")
	private String FirstName;

	private String MiddleName;

	@NotBlank(message = "Lastname is required!")
	@Pattern(regexp = "^[A-Za-z]+$", message = "Last name must contain only letters")
	@Schema(example = "string")
	private String LastName;

	private String Department;
	private List<String> Interest;
	private UserType Role;

	@Pattern(
			regexp = "^(http(s?):)([/|.|\\w|\\s|-])*\\.(?:jpg|gif|png|jpeg)$",
			message = "Invalid image URL format"
	)
	private String ProfileImgUrl;

	public String getFirstName() {
		return FirstName;
	}
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	public String getMiddleName() {
		return MiddleName;
	}
	public void setMiddleName(String middleName) {
		MiddleName = middleName;
	}
	public String getLastName() {
		return LastName;
	}
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	public String getDepartment() {
		return Department;
	}
	public void setDepartment(String department) {
		Department = department;
	}

	public List<String> getInterest() {
		return Interest;
	}

	public void setInterest(List<String> interest) {
		Interest = interest;
	}

	public UserType getRole() {
		return Role;
	}
	public void setRole(UserType role) {
		Role = role;
	}
	public String getProfileImgUrl() {
		return ProfileImgUrl;
	}
	public void setProfileImgUrl(String profileImgUrl) {
		ProfileImgUrl = profileImgUrl;
	}


}