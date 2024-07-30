package com.blog.controllers;

import com.blog.dtos.authdto.LoginRequestDTO;
import com.blog.dtos.authdto.RegisterRequestDTO;
import com.blog.dtos.authdto.TokenDTO;
import com.blog.dtos.userdto.UserResponseDTO;
import com.blog.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid RegisterRequestDTO requestDTO) {
        UserResponseDTO userResponseDTO = authService.register(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginRequestDTO requestDTO) {
        TokenDTO tokenDTO = authService.login(requestDTO);
        return ResponseEntity.ok(tokenDTO);
    }
}
