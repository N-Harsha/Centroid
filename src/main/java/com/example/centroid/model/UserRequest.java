package com.example.centroid.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @OneToOne
    User sender;
    @ManyToOne
    User receiver;
    LocalDateTime creationDateTime;
    LocalDateTime modificationDateTime;
    @OneToOne
    UserRequestStatus userRequestStatus;
}
