package com.example.centroid.api.v1;

import com.example.centroid.model.Dto.UserDTO;
import com.example.centroid.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserApi {
    @Autowired
    UserService userService;
    @GetMapping("/search")
    public Page<UserDTO> findUsersByQuery(
            @RequestHeader("session") String sessionId,
            @RequestParam(name = "query",required = false) String query, Pageable pageable){
        return userService.findUsersByQuery(sessionId,query,pageable);
    }
}
