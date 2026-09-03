package com.Zest.product_assesment.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users") // 'user' is often a reserved keyword in PostgreSQL
@Getter @Setter @AllArgsConstructor @Builder @RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public User(String yogi, String number) {
        this.username = yogi;
        this.password = number;
    }
}