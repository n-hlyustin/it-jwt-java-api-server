package com.itransition.simpleapiserver.mappers;

import com.itransition.simpleapiserver.dto.UserDto;
import com.itransition.simpleapiserver.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mappings({
        @Mapping(source = "firstname", target = "firstName"),
        @Mapping(source = "lastname", target = "lastName")
    })
    User userDtoToUser(UserDto userDto);

    @Mappings({
        @Mapping(source = "firstName", target = "firstname"),
        @Mapping(source = "lastName", target = "lastname")
    })
    UserDto userToUserDto(User user);
}
