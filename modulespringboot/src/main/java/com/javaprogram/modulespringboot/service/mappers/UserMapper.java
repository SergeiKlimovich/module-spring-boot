package com.javaprogram.modulespringboot.service.mappers;

import org.mapstruct.Mapper;

import com.javaprogram.modulespringboot.model.User;
import com.javaprogram.modulespringboot.service.dto.UserDto;

@Mapper
public interface UserMapper {

    UserDto userToUserDto(User user);

    User userDtoToUser(UserDto userDto);
}
