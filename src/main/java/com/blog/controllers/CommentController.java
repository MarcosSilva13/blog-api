package com.blog.controllers;

import com.blog.dtos.commentdto.CommentRequestDTO;
import com.blog.dtos.commentdto.CommentResponseDTO;
import com.blog.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponseDTO>> getAll(@PathVariable("postId") Long postId) {
        List<CommentResponseDTO> commentResponseDTOList = commentService.getAll(postId);
        return ResponseEntity.ok(commentResponseDTOList);
    }

    @PostMapping("/post/{postId}")
    public ResponseEntity<CommentResponseDTO> create(@PathVariable("postId") Long postId, @RequestBody CommentRequestDTO requestDTO) {
        CommentResponseDTO commentResponseDTO = commentService.createComment(postId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponseDTO);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDTO> update(@PathVariable("commentId") Long commentId, @RequestBody CommentRequestDTO requestDTO) {
        CommentResponseDTO commentResponseDTO = commentService.updateComment(commentId, requestDTO);
        return ResponseEntity.ok(commentResponseDTO);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
