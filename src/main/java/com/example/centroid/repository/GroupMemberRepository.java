package com.example.centroid.repository;

import com.example.centroid.model.Conversation;
import com.example.centroid.model.GroupMember;
import com.example.centroid.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends CrudRepository<GroupMember,Long> {
    @Query("select gm from GroupMember gm where gm.user= (:user) and gm.conversation= (:conversation)")
    Optional<GroupMember> findByUserAndConversation(@Param("user") User user,@Param("conversation") Conversation conversation);

    List<GroupMember> findAllByUserAndLeftDate(User user, LocalDateTime dateTime);
}
