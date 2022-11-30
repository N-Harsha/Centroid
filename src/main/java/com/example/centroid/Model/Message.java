package com.example.centroid.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String messageText;
    LocalDateTime sentDateTime;
    boolean viewed;
    boolean delivered;//todo change these boolean to enums
}
