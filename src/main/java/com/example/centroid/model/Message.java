package com.example.centroid.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String messageText;
    LocalDateTime sentDateTime;
    @OneToOne
    User fromUser;
    @ManyToOne
    @JoinColumn(name = "conversation_id",nullable = false)
    Conversation conversation;
}
