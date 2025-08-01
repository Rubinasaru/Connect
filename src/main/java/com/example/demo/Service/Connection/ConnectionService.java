package com.example.demo.Service.Connection;

import com.example.demo.DTO.request.UserDTO;
import com.example.demo.DTO.response.ConnectionResponseDTO;

import java.util.List;

public interface ConnectionService {
    void sendConnectionRequest(Long senderId, Long receiverId);
    void respondToRequest(Long senderId,Long receiverId, boolean accept);
    List<ConnectionResponseDTO> getPendingRequests(Long userId);

    List<ConnectionResponseDTO> getSentPendingRequests(Long senderId);

    List<UserDTO> getSuggestedConnections(Long userId);
    List<ConnectionResponseDTO> getConnections(Long userId);
}

