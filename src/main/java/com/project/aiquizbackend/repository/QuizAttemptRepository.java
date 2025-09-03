package com.project.aiquizbackend.repository;


import com.project.aiquizbackend.model.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizAttemptRepository extends  JpaRepository<QuizAttempt, Long>{
	// Find all attempts by a specific user, ordered by the most recent first
    List<QuizAttempt> findByUserIdOrderByAttemptedAtDesc(Long userId);

}
