package com.example.centroid.api.v1;

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
public class MessageApi {

    @Autowired
    MessageService messageService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/send")
    public ResponseEntity<Object> sendMessage(@Header("session") String sessionId,
                                              @Header("conversationId") Long id,
                                              @Payload MessageDTO messageDTO){
        Message message = messageService.sendMessage(sessionId,id,messageDTO);

        simpMessagingTemplate.convertAndSendToUser(message.getConversation().getId().toString(),
                                            "/private",message);
        //conversation/conv-id/private -- listen on.
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiSuccess.builder().
                message(SuccessEnum.Message_SENT_SUCCUSFULLY.getMessage()).build());
    }

    public List<MessageDTO> fetchMessages(@Header("session") String sessionId,@Header("conversationId") Long id){
        messageService.findMessages(sessionId,id);
    }

}
