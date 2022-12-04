package com.example.centroid.repository;

import com.example.centroid.model.UserRequestStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRequestStatusRepository  extends CrudRepository<UserRequestStatus,Long> {

    Optional<UserRequestStatus> findByStatus(String status);
}
