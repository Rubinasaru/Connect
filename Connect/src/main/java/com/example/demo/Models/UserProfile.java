package com.example.demo.Models;

import com.example.demo.Enums.UserType;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.RequiredArgsConstructor;

@Entity
public class UserProfile {
    @Id
    private Long id; // Same as user ID

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable= false)
    @JsonProperty("First Name")
    private String firstName;
    
    @Column(nullable= true)
    @JsonProperty("Middle Name")
    private String middleName;
    
    @Column(nullable= false)
    @JsonProperty("Last Name")
    private String lastName;
    
    @Column(unique=true,nullable= false)
    @JsonProperty("Email")
    private String email;
    
    @Column(name = "is_email_verified", nullable = false)
    private boolean isEmailVerified = false;
  
    
    @Column(nullable= false)
    @JsonProperty("Department")
    private String department;
    
    @Column(nullable= false)
    @JsonProperty("Year")
    private Long year;
    
    private String profileImgUrl;
    
    public UserProfile(){
    	
    }
    
    public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getMiddleName() {
		return middleName;
	}


	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public boolean isEmailVerified() {
		return isEmailVerified;
	}

	public void setEmailVerified(boolean isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}


	public Long getYear() {
		return year;
	}


	public void setYear(Long year) {
		this.year = year;
	}


	public String getProfileImgUrl() {
		return profileImgUrl;
	}


	public void setProfileImgUrl(String profileImgUrl) {
		this.profileImgUrl = profileImgUrl;
	}
	
	public UserProfile(String firstName,String middleName,String lastName,String email,String department,String profileImgUrl) {
		this.firstName=firstName;
		this.middleName=middleName;
		this.lastName=lastName;
		this.email=email;
		this.department=department;
		this.profileImgUrl=profileImgUrl;
		this.isEmailVerified=false;
	}

	
}
