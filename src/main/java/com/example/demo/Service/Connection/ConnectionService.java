package com.example.demo.Service.Connection;

import com.example.demo.DTO.response.ConnectionResponseDTO;

import java.util.List;

public interface ConnectionService {
    void sendConnectionRequest(Long senderId, Long receiverId);
    void respondToRequest(Long connectionId, boolean accept);
    List<ConnectionResponseDTO> getPendingRequests(Long userId);
    List<ConnectionResponseDTO> getConnections(Long userId);
}

