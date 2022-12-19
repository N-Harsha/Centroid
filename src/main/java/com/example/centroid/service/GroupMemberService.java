package com.example.centroid.service;

import com.example.centroid.mapper.ConversationMapper;
import com.example.centroid.model.*;
import com.example.centroid.model.Dto.ConversationDTO;
import com.example.centroid.repository.ConversationRepository;
import com.example.centroid.repository.GroupMemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GroupMemberService {
    private final Logger logger = LoggerFactory.getLogger(GroupMemberService.class);
    @Autowired
    GroupMemberRepository groupMemberRepository;

    @Autowired
    ConversationRepository conversationRepository;
    @Autowired
    UserSessionService userSessionService;

    @Autowired
    ConversationMapper conversationMapper;

    public void resolveUserRequest(final UserRequest userRequest, final Conversation conversation){
        User sender = userRequest.getSender();
        User receiver = userRequest.getReceiver();
        GroupMember groupMemberSender =  groupMemberRepository.save(GroupMember.builder().user(sender).joinDate(LocalDateTime.now())
                .conversationName(receiver.getUsername()).conversation(conversation).leftDate(null).build());
        logger.info("group member for userRequest sender has been created, id : {}",groupMemberSender.getId());

        GroupMember groupMemberReceiver =  groupMemberRepository.save(GroupMember.builder().user(receiver).joinDate(LocalDateTime.now())
                .conversationName(sender.getUsername()).conversation(conversation).leftDate(null).build());

        logger.info("group member for userRequest receiver has been created, id : {}",groupMemberReceiver.getId());
    }

    public Boolean isMemberOfConversation(final User user,final Conversation conversation){
        Optional<GroupMember> possibleGroupMember =  groupMemberRepository.findByUserAndConversation(user,conversation);
        if(possibleGroupMember.isEmpty())
            return false;
        return true;
    }
    public List<ConversationDTO> getAllConversations(String sessionId){
        final UserSession fetchedUserSession = userSessionService.findUserSessionBySessionId(sessionId);
        final User user = fetchedUserSession.getUser();
        logger.info("fetching all the conversations for the user : {}",user.getId());
        final List<GroupMember> userGroups = groupMemberRepository.findAllByUserAndLeftDate(user,null);
        logger.info("fetched {} userGroups for the user : {}",userGroups.size(),user.getId());
        return conversationMapper.groupMembersToConversationDTOs(userGroups);
    }

}
