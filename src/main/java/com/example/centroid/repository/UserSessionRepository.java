package com.example.centroid.repository;

import com.example.centroid.model.UserSession;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserSessionRepository extends CrudRepository<UserSession,Long> {
    Optional<UserSession> findUserSessionBySessionId(String sessionId);
}
