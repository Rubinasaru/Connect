package com.example.demo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.User;


@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
	
	Optional<User> findById(Long id);

	boolean existsById(Long id);

	void deleteById(Long id);

	boolean existsByEmail(String email);

	Optional<User> findByEmail(String email);

	Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);
}
