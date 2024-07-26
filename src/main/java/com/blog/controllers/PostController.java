package com.blog.controllers;

import com.blog.dtos.PageResponseDTO;
import com.blog.dtos.postdto.PostRequestDTO;
import com.blog.dtos.postdto.PostResponseDTO;
import com.blog.services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO> getAll(@RequestParam("page") int page) {
        PageResponseDTO pageResponseDTO = postService.getAll(page);
        return ResponseEntity.ok(pageResponseDTO);
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> create(@RequestBody PostRequestDTO requestDTO) {
        PostResponseDTO postResponseDTO = postService.createPost(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponseDTO);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> update(@PathVariable("postId") Long postId, @RequestBody PostRequestDTO requestDTO) {
        PostResponseDTO postResponseDTO = postService.updatePost(postId, requestDTO);
        return ResponseEntity.ok(postResponseDTO);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@PathVariable("postId") Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}