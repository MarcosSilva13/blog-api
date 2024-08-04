package com.blog.controllers;

import com.blog.dtos.ErrorDTO;
import com.blog.dtos.userdto.PasswordDTO;
import com.blog.dtos.userdto.UpdateUserRequestDTO;
import com.blog.dtos.userdto.UserResponseDTO;
import com.blog.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuário")
@SecurityRequirement(name = "bearer-key")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Retorna o usuário logado", method = "GET", description = "Retorna os dados do usuário logado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),

            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "aplication/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
    })
    @GetMapping
    public ResponseEntity<UserResponseDTO> getLoggedUser() {
        UserResponseDTO userResponseDTO = userService.getLoggedUser();
        return ResponseEntity.ok(userResponseDTO);
    }

    @Operation(summary = "Atualiza usuário", method = "PUT", description = "Atualiza os dados do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),

            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "aplication/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),

            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PutMapping
    public ResponseEntity<UserResponseDTO> update(@RequestBody @Valid UpdateUserRequestDTO requestDTO) {
        UserResponseDTO userResponseDTO = userService.updateUser(requestDTO);
        return ResponseEntity.ok(userResponseDTO);
    }

    @Operation(summary = "Atualiza senha", method = "PATCH", description = "Atualiza a senha do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content()),

            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "aplication/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
    })
    @PatchMapping
    public ResponseEntity<Void> updatePassword(@RequestBody @Valid PasswordDTO passwordDTO) {
        userService.updateUserPassword(passwordDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Deleta usuário", method = "DELETE", description = "Deleta a conta do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content", content = @Content()),

            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
    })
    @DeleteMapping
    public ResponseEntity<Void> delete() {
        userService.deleteUser();
        return ResponseEntity.noContent().build();
    }
}
