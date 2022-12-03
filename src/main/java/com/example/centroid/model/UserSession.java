package com.example.centroid.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSession implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    private String sessionId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date sessionExpiry;

}
