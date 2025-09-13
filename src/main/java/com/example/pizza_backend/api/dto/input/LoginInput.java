package com.example.pizza_backend.api.dto.input;

import lombok.Data;

@Data
public class LoginInput {
    private String username;
    private String password;
}
