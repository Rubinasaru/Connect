package com.example.demo.Controller;

import com.example.demo.DTO.request.ConnectionRequestDTO;
import com.example.demo.DTO.response.ConnectionResponseDTO;
import com.example.demo.Service.Connection.ConnectionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/connections")
@Tag(name = "Connections Controller", description = "User Connections related information")
@RequiredArgsConstructor
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    @PostMapping("/send")
    public ResponseEntity<String> sendRequest(@RequestBody ConnectionRequestDTO request,
                                              @RequestHeader("userId") Long senderId) {
        connectionService.sendConnectionRequest(senderId, request.getReceiverId());
        return ResponseEntity.ok("Connection request sent");
    }

    @PostMapping("/{id}/respond")
    public ResponseEntity<String> respondRequest(@PathVariable Long id,
                                                 @RequestParam boolean accept) {
        connectionService.respondToRequest(id, accept);
        return ResponseEntity.ok(accept ? "Request accepted" : "Request rejected");
    }

    @GetMapping("/pending")
    public List<ConnectionResponseDTO> getPending(@RequestHeader("userId") Long userId) {
        return connectionService.getPendingRequests(userId);
    }

    @GetMapping("/accepted")
    public List<ConnectionResponseDTO> getConnections(@RequestHeader("userId") Long userId) {
        return connectionService.getConnections(userId);
    }
}

