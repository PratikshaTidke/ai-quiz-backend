package com.project.aiquizbackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users") // This will create a 'users' table
@Data

public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // We will store the encrypted password here


}
