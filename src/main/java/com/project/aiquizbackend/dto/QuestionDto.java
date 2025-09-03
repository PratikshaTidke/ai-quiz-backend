package com.project.aiquizbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class QuestionDto {
	 @JsonProperty("question_text")
	    private String questionText;
	    private List<String> options;
	    @JsonProperty("correct_answer")
	    private String correctAnswer;

}
