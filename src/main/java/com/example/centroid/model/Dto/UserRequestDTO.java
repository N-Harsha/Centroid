package com.example.centroid.model.Dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String sender;
    private String receiver;
    //todo have to add the request type.
}
