package com.fintech.app.dto.request;

import com.fintech.app.model.enums.Moneda;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OperacionDineroRequest(
        @Positive(message = "El monto debe ser mayor que 0")
        double monto,

        @NotNull(message = "La moneda es obligatoria")
        Moneda moneda
) {
}
