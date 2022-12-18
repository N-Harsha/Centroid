package com.example.centroid.api.v1;

import com.example.centroid.exceptions.CustomException;
import com.example.centroid.jwt.JWTUtils;
import com.example.centroid.model.Dto.ApiError;
import com.example.centroid.model.Dto.SignUpFormDTO;
import com.example.centroid.model.Dto.UserSignInResponseDTO;
import com.example.centroid.model.Dto.UserSignInRequestDTO;
import com.example.centroid.model.User;
import com.example.centroid.model.UserSession;
import com.example.centroid.service.UserDetailsImpl;
import com.example.centroid.service.UserDetailsServiceImpl;
import com.example.centroid.service.UserService;
import com.example.centroid.service.UserSessionService;
import com.example.centroid.utils.ErrorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("api/auth")
public class LoginApi {
    private final Logger logger = LoggerFactory.getLogger(LoginApi.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserSessionService userSessionService;

    @PostMapping("/login")
    public UserSignInResponseDTO loginUser(@RequestBody UserSignInRequestDTO userSignInRequestDTO)
    throws CustomException {
        Authentication authentication = null;
        try{
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userSignInRequestDTO.getUsername(),
                            userSignInRequestDTO.getPassword()));
        } catch (AuthenticationException e){
            logger.warn("Attempted login for username : {} with bad credentials",userSignInRequestDTO.getUsername());
            final ApiError errorResponse = new ApiError(HttpStatus.UNAUTHORIZED, ErrorEnum.BAD_CREDENTIALS.getMessage(),ErrorEnum.BAD_CREDENTIALS.getCode(), null);
            throw new CustomException(errorResponse);
        }
        return userService.getUserSignInResponseDTO(userSignInRequestDTO.getUsername(),authentication);
    }

    @PostMapping("/user-registration")
    public ResponseEntity<Object> userRegistration(@RequestBody SignUpFormDTO signUpFormDTO)throws CustomException{
        return userService.userRegistration(signUpFormDTO);
    }


   @PutMapping("/refresh")
    public UserSignInResponseDTO refreshSession(
            @RequestHeader("session") String session)
            throws CustomException {
        final UserSession fetchedSession = userSessionService.findUserSessionBySessionId(session);
        final User user = fetchedSession.getUser();

        if (fetchedSession.getSessionExpiry().compareTo(new Date()) < 0) {
            logger.warn("Deleting expired session : {} for user : {}", fetchedSession.getSessionId(), user.getId());
            final ApiError errorResponse = new ApiError(HttpStatus.UNAUTHORIZED,
                    ErrorEnum.USER_SESSION_EXPIRED.getMessage(),
                    ErrorEnum.USER_SESSION_EXPIRED.getCode(), null);
            throw new CustomException(errorResponse);
        }
        final UserDetailsImpl userDetails = userDetailsService
                .getUserByUsername(fetchedSession.getUser().getUsername());

        userSessionService.updateUserSessionExpiry(fetchedSession);
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                null, userDetails.getAuthorities());
        String jwtToken = jwtUtils.generateJwtToken(authentication);
        logger.info("Refreshing access token for user : {}", user.getId());
        return new UserSignInResponseDTO(jwtToken,
                fetchedSession.getSessionId(),
                userDetails.getUsername(),user.getFirstName(),user.getLastName());
    }


}
