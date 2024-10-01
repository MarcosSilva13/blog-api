package com.blog.api.documentation;

import com.blog.api.dtos.ErrorDTO;
import com.blog.api.dtos.PageResponseDTO;
import com.blog.api.dtos.postdto.PostRequestDTO;
import com.blog.api.dtos.postdto.PostResponseDTO;
import com.blog.api.filters.PostQueryFilter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Publicação")
@SecurityRequirement(name = "bearer-key")
public interface PostControllerDoc {

    @Operation(summary = "Retorna todas as publicações", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponseDTO.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
    })
    ResponseEntity<PageResponseDTO> getAll(int page, PostQueryFilter postQueryFilter);

    @Operation(summary = "Registra uma nova publicação", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponseDTO.class))),

            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "aplication/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
    })
    ResponseEntity<PostResponseDTO> create(PostRequestDTO requestDTO);

    @Operation(summary = "Atualiza uma publicação", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponseDTO.class))),

            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "aplication/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),

            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    ResponseEntity<PostResponseDTO> update(Long postId, PostRequestDTO requestDTO);

    @Operation(summary = "Deleta uma publicação", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content",content = @Content()),

            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),

            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    ResponseEntity<Void> delete(Long postId);
}