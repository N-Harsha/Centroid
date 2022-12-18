package com.example.centroid.api.v1;

import com.example.centroid.model.Dto.UserRequestDTO;
import com.example.centroid.service.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-request")
@CrossOrigin(origins = "*")
public class UserRequestApi {
    @Autowired
    UserRequestService userRequestService;
    @PostMapping("/send/{id}")
    public ResponseEntity<Object> sendUserRequest(@RequestHeader("session") String sessionId,
                                                  @PathVariable(name = "id",required = true) Long id){
        return userRequestService.sendUserRequest(sessionId,id);
    }

    @PutMapping("/accept/{id}")
    public ResponseEntity<Object> acceptUserRequest(@RequestHeader("session") String sessionId,
                                                    @PathVariable(name = "id",required = true) Long id){
        return userRequestService.acceptUserRequest(sessionId,id);
    }
    @PutMapping("/cancel/{id}")
    public ResponseEntity<Object> cancelUserRequest(@RequestHeader("session") String sessionId,
                                                    @PathVariable(name = "id",required = true) Long id){
        return userRequestService.cancelUserRequest(sessionId,id);
    }
    @PutMapping("/reject/{id}")
    public ResponseEntity<Object> rejectUserRequest(@RequestHeader("session") String sessionId,
                                                    @PathVariable(name = "id",required = true) Long id){
        return userRequestService.rejectUserRequest(sessionId,id);
    }
    @GetMapping("/sent")
    public List<UserRequestDTO> sentRequests(@RequestHeader("session") String sessionId){
        return userRequestService.sentUserRequests(sessionId);
    }
    @GetMapping("/received")
    public List<UserRequestDTO> receivedRequests(@RequestHeader("session") String sessionId){
        return userRequestService.receivedUserRequests(sessionId);
    }
}
