package com.javaprogram.modulespringboot.web;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.javaprogram.modulespringboot.service.UserService;
import com.javaprogram.modulespringboot.service.dto.UserDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/users/")
@RequiredArgsConstructor
public class UserRestControllerV1 {

    private final UserService userService;

    @GetMapping(value = "{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") Long id) {
        Optional<UserDto> userDto = userService.findById(id);
        return userDto.map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("registration")
    public ResponseEntity register(@RequestBody UserDto userDto) {
        UserDto registeredUserDto = userService.register(userDto);
        return new ResponseEntity<>(registeredUserDto, HttpStatus.CREATED);
    }
}
