package com.fintech.app.service;

import com.fintech.app.model.Cuenta;
import com.fintech.app.model.Movimiento;
import com.fintech.app.repository.RepositorioCuenta;
import com.fintech.app.repository.RepositorioMovimiento;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class MovimientoService {
    private final RepositorioMovimiento repositorio;
    private final RepositorioCuenta cuentas;

    public MovimientoService(RepositorioMovimiento repositorio, RepositorioCuenta cuentas){
        if(repositorio == null || cuentas == null){
            throw  new IllegalArgumentException("NO SE PUEDE INICIALIZAR EL SERVICIO CON REPOSITORIOS NULL");
        }

        this.repositorio = repositorio;
        this.cuentas = cuentas;
    }

    public Movimiento registrarMovimiento(Movimiento movimiento){
        if(movimiento == null){
            throw new IllegalArgumentException("NO SE PUEDE REGISTRAR UN MOVIMIENTO NULL");
        }

        if(cuentas.buscarPorID(movimiento.getCuentaID()).isEmpty()){
            throw new IllegalArgumentException("NO SE PUEDE REGISTRAR UN MOVIMIENTO DE UNA CUENTA QUE NO EXISTE");
        }

        if(repositorio.buscarPorID(movimiento.getMovimientoID()).isPresent()){
            throw new IllegalArgumentException("NO SE PUEDE REGISTRAR UN MOVIMIENTO CON UN ID YA EXISTENTE");
        }

        return repositorio.guardar(movimiento);
    }

    public Optional<Movimiento> buscarPorID(int ID){
        return repositorio.buscarPorID(ID);
    }

    private List<Movimiento> listarTodos(){
        return repositorio.listarTodos();
    }

    public List<Movimiento> listarPorCuentaID(int ID){
        return repositorio.listarPorCuentaID(ID);
    }

    public List<Movimiento> listarPorUsuarioID(int usuarioID) {
        List<Cuenta> listado = cuentas.listarPorUsuarioID(usuarioID);
        List<Movimiento> movimientos = new ArrayList<>();

        for (Cuenta cuenta : listado) {
            movimientos.addAll(repositorio.listarPorCuentaID(cuenta.getID()));
        }

        return movimientos;
    }
}
