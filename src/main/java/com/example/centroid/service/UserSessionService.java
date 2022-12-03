package com.example.centroid.service;

import com.example.centroid.exceptions.CustomException;
import com.example.centroid.model.Dto.ApiError;
import com.example.centroid.model.User;
import com.example.centroid.model.UserSession;
import com.example.centroid.repository.UserSessionRepository;
import com.example.centroid.utils.DateTimeUtils;
import com.example.centroid.utils.ErrorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class UserSessionService {
    private static final Logger logger = LoggerFactory.getLogger(UserSessionService.class);

    @Value("${centroid.app.refreshExpiryDays}")
    private int refreshExpiryDays;

    @Autowired
    private UserSessionRepository userSessionRepository;

    //todo cacheInvalidatorService to be added here.

    public String createUserSession(User user) throws CustomException{
        String refreshToken = UUID.randomUUID().toString();
        Date sessionExpiry = Date.from(DateTimeUtils.dateTimeInIST().plusDays(refreshExpiryDays).toInstant());
        UserSession userSession = UserSession.builder()
                .user(user)
                .sessionExpiry(sessionExpiry)
                .sessionId(refreshToken)
                .build();
        try{
            userSessionRepository.save(userSession);
            return refreshToken;
        } catch (Exception e){
            ApiError errorResponse = new ApiError(HttpStatus.ACCEPTED, ErrorEnum.USER_SESSION_CREATION_FAILED.getMessage(),ErrorEnum.USER_SESSION_CREATION_FAILED.getCode(), null);
            throw new CustomException(errorResponse);
        }
    }

//    @CachePut(value = "UserSession",key="#userSession.sessionId")
    public UserSession updateUserSessionExpiry(UserSession userSession) throws CustomException{
        final Date sessionExpiry = Date.from(DateTimeUtils.dateTimeInIST().plusDays(refreshExpiryDays).toInstant());
        userSession.setSessionExpiry(sessionExpiry);
        try{
            return userSessionRepository.save(userSession);
        }catch (RuntimeException e){
            logger.info("Unable to login user {} with error {}",userSession.getUser(),e.getMessage());
            throw new CustomException("Unable to login",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @Cacheable(value = "UserSession", key = "#sessionId")
    public UserSession findUserSessionBySessionId(String sessionId) throws CustomException {
        logger.info("Get session details from DB: {}", sessionId);
        return userSessionRepository.findUserSessionBySessionId(sessionId)
                .<CustomException>orElseThrow(() -> {
                    logger.warn("Unable to find session : {}", sessionId);
                    final ApiError errorResponse = new ApiError(HttpStatus.UNAUTHORIZED, ErrorEnum.USER_SESSION_EXPIRED.getMessage(),
                            ErrorEnum.USER_SESSION_EXPIRED.getCode(), null);
                    throw new CustomException(errorResponse);
                });
    }

    //todo add the cache invalidation code here afterwards.
}
