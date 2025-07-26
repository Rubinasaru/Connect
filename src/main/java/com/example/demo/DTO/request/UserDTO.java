package com.example.demo.DTO.request;

import com.example.demo.Enums.UserType;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private Long id;

	@NotBlank(message = "Email is required!")
	@Email(message = "Should be of type email!")
	private String Email;

	@NotBlank(message = "Username is required!")
	@Size(min = 3, max = 20, message = "Username must contain minimum 3 characters and maximum 20 characters!")
	@JsonProperty("Username")
	private String username;

	@Column(nullable = false)
	@Pattern(
			regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
			message = "Password must be at least 8 characters and include a number, an uppercase, a lowercase, and a special character"
	)
	@JsonProperty("Password")
	private String password;

	@NotBlank(message = "Firstname is required!")
	@JsonProperty("First Name")
	private String FirstName;

	@Column(nullable= true)
	@JsonProperty("Middle Name")
	private String MiddleName;

	@NotBlank(message = "Lastname is required!")
	@JsonProperty("Last Name")
	private String LastName;

	@NotBlank(message = "Department is required!")
	private String Department;

	@NotBlank(message = "Interest is required!")
	private String Interest;

	private UserType Role;

	@Schema(example = "string")
	@Pattern(
			regexp = "^(http(s?):)([/|.|\\w|\\s|-])*\\.(?:jpg|gif|png|jpeg)$",
			message = "Invalid image URL format"
	)
	@JsonProperty("Profile Image URL")
	private String ProfileImgUrl;

	private Boolean ProfileCompleted=false;

	public Boolean getProfileCompleted() {
		return ProfileCompleted;
	}

	public void setProfileCompleted(Boolean ProfileCompleted) {
		this.ProfileCompleted = ProfileCompleted;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

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



	public String getInterest() {
		return Interest;
	}

	public void setInterest(String interest) {
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

	public void setId(Long id) {
		this.id = id;
	}


}