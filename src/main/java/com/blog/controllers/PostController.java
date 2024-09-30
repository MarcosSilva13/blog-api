package com.blog.controllers;

import com.blog.dtos.ErrorDTO;
import com.blog.dtos.PageResponseDTO;
import com.blog.dtos.postdto.PostRequestDTO;
import com.blog.dtos.postdto.PostResponseDTO;
import com.blog.queryfilters.PostQueryFilter;
import com.blog.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Publicação")
@SecurityRequirement(name = "bearer-key")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "Retorna todas as publicações", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponseDTO.class))),

            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content())
    })
    @GetMapping
    public ResponseEntity<PageResponseDTO> getAll(@RequestParam("page") int page, PostQueryFilter postQueryFilter) {
        PageResponseDTO pageResponseDTO = postService.getAll(page, postQueryFilter);
        return ResponseEntity.ok(pageResponseDTO);
    }

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
    @PostMapping
    public ResponseEntity<PostResponseDTO> create(@RequestBody @Valid PostRequestDTO requestDTO) {
        PostResponseDTO postResponseDTO = postService.createPost(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponseDTO);
    }

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
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> update(@PathVariable("postId") Long postId, @RequestBody @Valid PostRequestDTO requestDTO) {
        PostResponseDTO postResponseDTO = postService.updatePost(postId, requestDTO);
        return ResponseEntity.ok(postResponseDTO);
    }

    @Operation(summary = "Deleta uma publicação", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content",content = @Content()),

            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))),

            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content()),

            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    })
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@PathVariable("postId") Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}