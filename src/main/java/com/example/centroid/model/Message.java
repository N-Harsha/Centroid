package com.example.centroid.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    //todo after the messaging is working add the message status.
}
