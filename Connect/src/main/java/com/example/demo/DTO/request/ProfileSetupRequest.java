package com.example.demo.DTO.request;

import com.example.demo.Enums.UserType;

import lombok.Data;

@Data
public class ProfileSetupRequest {
	private String Email;
	private String FirstName;
	private String MiddleName;
	private String LastName;
	private String Department;
	private Long Year;
	private String ProfileImgUrl;
	

	public String getEmail() {
		return Email;
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
	public Long getYear() {
		return Year;
	}
	public void setYear(Long year) {
		Year = year;
	}
	public String getProfileImgUrl() {
		return ProfileImgUrl;
	}
	public void setProfileImgUrl(String profileImgUrl) {
		ProfileImgUrl = profileImgUrl;
	}
	public void setEmail(String email) {
		Email = email;
	}
	
}
