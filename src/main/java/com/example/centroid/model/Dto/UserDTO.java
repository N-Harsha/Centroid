package com.example.centroid.model.Dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
}
