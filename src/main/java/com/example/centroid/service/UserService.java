package com.example.centroid.service;

import com.example.centroid.jwt.JWTUtils;
import com.example.centroid.model.Dto.UserSignInResponseDTO;
import com.example.centroid.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    JWTUtils jwtUtils;

    @Autowired
    UserSessionService userSessionService;

    public UserSignInResponseDTO getUserSignInResponseDTO(String username, Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String jwtToken = jwtUtils.generateJwtToken(authentication);
        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        final User user = new User();
        user.setId(userDetails.getId());
        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        user.setEmail(userDetails.getEmail());
        user.setLastName(userDetails.getLastName());
        user.setFirstName(userDetails.getFirstName());

        final String refreshToken = userSessionService.createUserSession(user);
        logger.info("User : {} logging in to the application", user.getId());
        return new UserSignInResponseDTO(jwtToken,refreshToken,userDetails.getUsername(),user.getFirstName(),user.getLastName());
    }
}
