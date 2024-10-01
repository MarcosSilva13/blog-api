package com.blog.api.dtos.postdto;

import jakarta.validation.constraints.NotBlank;

public record PostRequestDTO(
        @NotBlank(message = "Campo 'título' não deve ficar em branco.")
        String title,

        @NotBlank(message = "Campo 'contéudo' não deve ficar em branco.")
        String content) {
}
