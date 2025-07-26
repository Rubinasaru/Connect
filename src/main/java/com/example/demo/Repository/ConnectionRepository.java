package com.example.demo.Repository;

import com.example.demo.Enums.ConnectionStatus;
import com.example.demo.Models.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    List<Connection> findByReceiverIdAndStatus(Long receiverId, ConnectionStatus status);
    List<Connection> findBySenderIdAndStatus(Long senderId, ConnectionStatus status);
    List<Connection> findBySenderIdOrReceiverIdAndStatus(Long userId1, Long userId2, ConnectionStatus status);
    Optional<Connection> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
}

