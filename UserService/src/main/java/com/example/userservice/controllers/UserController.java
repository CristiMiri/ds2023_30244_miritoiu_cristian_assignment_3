package com.example.userservice.controllers;

import com.example.userservice.dtos.UserRequestDTO;
import com.example.userservice.dtos.UserResponseDTO;
import com.example.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponseDTO> getUsers() {
        List<UserResponseDTO> users= userService.findUsers();
        System.out.println(users);
        return users;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO dto = userService.findUserById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserRequestDTO request) {
        UserResponseDTO dto = userService.findUserByEmailAndPassword(request.getEmail(), request.getPassword());
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRequestDTO request) {
        UserResponseDTO response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody UserRequestDTO request) {
        UserResponseDTO response = userService.update(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
