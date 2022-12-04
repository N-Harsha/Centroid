package com.example.centroid.model.Dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MessageDTO {
    @NotBlank
    private String messageText;

}
