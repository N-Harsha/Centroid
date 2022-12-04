package com.example.centroid.bootstrap;

import com.example.centroid.constants.Constants;
import com.example.centroid.model.Dto.SignUpFormDTO;
import com.example.centroid.model.UserRequestStatus;
import com.example.centroid.repository.UserRequestStatusRepository;
import com.example.centroid.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Bootstrap implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(Bootstrap.class);
    @Autowired
    private UserRequestStatusRepository userRequestStatusRepository;

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        logger.info("BootStrap class has been initiated...");
        List<UserRequestStatus> userRequestStatusList = new ArrayList<>();
        userRequestStatusList.add(UserRequestStatus.builder().status(Constants.USER_REQUEST_PENDING).build());
        userRequestStatusList.add(UserRequestStatus.builder().status(Constants.USER_REQUEST_ACCEPTED).build());
        userRequestStatusList.add(UserRequestStatus.builder().status(Constants.USER_REQUEST_REJECTED).build());
        userRequestStatusRepository.saveAll(userRequestStatusList);

        userService.userRegistration(SignUpFormDTO.builder().firstName("Harsha").lastName("Nimmala")
                .email("harsha0770@gmail.com").verifyEmail("harsha0770@gmail.com").password("Password@1").verifyPassword("Password@1").username("harsha").build());
        userService.userRegistration(SignUpFormDTO.builder().firstName("Kushal").lastName("Nimmala")
                .email("kushal0770@gmail.com").verifyEmail("kushal0770@gmail.com").password("Password@1").verifyPassword("Password@1").username("kushal").build());
    }
}
