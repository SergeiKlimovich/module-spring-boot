package com.javaprogram.modulespringboot.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.javaprogram.modulespringboot.model.Role;
import com.javaprogram.modulespringboot.model.User;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindAllUsers() {
        createUsers();
        List<User> users = userRepository.findAll();
        assertThat(users, hasSize(2));
    }

    @Test
    void shouldCreateUser() {
        User newUser = new User("testUser", "testFirstName", "testLastName", "testUser@gmail.com",
                "$2a$10$JCRhMmrSsmHwF6n0kT248uAG2WQTzupS0bq4.8ldjsOa/9ECRHokq", Arrays.asList(new Role()));

        User createdUser = userRepository.save(newUser);
        assertThat(newUser, is(createdUser));
    }

    @Test
    void shouldDeleteUserById() {
        createUsers();
        userRepository.deleteById(1L);
        Optional<User> deletedUser = userRepository.findById(1L);
        assertThat(deletedUser, is(Optional.empty()));
    }

    @Test
    void shouldFindUserById() {
        createUsers();
        User user = userRepository.findById(2L).get();
        assertThat(user.getFirstName(), is("testFirstName2"));
    }

    private void createUsers() {
        userRepository.saveAll(Arrays.asList(
                new User("testUser1", "testFirstName1", "testLastName1", "testUser1@gmail.com",
                        "$2a$10$fON/TeP/4qEewcRr/7vWauJ5nsy8J6X6nc7jsfbFYS7mkDupNsKRu",
                        Arrays.asList(new Role("ROLE_USER"))),
                new User("testUser2", "testFirstName2", "testLastName2", "testUser2@gmail.com",
                        "$2a$10$ImxRgpioXQWB3w2QPKhBG.fY9Mr3a/o3xnPbjXFcu8wg2J0/GglPu",
                        Arrays.asList(new Role("ROLE_USER")))));
    }

}
