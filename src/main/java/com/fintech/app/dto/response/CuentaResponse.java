package com.fintech.app.dto.response;

import com.fintech.app.model.enums.Moneda;
import com.fintech.app.model.enums.TipoCuenta;

public record CuentaResponse(
        int cuentaId,
        int usuarioId,
        TipoCuenta tipoCuenta,
        double saldo,
        Moneda moneda
) {
}
