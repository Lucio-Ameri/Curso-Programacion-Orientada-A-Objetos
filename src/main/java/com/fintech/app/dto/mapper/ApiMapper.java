package com.fintech.app.dto.mapper;

import com.fintech.app.dto.response.CuentaResponse;
import com.fintech.app.dto.response.MovimientoResponse;
import com.fintech.app.dto.response.UsuarioResponse;
import com.fintech.app.model.Cuenta;
import com.fintech.app.model.Movimiento;
import com.fintech.app.model.Usuario;

public final class ApiMapper {

    private ApiMapper() {
    }

    public static UsuarioResponse toUsuarioResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getUsuarioID(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getDNI()
        );
    }

    public static CuentaResponse toCuentaResponse(Cuenta cuenta) {
        return new CuentaResponse(
                cuenta.getID(),
                cuenta.getUsuarioID(),
                cuenta.getTipoCuenta(),
                cuenta.getMonto().getMonto(),
                cuenta.getMonto().getMoneda()
        );
    }

    public static MovimientoResponse toMovimientoResponse(Movimiento movimiento) {
        return new MovimientoResponse(
                movimiento.getMovimientoID(),
                movimiento.getTipoMovimiento(),
                movimiento.getMonto().getMonto(),
                movimiento.getMonto().getMoneda(),
                movimiento.getFecha(),
                movimiento.getCuentaID()
        );
    }
}
