package com.example.demo.Controller;

import com.example.demo.DTO.request.ConnectionRequestDTO;
import com.example.demo.DTO.request.NotificationDTO;
import com.example.demo.DTO.response.ConnectionResponseDTO;
import com.example.demo.DTO.response.ResponseObject;
import com.example.demo.Service.Connection.ConnectionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/connections")
@Tag(name = "Connections Controller", description = "User Connections related information")
@RequiredArgsConstructor
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private NotificationController notificationController;

    @PostMapping("/sendRequest/{senderId}")
    public ResponseEntity<String> sendRequest(@RequestBody ConnectionRequestDTO request,
                                                      @PathVariable("senderId") Long senderId) {
        connectionService.sendConnectionRequest(senderId, request.getReceiverId());
        return ResponseEntity.ok("Connection request sent");
    }

    @PostMapping("/{senderId}/respond")
    public ResponseEntity<String> respondRequest(@PathVariable("senderId") Long id,
                                                 @RequestParam boolean accept) {
        connectionService.respondToRequest(id, accept);
        return ResponseEntity.ok(accept ? "Request accepted" : "Request rejected");
    }

    @GetMapping("/{userId}/pending")
    public List<ConnectionResponseDTO> getPending(@PathVariable("userId") Long userId) {
        return connectionService.getPendingRequests(userId);
    }

    @GetMapping("/{userId}/accepted")
    public List<ConnectionResponseDTO> getConnections(@PathVariable("userId") Long userId) {
        return connectionService.getConnections(userId);
    }
}

