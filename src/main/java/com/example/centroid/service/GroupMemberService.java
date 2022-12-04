package com.example.centroid.service;

import com.example.centroid.model.Conversation;
import com.example.centroid.model.GroupMember;
import com.example.centroid.model.User;
import com.example.centroid.model.UserRequest;
import com.example.centroid.repository.ConversationRepository;
import com.example.centroid.repository.GroupMemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class GroupMemberService {
    private final Logger logger = LoggerFactory.getLogger(GroupMemberService.class);
    @Autowired
    GroupMemberRepository groupMemberRepository;

    @Autowired
    ConversationRepository conversationRepository;

    public void resolveUserRequest(final UserRequest userRequest, final Conversation conversation){
        User sender = userRequest.getSender();
        User receiver = userRequest.getReceiver();
        GroupMember groupMemberSender =  groupMemberRepository.save(GroupMember.builder().user(sender).joinDate(LocalDateTime.now())
                .conversationName(receiver.getUsername()).conversation(conversation).leftDate(LocalDateTime.MAX).build());
        logger.info("group member for userRequest sender has been created, id : {}",groupMemberSender.getId());

        GroupMember groupMemberReceiver =  groupMemberRepository.save(GroupMember.builder().user(receiver).joinDate(LocalDateTime.now())
                .conversationName(sender.getUsername()).conversation(conversation).leftDate(LocalDateTime.MAX).build());

        logger.info("group member for userRequest receiver has been created, id : {}",groupMemberReceiver.getId());
    }

    public Boolean isMemberOfConversation(final User user,final Conversation conversation){
        Optional<GroupMember> possibleGroupMember =  groupMemberRepository.findByUserAndConversation(user,conversation);
        if(possibleGroupMember.isEmpty())
            return false;
        return true;
    }



}
