package com.blog.dtos.authdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern.Flag;

public record RegisterRequestDTO(
        @NotBlank(message = "Campo 'nome' não deve ficar em branco.")
        String name,

        @NotBlank(message = "Campo 'email' não deve ficar em branco.")
        @Email(flags = Flag.CASE_INSENSITIVE, regexp = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Email inválido.")
        String email,

        @NotBlank(message = "Campo 'senha' não deve ficar em branco.")
        String password) {
}
