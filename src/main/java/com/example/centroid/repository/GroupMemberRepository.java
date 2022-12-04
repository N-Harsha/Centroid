package com.example.centroid.repository;

import com.example.centroid.model.Conversation;
import com.example.centroid.model.GroupMember;
import com.example.centroid.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GroupMemberRepository extends CrudRepository<GroupMember,Long> {
    @Query("select gm from GroupMember gm where gm.user=:user and gm.conversation=:conversation")
    Optional<GroupMember> findByUserAndConversation(User user, Conversation conversation);
}
