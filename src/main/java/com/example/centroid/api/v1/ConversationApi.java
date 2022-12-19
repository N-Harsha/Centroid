package com.example.centroid.api.v1;

import com.example.centroid.model.Dto.ConversationDTO;
import com.example.centroid.service.GroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/conversation")
public class ConversationApi {

    @Autowired
    GroupMemberService groupMemberService;

    @GetMapping("/all")
    public List<ConversationDTO> getAllConversations(@RequestHeader("session") String sessionId){
        return groupMemberService.getAllConversations(sessionId);
    }
}
