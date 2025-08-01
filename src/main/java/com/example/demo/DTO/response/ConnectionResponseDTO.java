package com.example.demo.DTO.response;

import com.example.demo.Enums.ConnectionStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConnectionResponseDTO {
    private Long id;
//    private Long senderId;
    private Long receiverId;

    private String receiverUsername;
    private String status;
    private LocalDateTime createdAt;

    public ConnectionResponseDTO(Long id, Long receiverId, String receiverUsername, ConnectionStatus status,LocalDateTime createdAt) {
        this.id=id;
        this.receiverId=receiverId;
        this.receiverUsername=receiverUsername;
        this.status= String.valueOf(status);
        this.createdAt=createdAt;
    }

    public ConnectionResponseDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Long getSenderId() {
//        return senderId;
//    }
//
//    public void setSenderId(Long senderId) {
//        this.senderId = senderId;
//    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

