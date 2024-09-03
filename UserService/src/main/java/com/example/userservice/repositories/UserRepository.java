package com.example.userservice.repositories;

import com.example.userservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);


    User findByEmailAndPassword(String email, String password);

    User findByUsername(String username);
}
