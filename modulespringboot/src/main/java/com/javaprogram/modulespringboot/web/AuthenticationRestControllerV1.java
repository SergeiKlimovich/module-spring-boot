package com.javaprogram.modulespringboot.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaprogram.modulespringboot.model.User;
import com.javaprogram.modulespringboot.security.jwt.JwtTokenProvider;
import com.javaprogram.modulespringboot.service.UserService;
import com.javaprogram.modulespringboot.service.dto.AuthenticationRequestDto;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/auth/")
@RequiredArgsConstructor
public class AuthenticationRestControllerV1 {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @PostMapping("login")
    @Timed(value = "user.login.time", description = "Time to login a user", percentiles = {0.5, 0.9})
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto) {
        try {
            String username = requestDto.getUsername();
            String password = requestDto.getPassword();
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            Optional<User> user = userService.findByUsername(username);
            if (!user.isPresent()) {
                throw new UsernameNotFoundException("User with username: " + username + " not found.");
            }
            String token = jwtTokenProvider.createToken(username, user.get().getRoles());
            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password.");
        }
    }
}
