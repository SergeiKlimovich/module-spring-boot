package com.javaprogram.modulespringboot.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.javaprogram.modulespringboot.model.Role;
import com.javaprogram.modulespringboot.model.Status;
import com.javaprogram.modulespringboot.model.User;
import com.javaprogram.modulespringboot.repository.RoleRepository;
import com.javaprogram.modulespringboot.repository.UserRepository;
import com.javaprogram.modulespringboot.service.UserService;
import com.javaprogram.modulespringboot.service.dto.UserDto;
import com.javaprogram.modulespringboot.service.mappers.UserMapper;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Autowired
    public UserServiceImpl(@Lazy UserRepository userRepository,
                           RoleRepository roleRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto register(UserDto userDto) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> updatedUserRoles = new ArrayList<>();
        updatedUserRoles.add(roleUser);
        User newUser = userMapper.userDtoToUser(userDto);
        newUser.setCreated(new Date());
        newUser.setUpdated(new Date());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setRoles(updatedUserRoles);
        newUser.setStatus(Status.ACTIVE);
        return userMapper.userToUserDto(userRepository.save(newUser));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        return userRepository.findById(id).map(userMapper::userToUserDto);
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        User updatedUser = userRepository.findById(id).get();
        updatedUser.setFirstName(userDto.getFirstName());
        updatedUser.setLastName(userDto.getLastName());
        updatedUser.setEmail(userDto.getEmail());
        updatedUser.setUpdated(new Date());

        return userMapper.userToUserDto(userRepository.save(updatedUser));
    }

    @Override
    public void deleteUserById(Long id) {
        if (findById(id).isPresent()) {
            userRepository.deleteById(id);
        }
    }
}