package com.blog.api.controllers;

import com.blog.api.documentation.CommentControllerDoc;
import com.blog.api.dtos.commentdto.CommentRequestDTO;
import com.blog.api.dtos.commentdto.CommentResponseDTO;
import com.blog.api.filters.CommentQueryFilter;
import com.blog.domain.services.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController implements CommentControllerDoc {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/post")
    public ResponseEntity<List<CommentResponseDTO>> getAll(CommentQueryFilter commentQueryFilter) {
        List<CommentResponseDTO> commentResponseDTOList = commentService.getAll(commentQueryFilter);
        return ResponseEntity.ok(commentResponseDTOList);
    }

    @PostMapping("/post/{postId}")
    public ResponseEntity<CommentResponseDTO> create(@PathVariable("postId") Long postId, @RequestBody @Valid CommentRequestDTO requestDTO) {
        CommentResponseDTO commentResponseDTO = commentService.createComment(postId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponseDTO);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDTO> update(@PathVariable("commentId") Long commentId, @RequestBody @Valid CommentRequestDTO requestDTO) {
        CommentResponseDTO commentResponseDTO = commentService.updateComment(commentId, requestDTO);
        return ResponseEntity.ok(commentResponseDTO);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
