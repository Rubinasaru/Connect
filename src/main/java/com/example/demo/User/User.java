package com.example.demo.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="users")

public class User {
	
	@Id	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public int id;
	
	public String fullName;
	public String username;
	
	@Column(unique= true)
	public String email;
	
	public String password;
	public String department;

	@Enumerated(EnumType.STRING)
	public UserType role;

	public int year;

	
//	@Column(unique = true)
//	private String session;


	public User(){
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getfullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public UserType getRole() {
		return role;
	}

	public void setRole(UserType role) {
		this.role = role;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	

//	public String getSession() {
//		return session;
//	}
//
//	public void setSession(String session) {
//		this.session = session;
//	}
	
	
}
