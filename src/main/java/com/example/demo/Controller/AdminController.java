package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.request.ChangePasswordDTO;
import com.example.demo.DTO.request.UserDTO;
import com.example.demo.DTO.response.ResponseObject;
import com.example.demo.Enums.UserType;
import com.example.demo.Service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin Controller", description = "Admin Dashboard")
public class AdminController {	

	@Autowired
	private UserService userService;
	
//    @GetMapping("/dashboard")
//    public ResponseEntity<String> adminDashboard(Authentication authentication) {
//        if (authentication.getAuthorities().stream()
//            .anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
//            return ResponseEntity.ok("Welcome Admin!");
//        }
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
//    }
	
	//password change
    @PostMapping("/changePassword")
    @Operation(summary = "Change password")
    public ResponseEntity<ResponseObject> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO){
        ChangePasswordDTO changePasswordDetail = userService.changePassword(changePasswordDTO);
        return ResponseEntity.ok(ResponseObject.success("Password changed successfully!", changePasswordDetail));
    }	

//    //getting all the users
//    @GetMapping("/getAllUsers")
//    @Operation(summary="Get all users")
//    public ResponseEntity<ResponseObject> getAllUsers() {
//        List<UserDTO> users = userService.getAllUsers();
//        return ResponseEntity.ok(ResponseObject.success("User details fetched successfully!", users));
//    }

    //get users by specific id
    @GetMapping("/register/{id}")
    @Operation(summary="Get users by id")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id) {
        UserDTO user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);  
        } else {
            return ResponseEntity.notFound().build();  
        }
    }
    
    //delete the user by id
    @DeleteMapping("/register/{id}")
    @Operation(summary="Delete users by id")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
    	boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.ok("User deleted successfully.");
        }
        return ResponseEntity.noContent().build();
    }
    
    //update the user roles by the admin
    @PutMapping("/{id}/roles")
    @Operation(summary = "Update user roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseObject> updateUserRoles(@PathVariable("id") Long id, @RequestBody UserType role) {
        UserDTO updatedUser = userService.updateUserRoles(id, role);
        return ResponseEntity.ok(ResponseObject.success("User roles updated successfully!", updatedUser));
    }

}

