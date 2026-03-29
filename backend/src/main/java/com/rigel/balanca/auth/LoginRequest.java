package com.rigel.balanca.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "username e obrigatorio")
        String username,
        @NotBlank(message = "password e obrigatorio")
        String password
) {
}
