package com.example.demo.Service.Connections;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.DTO.request.UserDTO;

@Service
public interface ConnectionService {
    void sendRequest(Long senderId, Long receiverId);
    void respondToRequest(Long connectionId, boolean accept);
    
    //for user's Profile
    List<UserDTO> getConnections(Long userId);
    List<UserDTO> getPendingRequests(Long receiverId);
    
    //for feed posts
    List<Long> getConnectedUserIds(Long userId); 
}

