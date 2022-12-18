package com.example.centroid.mapper;

import com.example.centroid.model.Dto.UserDTO;
import com.example.centroid.model.Dto.UserRequestDTO;
import com.example.centroid.model.User;
import com.example.centroid.model.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring"
)
public interface UserRequestMapper {
    UserRequestMapper INSTANCE = Mappers.getMapper(UserRequestMapper.class);
    @Mapping(source = "sender",target = "sender",qualifiedByName = "senderUsernameFromUserRequest")
    @Mapping(source = "receiver",target = "receiver",qualifiedByName = "receiverUsernameFromUserRequest")
    UserRequestDTO userRequestToUserRequestDTO(UserRequest userRequest);
    default List<UserRequestDTO> usersToUserDTOs(List<UserRequest> userRequests) {
        return !userRequests.isEmpty()
                ? userRequests.stream().map(m -> userRequestToUserRequestDTO(m)).collect(Collectors.toList())
                : Collections.emptyList();
    }
    @Named("senderUsernameFromUserRequest")
    default String senderUsernameFromUserRequest(User user){
        return user.getUsername();
    }
    @Named("receiverUsernameFromUserRequest")
    default String receiverUsernameFromUserRequest(User user){
        return user.getUsername();
    }
}
