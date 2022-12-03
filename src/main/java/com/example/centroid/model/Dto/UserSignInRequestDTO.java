package com.example.centroid.model.Dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserSignInRequestDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
