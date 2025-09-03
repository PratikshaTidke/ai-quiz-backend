package com.project.aiquizbackend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_attempts")
@Data
public class QuizAttempt {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String topic;
    private int score;
    private int totalQuestions;
    
    @Column(name = "attempted_at")
    private LocalDateTime attemptedAt = LocalDateTime.now();

}
