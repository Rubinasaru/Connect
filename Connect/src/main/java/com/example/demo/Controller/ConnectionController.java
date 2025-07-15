package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.request.UserDTO;
import com.example.demo.Service.UserDetailsImpl;
import com.example.demo.Service.Connections.ConnectionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/connection")
@Tag(name = "Connection Controller", description = "User Connections related information")
@RequiredArgsConstructor
public class ConnectionController {
	
	@Autowired
	 private ConnectionService connectionService;

	    @PostMapping("/send/{receiverId}")
	    public ResponseEntity<String> sendRequest(@PathVariable("receiverId") Long receiverId, @AuthenticationPrincipal UserDetailsImpl currentUser) {
//	    	System.out.println("CurrentUser = " + currentUser);

	    	connectionService.sendRequest(currentUser.getId(), receiverId);
	        return ResponseEntity.ok("Connection request sent");
	    }

	    @PostMapping("/respond/{connectionId}")
	    public ResponseEntity<String> respondToRequest(@PathVariable("connectionId") Long connectionId, @RequestParam("accept") boolean accept) {
	        connectionService.respondToRequest(connectionId, accept);
	        return ResponseEntity.ok("Request " + (accept ? "accepted" : "rejected"));
	    }

	    @GetMapping("/myConnections")
	    public ResponseEntity<List<UserDTO>> myConnections(@AuthenticationPrincipal UserDetailsImpl currentUser) {
	        return ResponseEntity.ok(connectionService.getConnections(currentUser.getId()));
	    }

	    @GetMapping("/pendingRequests")
	    public ResponseEntity<List<UserDTO>> pendingRequests(@AuthenticationPrincipal UserDetailsImpl currentUser) {
	        return ResponseEntity.ok(connectionService.getPendingRequests(currentUser.getId()));
	    }
}
