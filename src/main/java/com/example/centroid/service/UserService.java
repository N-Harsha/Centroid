package com.example.centroid.service;

import com.example.centroid.exceptions.CustomException;
import com.example.centroid.jwt.JWTUtils;
import com.example.centroid.model.Dto.ApiError;
import com.example.centroid.model.Dto.ApiSuccess;
import com.example.centroid.model.Dto.SignUpFormDTO;
import com.example.centroid.model.Dto.UserSignInResponseDTO;
import com.example.centroid.model.User;
import com.example.centroid.repository.UserRepository;
import com.example.centroid.utils.ErrorEnum;
import com.example.centroid.utils.SuccessEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserService {
    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    JWTUtils jwtUtils;

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    private void validateSignUpDTO(SignUpFormDTO signUpFormDTO) throws CustomException{
        final String USERNAME_PATTERN =
                "^[a-zA-Z0-9]([._](?![._])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$";
        //username contains A-Z , a-z and 0-9 and ._ and nothing else.
        //username cannot start or end with . or _
        //username cannot have the username cannot contain __ or ._ or _. within it.
        //min 5 chars long.
        //max 20 chars long.


        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$";
        //min 8 chars long and max 20 chars
        //should contain A-Z, a-z, 0-9 and special characters.
        //todo let the regex allow only limited special characters.

        final  String NAME_PATTERN = "^[\\p{L} .'-]+$";
        //\\p{L} is a Unicode Character Property that matches any kind of letter from any language

        final String EMAIL_PATTERN = "^(.+)@(.+)$";
        //can have anything and should contain @ in then middle and then some string.


       final Pattern usernamePattern = Pattern.compile(USERNAME_PATTERN);
       final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);
       final Pattern namePattern = Pattern.compile(NAME_PATTERN);
       final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);

       //todo write a regex for email. and write a condition.

        if(!signUpFormDTO.getEmail().equals(signUpFormDTO.getVerifyEmail())){
            ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST,ErrorEnum.VERIFY_EMAIL_MISMATCH.getMessage(),ErrorEnum.VERIFY_EMAIL_MISMATCH.getCode(), null);
            throw new CustomException(errorResponse);
        }

        if(!signUpFormDTO.getPassword().equals(signUpFormDTO.getVerifyPassword())){
            ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST,ErrorEnum.VERIFY_PASSWORD_MISMATCH.getMessage(),ErrorEnum.VERIFY_PASSWORD_MISMATCH.getCode(), null);
            throw new CustomException(errorResponse);
        }

        if(!usernamePattern.matcher(signUpFormDTO.getUsername()).matches()){
            ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST,ErrorEnum.INVALID_USERNAME.getMessage(),ErrorEnum.INVALID_USERNAME.getCode(), null);
            throw new CustomException(errorResponse);
        }
        if(!passwordPattern.matcher(signUpFormDTO.getPassword()).matches()){
            ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST,ErrorEnum.INVALID_PASSWORD.getMessage(),ErrorEnum.INVALID_PASSWORD.getCode(), null);
            throw new CustomException(errorResponse);
        }
        if(!emailPattern.matcher(signUpFormDTO.getEmail()).matches()){
            ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST,ErrorEnum.INVALID_EMAIL.getMessage(),ErrorEnum.INVALID_EMAIL.getCode(), null);
            throw new CustomException(errorResponse);
        }
        if(!namePattern.matcher(signUpFormDTO.getFirstName()).matches()){
            ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST,ErrorEnum.INVALID_FIRST_NAME.getMessage(),ErrorEnum.INVALID_FIRST_NAME.getCode(), null);
            throw new CustomException(errorResponse);
        }
        if(!namePattern.matcher(signUpFormDTO.getLastName()).matches()){
            ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST,ErrorEnum.INVALID_LAST_NAME.getMessage(),ErrorEnum.INVALID_LAST_NAME.getCode(), null);
            throw new CustomException(errorResponse);
        }
    }
    public ResponseEntity<Object> userRegistration(final SignUpFormDTO signUpFormDTO)throws CustomException{

        logger.info("New user registration has been invoked");

        validateSignUpDTO(signUpFormDTO);

        Boolean usernameExists = userRepository.existsByUsername(signUpFormDTO.getUsername());
        if(usernameExists){
            logger.info("Username already exits : {}",signUpFormDTO.getUsername());
            final ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST, ErrorEnum.DUPLICATE_USERNAME.getMessage(),
                    ErrorEnum.DUPLICATE_USERNAME.getCode(), null);
            throw new CustomException(errorResponse);
        }

        Boolean emailExists = userRepository.existsByEmail(signUpFormDTO.getUsername());
        if(emailExists){
            logger.info("Username already exits : {}",signUpFormDTO.getUsername());
            final ApiError errorResponse = new ApiError(HttpStatus.BAD_REQUEST, ErrorEnum.DUPLICATE_EMAIL.getMessage(),
                    ErrorEnum.DUPLICATE_EMAIL.getCode(), null);
            throw new CustomException(errorResponse);
        }

        User user = new User();

        user.setFirstName(signUpFormDTO.getFirstName());
        user.setLastName(signUpFormDTO.getLastName());
        user.setEmail(signUpFormDTO.getEmail());
        user.setUsername(signUpFormDTO.getUsername());
        user.setPassword(encoder.encode(signUpFormDTO.getPassword()));
        final User registeredUser = userRepository.save(user);
        logger.info("new user {} has been created",registeredUser.getId());
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccess.builder().message(SuccessEnum.USER_REGISTRATION_SUCCESSFUL.getMessage()).build());
    }

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
