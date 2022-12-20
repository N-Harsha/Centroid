package com.example.centroid.api.v1;

import com.example.centroid.mapper.MessageMapper;
import com.example.centroid.model.Dto.ApiSuccess;
import com.example.centroid.model.Dto.MessageDTO;
import com.example.centroid.model.Message;
import com.example.centroid.service.MessageService;
import com.example.centroid.utils.SuccessEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/message")
public class MessageApi {

    @Autowired
    MessageService messageService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    MessageMapper messageMapper;

    @PostMapping("/send/{id}")
    public ResponseEntity<Object> sendMessage(@RequestHeader("session") String sessionId,
                                              @PathVariable(name = "id",required = true) Long id,
                                              @RequestBody MessageDTO messageDTO){
        Message message = messageService.sendMessage(sessionId,id,messageDTO);

        simpMessagingTemplate.convertAndSend("/conversation/"+id,messageMapper.messageToMessageDTO(message));

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiSuccess.builder().
                message(SuccessEnum.Message_SENT_SUCCUSFULLY.getMessage()).build());
    }

    @GetMapping("/fetch/{id}")
    public List<MessageDTO> fetchMessages(@RequestHeader("session") String sessionId,@PathVariable("id") Long id){
        return messageService.findMessages(sessionId,id);
    }

}
