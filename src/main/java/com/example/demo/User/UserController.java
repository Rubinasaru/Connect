package com.example.demo.User;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Tag(name="User controller",description="User related information")
public class UserController {
	
	@Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary="register users")
    public String createUser(@RequestBody User user) {
    	if(user.getYear()<=LocalDate.now().getYear()) {
    		user.setRole(UserType.Senior);
    	}else {
    		user.setRole(UserType.Junior);
    	}
        if(userService.createUser(user) != null) {
        	return "User registered successfully!";
        }else {
        	return "User cannot be registered!! ";
        }
    }

    @GetMapping("/register")
    @Operation(summary="Get all users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("register/{id}")
    @Operation(summary="Get users by id")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);  
        } else {
            return ResponseEntity.notFound().build();  
        }
    }

    @DeleteMapping("register/{id}")
    @Operation(summary="Delete users by id")
    public ResponseEntity<String> deleteUser(@PathVariable("id") int id) {
    	boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.ok("User deleted successfully.");
        }
        return ResponseEntity.noContent().build();
    }
    
    

  
		
}

