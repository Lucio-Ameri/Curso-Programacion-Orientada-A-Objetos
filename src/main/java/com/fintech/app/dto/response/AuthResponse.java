package com.fintech.app.dto.response;

public record AuthResponse(
        String mensaje,
        UsuarioResponse usuario
) {
}
