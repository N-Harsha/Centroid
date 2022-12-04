package com.example.centroid.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    LocalDateTime joinDate;
    LocalDateTime leftDate;
    @ManyToOne
    User user;
    @ManyToOne
    Conversation conversation;

    String conversationName;

}
