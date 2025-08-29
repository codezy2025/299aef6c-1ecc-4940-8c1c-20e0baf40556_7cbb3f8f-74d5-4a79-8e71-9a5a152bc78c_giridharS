package com.java.coreTemplate.repository;


import com.java.coreTemplate.model.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDetails, Long> {
    Optional<UserDetails> findByUsername(String username);
    Optional<UserDetails> findByEmail(String email);
}