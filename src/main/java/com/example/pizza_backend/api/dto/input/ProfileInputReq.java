package com.example.pizza_backend.api.dto.input;

import lombok.Data;

@Data
public class ProfileInputReq {
    private String username;
    private String password;
    private String profileName;
}
