package com.example.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Enums.ConnectionStatus;
import com.example.demo.Models.Connections;
import com.example.demo.Models.User;

@Repository
public interface ConnectionRepository extends JpaRepository <Connections,Long> {

	Optional<Connections> findBySenderAndReceiver(User sender, User receiver);

	List<Connections> findBySenderOrReceiverAndStatus(User user, User user2, ConnectionStatus accepted);

	List<Connections> findByReceiverAndStatus(User receiver, ConnectionStatus pending);

}
