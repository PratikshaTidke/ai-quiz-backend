package com.project.aiquizbackend.repository;

import com.project.aiquizbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<User, Long>{
	// This method will be used by Spring Security to find a user by their username
    Optional<User> findByUsername(String username);


}
