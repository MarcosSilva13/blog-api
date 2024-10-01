package com.blog.api.documentation;

import com.blog.api.dtos.ErrorDTO;
import com.blog.api.dtos.authdto.LoginRequestDTO;
import com.blog.api.dtos.authdto.RegisterRequestDTO;
import com.blog.api.dtos.authdto.TokenDTO;
import com.blog.api.dtos.userdto.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Autenticação")
public interface AuthControllerDoc {

    @Operation(summary = "Registra um novo usuário", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),

            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "409", description = "Conflict",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    ResponseEntity<UserResponseDTO> register(RegisterRequestDTO requestDTO);

    @Operation(summary = "Realiza login", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenDTO.class))),

            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    ResponseEntity<TokenDTO> login(LoginRequestDTO requestDTO);
}
