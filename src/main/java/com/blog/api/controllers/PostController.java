package com.blog.api.controllers;

import com.blog.api.documentation.PostControllerDoc;
import com.blog.api.dtos.PageResponseDTO;
import com.blog.api.dtos.postdto.PostRequestDTO;
import com.blog.api.dtos.postdto.PostResponseDTO;
import com.blog.api.filters.PostQueryFilter;
import com.blog.domain.services.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController implements PostControllerDoc {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO> getAll(@RequestParam("page") int page, PostQueryFilter postQueryFilter) {
        PageResponseDTO pageResponseDTO = postService.getAll(page, postQueryFilter);
        return ResponseEntity.ok(pageResponseDTO);
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> create(@RequestBody @Valid PostRequestDTO requestDTO) {
        PostResponseDTO postResponseDTO = postService.createPost(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponseDTO);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> update(@PathVariable("postId") Long postId, @RequestBody @Valid PostRequestDTO requestDTO) {
        PostResponseDTO postResponseDTO = postService.updatePost(postId, requestDTO);
        return ResponseEntity.ok(postResponseDTO);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@PathVariable("postId") Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}