package com.fintech.app.dto.request;

import com.fintech.app.model.enums.TipoCuenta;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CrearCuentaRequest(
        @Positive(message = "El usuarioId debe ser mayor que 0")
        int usuarioId,

        @NotNull(message = "El tipo de cuenta es obligatorio")
        TipoCuenta tipoCuenta
) {
}
