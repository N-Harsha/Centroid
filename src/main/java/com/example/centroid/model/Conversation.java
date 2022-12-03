package com.example.centroid.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String conversationName;
    @OneToMany(mappedBy = "conversation")
    List<Message> messages;
}
