package com.example.centroid.mapper;

import com.example.centroid.model.Dto.UserDTO;
import com.example.centroid.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring"
)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDTO userToUserDTO(User user);
    User UserDTOToUser(UserDTO user);
    default List<UserDTO> usersToUserDTOs(List<User> users) {
        return !users.isEmpty()
                ? users.stream().map(m -> userToUserDTO(m)).collect(Collectors.toList())
                : Collections.emptyList();
    }

}
