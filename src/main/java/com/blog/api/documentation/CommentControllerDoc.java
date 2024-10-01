package com.blog.api.documentation;

import com.blog.api.dtos.ErrorDTO;
import com.blog.api.dtos.commentdto.CommentRequestDTO;
import com.blog.api.dtos.commentdto.CommentResponseDTO;
import com.blog.api.filters.CommentQueryFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Comentário")
@SecurityRequirement(name = "bearer-key")
public interface CommentControllerDoc {

    @Operation(summary = "Retorna todos os comentários de uma publicação", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = CommentResponseDTO.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
    })
    ResponseEntity<List<CommentResponseDTO>> getAll(CommentQueryFilter commentQueryFilter);

    @Operation(summary = "Registra um novo comentário em uma publicação", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponseDTO.class))),

            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "aplication/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
    })
    ResponseEntity<CommentResponseDTO> create(Long postId, CommentRequestDTO requestDTO);

    @Operation(summary = "Atualiza um comentário", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponseDTO.class))),

            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "aplication/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),

            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    ResponseEntity<CommentResponseDTO> update(Long commentId, CommentRequestDTO requestDTO);

    @Operation(summary = "Deleta um comentário", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content", content = @Content()),

            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),

            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    ResponseEntity<Void> delete(Long commentId);
}