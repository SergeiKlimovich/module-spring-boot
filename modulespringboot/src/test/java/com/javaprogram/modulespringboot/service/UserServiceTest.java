package com.javaprogram.modulespringboot.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.javaprogram.modulespringboot.model.Role;
import com.javaprogram.modulespringboot.model.User;
import com.javaprogram.modulespringboot.repository.RoleRepository;
import com.javaprogram.modulespringboot.repository.UserRepository;
import com.javaprogram.modulespringboot.service.dto.UserDto;
import com.javaprogram.modulespringboot.service.impl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserServiceImpl sut;

    @Test
    void shouldFindAllUsers() {
        User user = new User();
        UserDto userDto = new UserDto();
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        List<UserDto> userDtos = sut.getAll();
        assertThat(userDtos, is(Collections.singletonList(userDto)));
    }

    @Test
    void shouldFindUserByUsername() {
        Optional<User> expected = Optional.of(new User());
        String username = "username";
        when(userRepository.findByUsername(username)).thenReturn(expected);
        Optional<User> user = sut.findByUsername(username);
        assertThat(user, is(expected));
    }

    @Test
    void shouldFindUserById() {
        Optional<User> user = Optional.of(new User());
        Optional<UserDto> userDto = Optional.of(new UserDto());
        when(userRepository.findById(1L)).thenReturn(user);
        Optional<UserDto> userDtoById = sut.findById(1L);
        assertThat(userDtoById, is(userDto));
    }

    @Test
    void shouldDeleteUserById() {
        Long id = 1L;
        Optional<User> user = Optional.of(new User());
        when(userRepository.findById(1L)).thenReturn(user);
        doNothing().when(userRepository).deleteById(id);
        sut.deleteUserById(id);
        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void shouldCreateUser() {
        UserDto userDto = new UserDto();
        userDto.setPassword("test");
        User user = new User();
        user.setPassword("$2a$12$OMM0oDXMy645Ek3Y0MLM4eCCWDfWXuMllWOa/fkzO9vrNC5sHHicG");
        UserDto expected = new UserDto();
        expected.setPassword("$2a$12$OMM0oDXMy645Ek3Y0MLM4eCCWDfWXuMllWOa/fkzO9vrNC5sHHicG");
        when(bCryptPasswordEncoder.encode("test"))
                .thenReturn("$2a$12$OMM0oDXMy645Ek3Y0MLM4eCCWDfWXuMllWOa/fkzO9vrNC5sHHicG");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(new Role("USER"));
        when(userRepository.save(any(User.class))).thenReturn(user);
        UserDto actual = sut.register(userDto);
        assertThat(actual, is(expected));
    }

    @Test
    void shouldUpdateUserWhenIdFound() {
        UserDto userDto = new UserDto();
        Long id = 1L;

        User user = new User();
        Optional<UserDto> expected = Optional.of(new UserDto());

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        Optional<UserDto> actual = Optional.of(sut.update(id, userDto));

        assertThat(actual, is(expected));
    }
}
