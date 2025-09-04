package com.project.aiquizbackend.controller;


import com.project.aiquizbackend.model.QuizAttempt;
import com.project.aiquizbackend.model.User;
import com.project.aiquizbackend.repository.QuizAttemptRepository;
import com.project.aiquizbackend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/history")
public class HistoryController {
	
	private final QuizAttemptRepository quizAttemptRepository;
    private final UserRepository userRepository;

    public HistoryController(QuizAttemptRepository quizAttemptRepository, UserRepository userRepository) {
        this.quizAttemptRepository = quizAttemptRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveQuizAttempt(@RequestBody Map<String, Object> payload, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        QuizAttempt attempt = new QuizAttempt();
        attempt.setUser(user);
        attempt.setTopic((String) payload.get("topic"));
        attempt.setScore((Integer) payload.get("score"));
        attempt.setTotalQuestions((Integer) payload.get("totalQuestions"));
        
        quizAttemptRepository.save(attempt);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<QuizAttempt>> getHistory(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<QuizAttempt> history = quizAttemptRepository.findByUserIdOrderByAttemptedAtDesc(user.getId());
        return ResponseEntity.ok(history);
    }

}
