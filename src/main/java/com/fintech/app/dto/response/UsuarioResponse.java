package com.fintech.app.dto.response;

public record UsuarioResponse(
        int usuarioId,
        String nombre,
        String apellido,
        String email,
        String dni
) {
}

