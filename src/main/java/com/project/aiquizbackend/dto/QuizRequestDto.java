package com.project.aiquizbackend.dto;

import lombok.Data;

@Data
public class QuizRequestDto {
	
	 private String topic;
	    private int numQuestions;
	    private String difficulty;
	    private String questionType;
}
