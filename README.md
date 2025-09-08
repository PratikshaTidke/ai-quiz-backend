# QuizMind-API

This is the Spring Boot REST API for an AI-powered quiz platform. It handles user registration and login with JWT authentication, generates dynamic quizzes using the OpenAI API, and tracks user score history.

## Technologies Used
- Java 17 & Spring Boot
- Spring Security & JWT
- Spring Data JPA & MySQL
- Maven
- OpenAI API

## Setup and Run

1.  Clone the repository.
2.  Set up your MySQL database and update the `application.properties` file with your database URL, username, and password.
3.  Add your OpenAI API key and JWT secret key to `application.properties`.
4.  Run the application from your IDE or by using `mvn spring-boot:run`.