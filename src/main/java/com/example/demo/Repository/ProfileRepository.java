package com.example.demo.Repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.Enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.UserProfile;

@Repository
public interface ProfileRepository extends JpaRepository<UserProfile,Integer> {

	Optional<UserProfile> findById(Long userId);

	Optional<UserProfile> findByEmail(String email);

    boolean existsByEmail(String email);

	List<UserProfile> findByRole(UserType mentor);
}
