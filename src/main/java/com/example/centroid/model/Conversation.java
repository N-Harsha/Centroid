package com.example.centroid.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

//    boolean isGroup=false;

    @OneToMany(mappedBy = "conversation")
    List<Message> messages;
}
