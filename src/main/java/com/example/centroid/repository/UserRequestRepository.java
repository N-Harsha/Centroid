package com.example.centroid.repository;

import com.example.centroid.model.User;
import com.example.centroid.model.UserRequest;
import com.example.centroid.model.UserRequestStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRequestRepository extends CrudRepository<UserRequest,Long> {
    List<UserRequest> findAllBySenderAndUserRequestStatus(User sender, UserRequestStatus userRequestStatus);
    List<UserRequest> findAllByReceiverAndUserRequestStatus(User receiver,UserRequestStatus userRequestStatus);
    Optional<UserRequest> findAllBySenderAndReceiverAndUserRequestStatus(User sender, User receiver, UserRequestStatus userRequestStatus);
}
