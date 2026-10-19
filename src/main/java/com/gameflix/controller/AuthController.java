package com.gameflix.controller;

import com.gameflix.dto.RegisterRequest;
import com.gameflix.model.User;
import com.gameflix.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.gameflix.dto.LoginRequest;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @Valid @RequestBody RegisterRequest request) {

        User user = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                        "id", user.getId(),
                        "email", user.getEmail()
                )
        );
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @Valid @RequestBody LoginRequest request) {

        String token = authService.login(request);

        return ResponseEntity.ok(
                Map.of(
                        "token", token,
                        "tokenType", "Bearer"
                )
        );
    }

}
