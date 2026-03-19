package com.fintech.app.repository;

import com.fintech.app.model.Cuenta;

import java.util.List;
import java.util.Optional;

public interface RepositorioCuenta {
    Cuenta guardar(Cuenta cuenta);

    Optional<Cuenta> buscarPorID(int cuentaID);

    List<Cuenta> listarCuentas();

    List<Cuenta> listarPorUsuarioID(int usuarioID);

    void eliminarPorID(int cuentaID);
}
