package com.blog.api.documentation;

import com.blog.api.dtos.ErrorDTO;
import com.blog.api.dtos.userdto.PasswordDTO;
import com.blog.api.dtos.userdto.UpdateUserRequestDTO;
import com.blog.api.dtos.userdto.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Usuário")
@SecurityRequirement(name = "bearer-key")
public interface UserControllerDoc {

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
    ResponseEntity<UserResponseDTO> getLoggedUser();

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
    ResponseEntity<UserResponseDTO> update(UpdateUserRequestDTO requestDTO);

    @Operation(summary = "Atualiza senha", method = "PATCH", description = "Atualiza a senha do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content()),

            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "aplication/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
    })
    ResponseEntity<Void> updatePassword(PasswordDTO passwordDTO);

    @Operation(summary = "Deleta usuário", method = "DELETE", description = "Deleta a conta do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content", content = @Content()),

            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
    })
    ResponseEntity<Void> delete();
}
