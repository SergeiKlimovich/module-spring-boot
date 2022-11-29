package com.javaprogram.modulespringboot.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaprogram.modulespringboot.service.UserService;
import com.javaprogram.modulespringboot.service.dto.UserDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/admin/users/")
@RequiredArgsConstructor
public class AdminRestControllerV1 {

    private final UserService userService;

    @GetMapping()
    public ResponseEntity<List<UserDto>> findAllUsers() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<UserDto> updateUserById(@PathVariable(name = "id") Long id,
                                                  @RequestBody UserDto userDto) {
        UserDto updatedUserDto = userService.update(id, userDto);
        return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
