package com.example.centroid.repository;

import com.example.centroid.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long> {
    Optional<User> findUserByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("select user from User user where upper(user.username) like CONCAT('%',upper(:query),'%') ")
    Page<User> findDistinctUsersByUsername(String query, Pageable pageable);
}
