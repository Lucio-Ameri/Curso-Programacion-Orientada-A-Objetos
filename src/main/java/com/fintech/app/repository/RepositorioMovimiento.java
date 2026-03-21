package com.fintech.app.repository;

import com.fintech.app.model.Movimiento;

import java.util.List;
import java.util.Optional;

public interface RepositorioMovimiento {

    Movimiento guardar(Movimiento movimiento);

    Optional<Movimiento> buscarPorID(int movimientoID);

    List<Movimiento> listarTodos();

    List<Movimiento> listarPorCuentaID(int cuentaID);
}
