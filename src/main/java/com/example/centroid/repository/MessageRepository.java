package com.example.centroid.repository;

import com.example.centroid.model.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message,Long> {
    List<Message> findAllByConversation_Id(Long id);

}
