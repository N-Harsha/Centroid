package com.example.centroid.model.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserSignInResponseDTO {

    private String accessToken;
    private String refreshToken;
    private String username;
    private String firstName;
    private String lastName;



}
