package com.example.demo.DTO.request;

import lombok.Data;

@Data
public class ConnectionRequestDTO {
    private Long receiverId;

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }
}

