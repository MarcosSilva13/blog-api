package com.blog.controllers;

import com.blog.dtos.userdto.PasswordDTO;
import com.blog.dtos.userdto.UpdateUserRequestDTO;
import com.blog.dtos.userdto.UserResponseDTO;
import com.blog.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserResponseDTO> getLoggedUser() {
        UserResponseDTO userResponseDTO = userService.getLoggedUser();
        return ResponseEntity.ok(userResponseDTO);
    }

    @PutMapping
    public ResponseEntity<UserResponseDTO> update(@RequestBody @Valid UpdateUserRequestDTO requestDTO) {
        UserResponseDTO userResponseDTO = userService.updateUser(requestDTO);
        return ResponseEntity.ok(userResponseDTO);
    }

    @PatchMapping
    public ResponseEntity<Void> updatePassword(@RequestBody @Valid PasswordDTO passwordDTO) {
        userService.updateUserPassword(passwordDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete() {
        userService.deleteUser();
        return ResponseEntity.noContent().build();
    }
}
