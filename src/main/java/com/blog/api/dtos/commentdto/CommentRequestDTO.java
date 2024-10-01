package com.blog.api.dtos.commentdto;

import jakarta.validation.constraints.NotBlank;

public record CommentRequestDTO(
        @NotBlank(message = "Campo 'conteúdo' não deve ficar em branco.")
        String content) {
}
