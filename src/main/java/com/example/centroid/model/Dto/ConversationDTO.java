package com.example.centroid.model.Dto;

import lombok.Data;

@Data
public class ConversationDTO {
    String conversationName;
    Long conversationId;
    Long newMessages=0L;
}
