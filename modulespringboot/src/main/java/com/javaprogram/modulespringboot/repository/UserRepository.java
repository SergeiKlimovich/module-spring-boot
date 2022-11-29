package com.javaprogram.modulespringboot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javaprogram.modulespringboot.model.Status;
import com.javaprogram.modulespringboot.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Integer countUserByStatus(Status status);
}
