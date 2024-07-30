package com.blog.dtos.userdto;

import jakarta.validation.constraints.NotBlank;

public record PasswordDTO(
        @NotBlank(message = "Campo 'senha antiga' não deve ficar em branco.")
        String oldPassword,

        @NotBlank(message = "Campo 'nova senha' não deve ficar em branco.")
        String newPassword) {
}
