package com.example.demo.DTO.request;

import com.example.demo.Enums.UserType;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Email is required!")
    @Email(message = "Should be of type email!")
    @JsonProperty("Email")
    private String email;

    @NotBlank(message = "Username is required!")
    @Size(min = 3, max = 20, message = "Username must contain minimum 3 characters and maximum 20 characters!")
    @JsonProperty("Username")
    private String username;
    
    @Column(nullable = false)
    @JsonProperty("Password")
    private String password;

	@NotBlank(message = "Fullname is required!")
	@JsonProperty("First Name")
    private String firstName;
	
	@Column(nullable= true)
	@JsonProperty("Middle Name")
    private String middleName;
	
	@NotBlank(message = "Lastname is required!")
	@JsonProperty("Last Name")
    private String lastName;
    
    @NotBlank(message = "Department is required!")
    @JsonProperty("Department")
    private String department;
    
    @JsonProperty("Year")
    private Long year; 
    
    @Schema(example = "string")
    @JsonProperty("Profile Image Url")
    private String profileImgUrl;
    
    private Boolean profileCompleted=false;
    
    public UserDTO(){
    	
    }
    
    public UserDTO(long id, String username, String email,String profileImgUrl) {
		this.id=id;
		this.username=username;
		this.email=email;
		this.profileImgUrl=profileImgUrl;
	}

	public Boolean getProfileCompleted() {
		return profileCompleted;
	}

	public void setProfileCompleted(Boolean ProfileCompleted) {
		this.profileCompleted = ProfileCompleted;
	}

	public long getId() {
 		return id;
 	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

 	
}
