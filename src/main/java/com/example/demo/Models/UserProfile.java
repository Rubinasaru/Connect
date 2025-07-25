package com.example.demo.Models;

import com.example.demo.Enums.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class UserProfile {
	@Id
    private Long id; // Same as user ID

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonIgnore
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
    @JsonProperty("Interest")
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> interest;
    
    @Column(nullable= false)
    @JsonProperty("Role")
    private UserType role;

	@Column(length = 1000)
	@JsonProperty("Profile Img Url")
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

	public List<String> getInterest() {
		return interest;
	}

	public void setInterest(List<String> interest) {
		this.interest = interest;
	}

	public UserType getRole() {
		return role;
	}


	public void setRole(UserType role) {
		this.role = role;
	}


	public String getProfileImgUrl() {
		return profileImgUrl;
	}


	public void setProfileImgUrl(String profileImgUrl) {
		this.profileImgUrl = profileImgUrl;
	}
	
	public UserProfile(String firstName,String middleName,String lastName,String email,String department,List<String> interest,String profileImgUrl) {
		this.firstName=firstName;
		this.middleName=middleName;
		this.lastName=lastName;
		this.email=email;
		this.department=department;
		this.interest=interest;
		this.profileImgUrl=profileImgUrl;
		this.isEmailVerified=false;
	}

}
