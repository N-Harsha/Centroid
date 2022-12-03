package com.example.centroid.repository;

import com.example.centroid.model.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message,Long> {

}
