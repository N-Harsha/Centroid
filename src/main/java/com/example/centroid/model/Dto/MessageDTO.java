package com.example.centroid.model.Dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class MessageDTO {
    @NotBlank
    private String messageText;
    private String fromUser;
    private LocalDateTime sentDateTime;

}
