package com.example.centroid.service;

import com.example.centroid.exceptions.CustomException;
import com.example.centroid.model.Conversation;
import com.example.centroid.model.Dto.ApiError;
import com.example.centroid.model.Dto.ApiSuccess;
import com.example.centroid.model.Dto.MessageDTO;
import com.example.centroid.model.Message;
import com.example.centroid.model.User;
import com.example.centroid.model.UserSession;
import com.example.centroid.repository.ConversationRepository;
import com.example.centroid.repository.MessageRepository;
import com.example.centroid.utils.ErrorEnum;
import com.example.centroid.utils.SuccessEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MessageService{
    private final Logger logger = LoggerFactory.getLogger(MessageService.class);
    @Autowired
    UserSessionService userSessionService;

    @Autowired
    GroupMemberService groupMemberService;

    @Autowired
    ConversationRepository conversationRepository;

    @Autowired
    MessageRepository messageRepository;

    public void validateMessageDTO(MessageDTO messageDTO){
        //todo do this latter.
    }

    public Message sendMessage(@NotNull final String sessionId, final Long id, final MessageDTO messageDTO){
        UserSession fetchedUserSession = userSessionService.findUserSessionBySessionId(sessionId);
        User user = fetchedUserSession.getUser();
        Optional<Conversation> possibleConversation = conversationRepository.findById(id);
        if(possibleConversation.isEmpty()){
            logger.info("Conversation {} does not exist",id);
            ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST, ErrorEnum.CONVERSATION_DOSE_NOT_EXIST.getMessage(),
                    ErrorEnum.CONVERSATION_DOSE_NOT_EXIST.getCode(),null);
            throw new CustomException(errorResponse);
        }

        Conversation conversation = possibleConversation.get();

        if(!groupMemberService.isMemberOfConversation(user,conversation)){
            logger.info("User {} is not a member of conversation {} ",user.getId(),id);
            ApiError errorResponse = new ApiError(HttpStatus.UNAUTHORIZED, ErrorEnum.USER_NOT_MEMBER_OF_CONVERSATION.getMessage(),
                    ErrorEnum.USER_NOT_MEMBER_OF_CONVERSATION.getCode(),null);
            throw new CustomException(errorResponse);
        }
        validateMessageDTO(messageDTO);
        Message message = Message.builder().messageText(messageDTO.getMessageText())
                .conversation(conversation).fromUser(user).sentDateTime(LocalDateTime.now()).build();
        Message savedMessage =  messageRepository.save(message);
        logger.info("message {}  has been saved by the user {}",savedMessage.getId(),user.getId());
        return savedMessage;
    }

}
