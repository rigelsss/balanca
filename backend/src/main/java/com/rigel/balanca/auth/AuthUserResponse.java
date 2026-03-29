package com.rigel.balanca.auth;

import java.util.UUID;

public record AuthUserResponse(UUID id, String username) {
}
