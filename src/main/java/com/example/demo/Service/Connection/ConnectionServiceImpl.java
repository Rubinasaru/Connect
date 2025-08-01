package com.example.demo.Service.Connection;

import com.example.demo.Controller.NotificationController;
import com.example.demo.DTO.request.NotificationDTO;
import com.example.demo.DTO.request.UserDTO;
import com.example.demo.DTO.response.ConnectionResponseDTO;
import com.example.demo.Enums.ConnectionStatus;
import com.example.demo.Models.Connection;
import com.example.demo.Models.User;
import com.example.demo.Models.UserProfile;
import com.example.demo.Repository.ConnectionRepository;
import com.example.demo.Repository.ProfileRepository;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ConnectionServiceImpl implements ConnectionService {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private UserRepository userRepository;

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
    public void respondToRequest(Long senderId, Long receiverId, boolean accept) {
        Connection connection = connectionRepository
                .findBySenderIdAndReceiverId(senderId, receiverId)
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

    @Override
    public List<ConnectionResponseDTO> getSentPendingRequests(Long senderId) {
        List<Connection> pendingRequests = connectionRepository.findBySenderIdAndStatus(senderId, ConnectionStatus.valueOf("PENDING"));

        return pendingRequests.stream().map(connection -> {
            Long receiverId = connection.getId();
            String receiverUsername = userRepository.findById(receiverId)
                    .map(User::getUsername)
                    .orElse("Unknown");

            return new ConnectionResponseDTO(
                    connection.getId(),
                    receiverId,
                    receiverUsername,
                    connection.getStatus(),
                    connection.getCreatedAt()
            );
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getSuggestedConnections(Long userId) {
        // Step 1: Get current user's profile
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserProfile currentProfile = user.getProfile();
        if (currentProfile == null) return Collections.emptyList();

        List<String> currentUserInterests = currentProfile.getInterest();
        String currentDepartment = currentProfile.getDepartment();

        List<User> allUsers = userRepository.findAllExceptUser(userId);

        // Step 3: Get existing connections
        List<Connection> existingConnections = connectionRepository.findByUserId(userId);
        Set<Long> connectedUserIds = existingConnections.stream()
                .map(conn -> conn.getOtherUserId(userId))  // FIXED
                .collect(Collectors.toSet());

        System.out.println("Current interests: " + currentUserInterests);
        System.out.println("Current department: " + currentDepartment);

        return allUsers.stream()
                .filter(u -> !connectedUserIds.contains(u.getId()))
                .filter(u -> {
                    UserProfile profile = u.getProfile();
                    if (profile == null || profile.getInterest() == null || currentUserInterests == null)
                        return false;

                    boolean matched = profile.getInterest().stream()
                            .anyMatch(interest ->
                                    currentUserInterests.stream()
                                            .anyMatch(current -> interest.trim().equalsIgnoreCase(current.trim()))
                            );
                    if (matched) {
                        System.out.println("Matched interest: " + u.getUsername());
                    }
                    return matched;
                })
                .filter(u -> {
                    UserProfile profile = u.getProfile();
                    boolean deptMatch = profile != null && profile.getDepartment() != null &&
                            profile.getDepartment().trim().equalsIgnoreCase(currentDepartment.trim());
                    if (deptMatch) {
                        System.out.println("Matched department: " + u.getUsername());
                    }
                    return deptMatch;
                })
                .map(this::convertToUserDto)
                .collect(Collectors.toList());
    }

    private UserDTO convertToUserDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());

        UserProfile profile = user.getProfile();
        if (profile != null) {
            dto.setFirstName(profile.getFirstName());
            dto.setLastName(profile.getLastName());
            dto.setDepartment(profile.getDepartment());
            dto.setInterest(String.join(", ", profile.getInterest()));
            dto.setProfileImgUrl(profile.getProfileImgUrl());
        }
        return dto;
    }


    private ConnectionResponseDTO mapToDto(Connection connection) {
        ConnectionResponseDTO dto = new ConnectionResponseDTO();
        dto.setId(connection.getId());
//        dto.setSenderId(connection.getSender().getId());
        dto.setReceiverId(connection.getReceiver().getId());
        dto.setReceiverUsername(connection.getReceiver().getUsername());
        dto.setStatus(connection.getStatus().name());
        dto.setCreatedAt(connection.getCreatedAt());
        return dto;
    }
}

