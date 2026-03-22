package com.fintech.app.dto.response;

import com.fintech.app.model.enums.Moneda;
import com.fintech.app.model.enums.TipoMovimiento;

import java.time.LocalDateTime;

public record MovimientoResponse(
        int movimientoId,
        TipoMovimiento tipoMovimiento,
        double monto,
        Moneda moneda,
        LocalDateTime fecha,
        int cuentaId
) {
}
