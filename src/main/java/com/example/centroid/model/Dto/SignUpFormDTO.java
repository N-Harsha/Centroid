package com.example.centroid.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpFormDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String verifyEmail;
    private String password;
    private String verifyPassword;
}
