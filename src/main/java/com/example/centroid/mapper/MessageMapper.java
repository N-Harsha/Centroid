package com.example.centroid.mapper;

import com.example.centroid.model.Dto.ConversationDTO;
import com.example.centroid.model.Dto.MessageDTO;
import com.example.centroid.model.GroupMember;
import com.example.centroid.model.Message;
import com.example.centroid.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(source = "fromUser",target = "fromUser",qualifiedByName = "userNameFromUser")
    MessageDTO messageToMessageDTO(Message message);
    @Mapping(source = "fromUser",target = "fromUser",qualifiedByName = "userFromUserName")
    Message messageDTOToMessage(MessageDTO messageDTO);

    @Named("userNameFromUser")
    default String userNameFromUser(User user){
        return user.getUsername();
    }
    @Named("userFromUserName")
    default User userFromUserName(String username){
        return null;
    }
    default List<MessageDTO> messagesToMessageDTOs(List<Message> messages){
        return !messages.isEmpty()
                ?messages.stream().map(this::messageToMessageDTO).collect(Collectors.toList())
                : Collections.emptyList();
    }
}
