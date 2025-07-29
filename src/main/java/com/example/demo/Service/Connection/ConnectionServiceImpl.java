package com.example.demo.Service.Connection;

import com.example.demo.Controller.NotificationController;
import com.example.demo.DTO.request.NotificationDTO;
import com.example.demo.DTO.response.ConnectionResponseDTO;
import com.example.demo.Enums.ConnectionStatus;
import com.example.demo.Models.Connection;
import com.example.demo.Models.UserProfile;
import com.example.demo.Repository.ConnectionRepository;
import com.example.demo.Repository.ProfileRepository;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConnectionServiceImpl implements ConnectionService {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private NotificationController notificationController;

    @Override
    public void sendConnectionRequest(Long senderId, Long receiverId) {
        if (connectionRepository.findBySenderIdAndReceiverId(senderId, receiverId).isPresent()) {
            throw new RuntimeException("Connection already exists");
        }

        UserProfile sender = profileRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        UserProfile receiver = profileRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Connection connection = new Connection();
        connection.setSender(sender.getUser());
        connection.setReceiver(receiver.getUser());
        connection.setStatus(ConnectionStatus.PENDING);

        connectionRepository.save(connection);

        NotificationDTO notification = new NotificationDTO();
        notification.setSenderId(senderId);
        notification.setRecipientId(receiverId);
        notification.setType("CONNECTION_REQUEST");
        notification.setContent(sender.getFirstName() + " sent you a connection request");
        notification.setTimestamp(LocalDateTime.now().toString());

        notificationController.sendNotification(notification);
    }


    @Override
    public void respondToRequest(Long connectionId, boolean accept) {
        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new RuntimeException("Connection not found"));

        connection.setStatus(accept ? ConnectionStatus.ACCEPTED : ConnectionStatus.REJECTED);
        connectionRepository.save(connection);
    }

    @Override
    public List<ConnectionResponseDTO> getPendingRequests(Long userId) {
        return connectionRepository.findByReceiverIdAndStatus(userId, ConnectionStatus.PENDING)
                .stream().map(this::mapToDto).toList();
    }

    @Override
    public List<ConnectionResponseDTO> getConnections(Long userId) {
        return connectionRepository.findBySenderIdOrReceiverIdAndStatus(userId, userId, ConnectionStatus.ACCEPTED)
                .stream().map(this::mapToDto).toList();
    }

    private ConnectionResponseDTO mapToDto(Connection connection) {
        ConnectionResponseDTO dto = new ConnectionResponseDTO();
        dto.setId(connection.getId());
        dto.setSenderId(connection.getSender().getId());
        dto.setReceiverId(connection.getReceiver().getId());
        dto.setStatus(connection.getStatus().name());
        dto.setCreatedAt(connection.getCreatedAt());
        return dto;
    }
}

