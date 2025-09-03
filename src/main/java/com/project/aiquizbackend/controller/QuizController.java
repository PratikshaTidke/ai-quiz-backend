package com.project.aiquizbackend.controller;

import com.project.aiquizbackend.dto.QuizRequestDto;


import com.project.aiquizbackend.model.Quiz;
import com.project.aiquizbackend.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/v1/quiz")
@CrossOrigin("http://localhost:3000")

public class QuizController {
	
	private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generateQuiz(@RequestBody QuizRequestDto quizRequest) {
        try {
            Quiz savedQuiz = quizService.generateAndSaveQuiz(quizRequest);
            return ResponseEntity.ok(savedQuiz);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error generating quiz: " + e.getMessage());
        }
    }

}
