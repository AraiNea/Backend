package com.example.pizza_backend.api.dto.input;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileInput {
    //Profile
    private String username;
    private String password;
    private String profileName;
    private String profileSname;
    private Integer profileRole;
    private LocalDate createdAt;

    //Address
    private String phone;
    private String province;
    private String amphor;
    private String district;
    private String zipCode;
    private String addrNum;
    private String detail;
    private String receivedName;

}
