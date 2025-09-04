package com.project.aiquizbackend.service;

import com.project.aiquizbackend.dto.QuestionDto;
import com.project.aiquizbackend.dto.QuizRequestDto;
import com.project.aiquizbackend.model.Question;
import com.project.aiquizbackend.model.Quiz;
import com.project.aiquizbackend.repository.QuizRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service

public class QuizService {
	

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.model}")
    private String model;

    private final QuizRepository quizRepository;
    private final ObjectMapper objectMapper;

    public QuizService(QuizRepository quizRepository, ObjectMapper objectMapper) {
        this.quizRepository = quizRepository;
        this.objectMapper = objectMapper;
    }

    public Quiz generateAndSaveQuiz(QuizRequestDto requestDto) {
        String prompt = createPrompt(requestDto);
        String aiResponse = callOpenAiApi(prompt);
        List<QuestionDto> questionDtos = parseAiResponse(aiResponse);
        return saveQuiz(requestDto, questionDtos);
    }

    private String createPrompt(QuizRequestDto request) {
        return String.format(
            "Generate a quiz with the following criteria: Topic='%s', Number of Questions=%d, Difficulty='%s', Type='%s'. " +
            "Your response MUST be a valid JSON array of objects. Do not include any text before or after the JSON array. " +
            "Each object must have three keys: 'question_text' (string), 'options' (an array of 4 strings for MCQ, or an empty array for short-answer), and 'correct_answer' (string).",
            request.getTopic(), request.getNumQuestions(), request.getDifficulty(), request.getQuestionType()
        );
    }

    private String callOpenAiApi(String prompt) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        Map<String, Object> message = Map.of("role", "user", "content", prompt);
        Map<String, Object> body = Map.of("model", model, "messages", List.of(message));
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        Map<String, Object> response = restTemplate.postForObject(apiUrl, entity, Map.class);
        return (String) ((Map<?, ?>) ((Map<?, ?>) ((List<?>) response.get("choices")).get(0)).get("message")).get("content");
    }

    private List<QuestionDto> parseAiResponse(String aiResponse) {
        try {
            String cleanJson = aiResponse.trim().replace("```json", "").replace("```", "");
            return objectMapper.readValue(cleanJson, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response.", e);
        }
    }

    private Quiz saveQuiz(QuizRequestDto requestDto, List<QuestionDto> questionDtos) {
        Quiz quiz = new Quiz();
        quiz.setTopic(requestDto.getTopic());
        quiz.setDifficulty(requestDto.getDifficulty());
        List<Question> questions = questionDtos.stream().map(dto -> {
            Question q = new Question();
            q.setQuestionText(dto.getQuestionText());
            q.setCorrectAnswer(dto.getCorrectAnswer());
            try {
                q.setOptions(objectMapper.writeValueAsString(dto.getOptions()));
            } catch (Exception e) {
                throw new RuntimeException("Error converting options to JSON string.", e);
            }
            q.setQuiz(quiz);
            return q;
        }).collect(Collectors.toList());
        quiz.setQuestions(questions);
        return quizRepository.save(quiz);
    }

}
