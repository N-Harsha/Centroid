package com.example.centroid.service;

import com.example.centroid.constants.Constants;
import com.example.centroid.exceptions.CustomException;
import com.example.centroid.mapper.UserRequestMapper;
import com.example.centroid.model.*;
import com.example.centroid.model.Dto.ApiError;
import com.example.centroid.model.Dto.ApiSuccess;
import com.example.centroid.model.Dto.UserRequestDTO;
import com.example.centroid.repository.ConversationRepository;
import com.example.centroid.repository.UserRequestRepository;
import com.example.centroid.repository.UserRequestStatusRepository;
import com.example.centroid.utils.ErrorEnum;
import com.example.centroid.utils.SuccessEnum;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserRequestService {
    private final Logger logger = LoggerFactory.getLogger(UserRequestService.class);
    @Autowired
    UserSessionService userSessionService;

    @Autowired
    UserService userService;

    @Autowired
    UserRequestStatusRepository userRequestStatusRepository;

    @Autowired
    UserRequestRepository userRequestRepository;

    @Autowired
    ConversationRepository conversationRepository;

    @Autowired
    GroupMemberService groupMemberService;

    @Autowired
    UserRequestMapper userRequestMapper;

    @Transactional
    public ResponseEntity<Object> sendUserRequest(@NonNull final String sessionId, final Long id){
        final UserSession fetchedSession = userSessionService.findUserSessionBySessionId(sessionId);
        final User user = fetchedSession.getUser();
        logger.info("sending User Request from {} user to {} user",user.getId(),id);
        if(user.getId()==id){
            logger.info("user {} made a self request",id);
            ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST, ErrorEnum.INVALID_USER_REQUEST.getMessage(),
                    ErrorEnum.INVALID_USER_REQUEST.getCode(),null);
            throw new CustomException(errorResponse);
        }

        UserRequestStatus pendingUserRequestStatus = userRequestStatusRepository.findByStatus(Constants.USER_REQUEST_PENDING).get();
        Optional<User> possibleReceiver = userService.getUserById(id);
        if(possibleReceiver.isEmpty()){
            logger.info("user {} , doesn't exits...",id);
            ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST, ErrorEnum.INVALID_USER_REQUEST.getMessage(),
                    ErrorEnum.INVALID_USER_REQUEST.getCode(),null);
            throw new CustomException(errorResponse);
        }
        User receiver = possibleReceiver.get();
        Optional<UserRequest> existingRequestPending = userRequestRepository.findAllBySenderAndReceiverAndUserRequestStatus(user,receiver,userRequestStatusRepository.findByStatus(Constants.USER_REQUEST_PENDING).get());
        if(existingRequestPending.isPresent()){
            logger.info("A request already exists between sender : {} and receiver : {}",user.getId(),id);
            ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST, ErrorEnum.USER_REQUEST_IN_PENDING_STATUS.getMessage(),
                    ErrorEnum.USER_REQUEST_IN_PENDING_STATUS.getCode(),null);
            throw new CustomException(errorResponse);
        }
        Optional<UserRequest> existingRequestAccepted = userRequestRepository.findAllBySenderAndReceiverAndUserRequestStatus(user,receiver,userRequestStatusRepository.findByStatus(Constants.USER_REQUEST_ACCEPTED).get());
        if(existingRequestAccepted.isPresent()){
            logger.info(" sender : {} and receiver : {} are already friends",user.getId(),id);
            ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST, ErrorEnum.USER_REQUEST_IN_Accepted_STATUS.getMessage(),
                    ErrorEnum.USER_REQUEST_IN_Accepted_STATUS.getCode(),null);
            throw new CustomException(errorResponse);
        }
        existingRequestAccepted = userRequestRepository.findAllBySenderAndReceiverAndUserRequestStatus(receiver,user,userRequestStatusRepository.findByStatus(Constants.USER_REQUEST_ACCEPTED).get());
        if(existingRequestAccepted.isPresent()){
            logger.info(" sender : {} and receiver : {} are already friends",user.getId(),id);
            ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST, ErrorEnum.USER_REQUEST_IN_Accepted_STATUS.getMessage(),
                    ErrorEnum.USER_REQUEST_IN_Accepted_STATUS.getCode(),null);
            throw new CustomException(errorResponse);
        }
        UserRequest userRequest = UserRequest.builder().sender(user).receiver(receiver)
                .userRequestStatus(pendingUserRequestStatus).creationDateTime(LocalDateTime.now()).modificationDateTime(LocalDateTime.now()).build();
        UserRequest savedUserRequest = userRequestRepository.save(userRequest);
        logger.info("user request {} from user {} sent successfully to user {}",savedUserRequest.getId(),user.getId(),id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiSuccess.builder().message(SuccessEnum.USER_REQUEST_SENT_SUCCESSFULLY.getMessage()).build());
    }

    @Transactional
    public ResponseEntity<Object> acceptUserRequest(@NonNull final String sessionId,final Long id) {
        final UserSession fetchedUserSession = userSessionService.findUserSessionBySessionId(sessionId);
        final User user = fetchedUserSession.getUser();
        logger.info("accepting User request {} by user {}",id,user.getId());
        Optional<UserRequest> possibleUserRequest = userRequestRepository.findById(id);
        if(possibleUserRequest.isEmpty()){
            logger.info("the user request {} is not found",id);
            ApiError errorResponse = new ApiError(HttpStatus.FORBIDDEN,ErrorEnum.USER_REQUEST_NOT_FOUND.getMessage(),
                    ErrorEnum.USER_REQUEST_NOT_FOUND.getCode(), null);
            throw new CustomException(errorResponse);
        }
        UserRequest userRequest = possibleUserRequest.get();

        if(userRequest.getReceiver().getId()!=user.getId()){
            logger.info("User {} tried to be update User Request {}",id,user.getId());
            ApiError errorResponse = new ApiError(HttpStatus.FORBIDDEN,ErrorEnum.UNAUTHORIZED_USER_REQUEST_UPDATE.getMessage(),
                    ErrorEnum.UNAUTHORIZED_USER_REQUEST_UPDATE.getCode(),null);
            throw new CustomException(errorResponse);
        }

        if(!userRequest.getUserRequestStatus().getStatus().equals(Constants.USER_REQUEST_PENDING)){
            logger.info("User {} tried to be update User Request {} which is already {}",id,user.getId(),userRequest.getUserRequestStatus());
            ApiError errorResponse = new ApiError(HttpStatus.FORBIDDEN,ErrorEnum.USER_REQUEST_ALREADY_UPDATED.getMessage(),
                    ErrorEnum.USER_REQUEST_ALREADY_UPDATED.getCode(),null);
            throw new CustomException(errorResponse);
        }

        UserRequestStatus acceptedUserRequestStatus = userRequestStatusRepository.findByStatus(Constants.USER_REQUEST_ACCEPTED).get();
        userRequest.setUserRequestStatus(acceptedUserRequestStatus);
        userRequest.setModificationDateTime(LocalDateTime.now());
        userRequestRepository.save(userRequest);
        logger.info("User Request {} accepted Successfully",id);

        logger.info("Creating conversation between user {} and user {}",userRequest.getSender().getId(),userRequest.getReceiver().getId());

        Conversation conversation = new Conversation();
        Conversation savedConversation = conversationRepository.save(conversation);
        logger.info("new conversation has been created : {}",savedConversation.getId());

        groupMemberService.resolveUserRequest(userRequest,savedConversation);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiSuccess.builder().message(SuccessEnum.USER_REQUEST_ACCEPTED_SUCCESSFULLY.getMessage()).build());
    }
    public ResponseEntity<Object> rejectUserRequest(@NonNull final String sessionId,final Long id) {
        final UserSession fetchedUserSession = userSessionService.findUserSessionBySessionId(sessionId);
        final User user = fetchedUserSession.getUser();
        logger.info("rejecting User request {} by user {}",id,user.getId());
        Optional<UserRequest> possibleUserRequest = userRequestRepository.findById(id);
        if(possibleUserRequest.isEmpty()){
            logger.info("the user request {} is not found",id);
            ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST,ErrorEnum.USER_REQUEST_NOT_FOUND.getMessage(),
                    ErrorEnum.USER_REQUEST_NOT_FOUND.getCode(), null);
            throw new CustomException(errorResponse);
        }
        UserRequest userRequest = possibleUserRequest.get();

        if(!Objects.equals(userRequest.getReceiver().getId(), user.getId())){
            logger.info("User {} tried to be update User Request {}",id,user.getId());
            ApiError errorResponse = new ApiError(HttpStatus.UNAUTHORIZED,ErrorEnum.UNAUTHORIZED_USER_REQUEST_UPDATE.getMessage(),
                    ErrorEnum.UNAUTHORIZED_USER_REQUEST_UPDATE.getCode(),null);
            throw new CustomException(errorResponse);
        }

        if(!userRequest.getUserRequestStatus().getStatus().equals(Constants.USER_REQUEST_PENDING)){
            logger.info("User {} tried to be update User Request {} which is already {}",id,user.getId(),userRequest.getUserRequestStatus());
            ApiError errorResponse = new ApiError(HttpStatus.FORBIDDEN,ErrorEnum.USER_REQUEST_ALREADY_UPDATED.getMessage(),
                    ErrorEnum.USER_REQUEST_ALREADY_UPDATED.getCode(),null);
            throw new CustomException(errorResponse);
        }

        UserRequestStatus rejectedUserRequestStatus = userRequestStatusRepository.findByStatus(Constants.USER_REQUEST_REJECTED).get();
        userRequest.setUserRequestStatus(rejectedUserRequestStatus);
        userRequest.setModificationDateTime(LocalDateTime.now());
        userRequestRepository.save(userRequest);
        logger.info("User Request {} rejected Successfully",id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiSuccess.builder().message(SuccessEnum.USER_REQUEST_REJECTED_SUCCESSFULLY.getMessage()).build());
    }

    public ResponseEntity<Object> cancelUserRequest(@NonNull final String sessionId,final Long id) {
        final UserSession fetchedUserSession = userSessionService.findUserSessionBySessionId(sessionId);
        final User user = fetchedUserSession.getUser();
        logger.info("canceling User request {} by user {}",id,user.getId());
        Optional<UserRequest> possibleUserRequest = userRequestRepository.findById(id);
        if(possibleUserRequest.isEmpty()){
            logger.info("the user request {} is not found",id);
            ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST,ErrorEnum.USER_REQUEST_NOT_FOUND.getMessage(),
                    ErrorEnum.USER_REQUEST_NOT_FOUND.getCode(), null);
            throw new CustomException(errorResponse);
        }
        UserRequest userRequest = possibleUserRequest.get();

        if(!Objects.equals(userRequest.getSender().getId(), user.getId())){
            logger.info("User {} tried to be update User Request {}",id,user.getId());
            ApiError errorResponse = new ApiError(HttpStatus.UNAUTHORIZED,ErrorEnum.UNAUTHORIZED_USER_REQUEST_UPDATE.getMessage(),
                    ErrorEnum.UNAUTHORIZED_USER_REQUEST_UPDATE.getCode(),null);
            throw new CustomException(errorResponse);
        }

        if(!userRequest.getUserRequestStatus().getStatus().equals(Constants.USER_REQUEST_PENDING)){
            logger.info("User {} tried to be update User Request {} which is already {}",id,user.getId(),userRequest.getUserRequestStatus());
            ApiError errorResponse = new ApiError(HttpStatus.FORBIDDEN,ErrorEnum.USER_REQUEST_ALREADY_UPDATED.getMessage(),
                    ErrorEnum.USER_REQUEST_ALREADY_UPDATED.getCode(),null);
            throw new CustomException(errorResponse);
        }

        UserRequestStatus rejectedUserRequestStatus = userRequestStatusRepository.findByStatus(Constants.USER_REQUEST_CANCELLED).get();
        userRequest.setUserRequestStatus(rejectedUserRequestStatus);
        userRequest.setModificationDateTime(LocalDateTime.now());
        userRequestRepository.save(userRequest);
        logger.info("User Request {} cancelled Successfully",id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiSuccess.builder().message(SuccessEnum.USER_REQUEST_CANCELED_SUCCESSFULLY.getMessage()).build());
    }

    public List<UserRequestDTO> sentUserRequests(String sessionId){
        final UserSession fetchedUserSession = userSessionService.findUserSessionBySessionId(sessionId);
        final User user = fetchedUserSession.getUser();
        logger.info("fetching the user request sent by the user : {}",user.getId());
        List<UserRequest> userRequestsSent = userRequestRepository.findAllBySenderAndUserRequestStatus(user,userRequestStatusRepository.findByStatus(Constants.USER_REQUEST_PENDING).get());
        logger.info("{} requests sent by user : {}",userRequestsSent.size(),user.getId());
        return userRequestMapper.usersToUserDTOs(userRequestsSent);
    }
    public List<UserRequestDTO> receivedUserRequests(String sessionId){
        final UserSession fetchedUserSession = userSessionService.findUserSessionBySessionId(sessionId);
        final User user = fetchedUserSession.getUser();
        logger.info("fetching the user request received by the user : {}",user.getId());
        List<UserRequest> userRequestsReceived = userRequestRepository.findAllByReceiverAndUserRequestStatus(user,userRequestStatusRepository.findByStatus(Constants.USER_REQUEST_PENDING).get());
        logger.info("{} requests sent by user : {}",userRequestsReceived.size(),user.getId());
        return userRequestMapper.usersToUserDTOs(userRequestsReceived);
    }

}
