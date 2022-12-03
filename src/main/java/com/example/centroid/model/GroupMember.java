package com.example.centroid.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
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

}
