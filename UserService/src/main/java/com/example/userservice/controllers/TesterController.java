package com.example.userservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tester")

public class TesterController {
    @GetMapping
    public ResponseEntity<String> sayHello() {
        System.out.println("Hello from secured endpoint");
        return ResponseEntity.ok("Hello from secured endpoint");
    }
}
