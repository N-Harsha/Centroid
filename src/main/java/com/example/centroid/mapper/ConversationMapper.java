package com.example.centroid.mapper;

import com.example.centroid.model.Conversation;
import com.example.centroid.model.Dto.ConversationDTO;
import com.example.centroid.model.Dto.UserRequestDTO;
import com.example.centroid.model.GroupMember;
import com.example.centroid.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ConversationMapper {
    ConversationMapper INSTANCE = Mappers.getMapper(ConversationMapper.class);

    @Mapping(source = "conversation",target = "conversationId",qualifiedByName = "ConversationToConversationId")
    ConversationDTO groupMemberToConversationDTO(GroupMember groupMember);

    default List<ConversationDTO> groupMembersToConversationDTOs(List<GroupMember> groupMembers){
        return !groupMembers.isEmpty()
                ?groupMembers.stream().map(this::groupMemberToConversationDTO).collect(Collectors.toList())
                : Collections.emptyList();
    }

    @Named("ConversationToConversationId")
    default Long ConversationToConversationId(Conversation conversation){
     return conversation.getId();
    }


}
