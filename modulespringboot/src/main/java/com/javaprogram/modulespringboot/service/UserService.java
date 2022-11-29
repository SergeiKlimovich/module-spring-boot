package com.javaprogram.modulespringboot.service;

import java.util.List;
import java.util.Optional;

import com.javaprogram.modulespringboot.model.User;
import com.javaprogram.modulespringboot.service.dto.UserDto;

public interface UserService {
    UserDto register(UserDto userDto);

    List<UserDto> getAll();

    Optional<User> findByUsername(String username);

    Optional<UserDto> findById(Long id);

    UserDto update(Long id, UserDto userDto);

    void deleteUserById(Long id);
}
