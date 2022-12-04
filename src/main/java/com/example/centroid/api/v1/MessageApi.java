package com.example.centroid.api.v1;

import com.example.centroid.model.Dto.MessageDTO;
import com.example.centroid.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/message")
public class MessageApi {

    @Autowired
    MessageService messageService;

    @PostMapping("/send/{id}")
    public ResponseEntity<Object> sendMessage(@RequestHeader("session") String sessionId,
                                              @RequestBody MessageDTO messageDTO,
                                              @PathVariable(name = "id",required = true) Long id){
    return messageService.sendMessage(sessionId,id,messageDTO);
    }

}
