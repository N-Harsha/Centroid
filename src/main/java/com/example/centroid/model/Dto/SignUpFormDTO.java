package com.example.centroid.model.Dto;

import lombok.Data;

@Data
public class SignUpFormDTO {
    private static final long serialVersionUID = 1L;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String verifyEmail;
    private String password;
    private String verifyPassword;
}
