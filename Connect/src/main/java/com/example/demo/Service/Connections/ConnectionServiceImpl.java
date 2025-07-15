package com.example.demo.Service.Connections;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.request.UserDTO;
import com.example.demo.Enums.ConnectionStatus;
import com.example.demo.Mapper.UserMapper;
import com.example.demo.Models.Connections;
import com.example.demo.Models.User;
import com.example.demo.Repository.ConnectionRepository;
import com.example.demo.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConnectionServiceImpl implements ConnectionService {

	@Autowired
    private ConnectionRepository connectionRepo;
	
	@Autowired
    private UserRepository userRepo;
	
	@Autowired
    private UserMapper userMapper;

    @Override
    public void sendRequest(Long senderId, Long receiverId) {
        if (senderId.equals(receiverId)) throw new IllegalArgumentException("Cannot connect to yourself");

        User sender = userRepo.findById(senderId).orElseThrow(()->new RuntimeException("User not found!"));
        User receiver = userRepo.findById(receiverId).orElseThrow(()->new RuntimeException("User not found!"));

        if (connectionRepo.findBySenderAndReceiver(sender, receiver).isPresent())
            throw new IllegalStateException("Connection request already sent");

        connectionRepo.save(new Connections(null, sender, receiver, ConnectionStatus.PENDING, LocalDateTime.now()));
    }

    @Override
    public void respondToRequest(Long connectionId, boolean accept) {
        Connections connection = connectionRepo.findById(connectionId).orElseThrow(()->new RuntimeException("NO connection!"));
        connection.setStatus(accept ? ConnectionStatus.ACCEPTED : ConnectionStatus.REJECTED);
        connectionRepo.save(connection);
    }

    @Override
    public List<UserDTO> getConnections(Long userId) {
        User user = userRepo.findById(userId).orElseThrow();
        List<Connections> accepted = connectionRepo.findBySenderOrReceiverAndStatus(user, user, ConnectionStatus.ACCEPTED);

        Set<User> connected = new HashSet<>();
        for (Connections c : accepted) {
            if (c.getSender().getId()== userId) {
                connected.add(c.getReceiver());
            } else {
                connected.add(c.getSender());
            }
        }
        
        return connected.stream().map(userMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getPendingRequests(Long receiverId) {
        User receiver = userRepo.findById(receiverId).orElseThrow(()->new RuntimeException("User not found!"));
        List<Connections> pending = connectionRepo.findByReceiverAndStatus(receiver, ConnectionStatus.PENDING);
        return pending.stream()
                .map(Connections::getSender)
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getConnectedUserIds(Long userId) {
        return getConnections(userId).stream()
                .map(UserDTO::getId)
                .collect(Collectors.toList());
    }
}
