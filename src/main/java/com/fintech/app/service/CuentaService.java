package com.fintech.app.service;

import com.fintech.app.model.Cuenta;
import com.fintech.app.model.Dinero;
import com.fintech.app.model.Movimiento;
import com.fintech.app.model.enums.TipoMovimiento;
import com.fintech.app.repository.RepositorioCuenta;
import com.fintech.app.repository.RepositorioMovimiento;
import com.fintech.app.repository.RepositorioUsuario;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CuentaService {
    private final RepositorioCuenta repositorioCuenta;
    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioMovimiento repositorioMovimiento;

    public CuentaService(RepositorioCuenta rc, RepositorioUsuario ru, RepositorioMovimiento rm){
        if(rc == null || ru == null || rm == null){
            throw new IllegalArgumentException("LOS DATOS DE LOS PARAMETROS NO PUEDEN SER NULL");
        }

        this.repositorioCuenta = rc;
        this.repositorioUsuario = ru;
        this.repositorioMovimiento = rm;
    }

    public Cuenta registrarCuenta(Cuenta cuenta){
        if(cuenta == null){
            throw new IllegalArgumentException("NO SE PUEDE REGISTRAR UNA CUENTA NULL");
        }

        if(repositorioUsuario.buscarPorID(cuenta.getUsuarioID()).isEmpty()){
            throw new IllegalArgumentException("SE ESTA INTENTANDO REGISTRAR LA CUENTA CON EL ID DE UN USUARIO QUE NO EXISTE");
        }

        if(repositorioCuenta.buscarPorID(cuenta.getID()).isPresent()){
            throw new IllegalArgumentException("SE ESTA INTENTANDO REGISTRAR LA CUENTA CON EL ID DE UNA YA EXISTENTE");
        }

        return repositorioCuenta.guardar(cuenta);
    }

    public Cuenta actualizarCuenta(Cuenta cuenta){
        if(cuenta == null){
            throw new IllegalArgumentException("NO SE PUEDE ACTUALIZAR UNA CUENTA NULL");
        }

        if(repositorioUsuario.buscarPorID(cuenta.getUsuarioID()).isEmpty()){
            throw new IllegalArgumentException("SE ESTA INTENTANDO ACTUALIZAR LA CUENTA CON EL ID DE UN USUARIO QUE NO EXISTE");
        }

        if(repositorioCuenta.buscarPorID(cuenta.getID()).isEmpty()){
            throw new IllegalArgumentException("SE ESTA INTENTANDO ACTUALIZAR UNA CUENTA QUE NO EXISTE");
        }

        return repositorioCuenta.guardar(cuenta);
    }

    public Optional<Cuenta> buscarPorID(int ID){
        return repositorioCuenta.buscarPorID(ID);
    }

    public List<Cuenta> listarCuentas() {
        return repositorioCuenta.listarCuentas();
    }

    public List<Cuenta> listarPorUsuarioID(int usuarioID){
        return repositorioCuenta.listarPorUsuarioID(usuarioID);
    }

    public void eliminarPorID(int ID){
        repositorioCuenta.eliminarPorID(ID);
    }

    public Cuenta depositarDinero(int ID, Dinero dinero, int movimientoID){
        if(dinero == null){
            throw new IllegalArgumentException("NO SE PUEDE DEPOSITAR UN DINERO NULL");
        }

        Optional<Cuenta> c = repositorioCuenta.buscarPorID(ID);
        if(c.isEmpty()){
            throw new IllegalArgumentException("NO SE PUEDE DEPOSITAR DINERO EN UNA CUENTA QUE NO EXISTE");
        }


        Movimiento mov = new Movimiento(movimientoID, TipoMovimiento.DEPOSITO, dinero, LocalDateTime.now(), ID);
        repositorioMovimiento.guardar(mov);

        c.get().depositar(dinero);
        return repositorioCuenta.guardar(c.get());
    }

    public Cuenta retirarDinero(int ID, Dinero dinero, int movimientoID){
        if(dinero == null){
            throw new IllegalArgumentException("NO SE PUEDE RETIRAR UN DINERO NULL");
        }

        Optional<Cuenta> c = repositorioCuenta.buscarPorID(ID);
        if(c.isEmpty()){
            throw new IllegalArgumentException("NO SE PUEDE RETIRAR DINERO EN UNA CUENTA QUE NO EXISTE");
        }

        Movimiento mov = new Movimiento(movimientoID, TipoMovimiento.RETIRO, dinero, LocalDateTime.now(), ID);
        repositorioMovimiento.guardar(mov);

        c.get().retirar(dinero);
        return repositorioCuenta.guardar(c.get());
    }
}
