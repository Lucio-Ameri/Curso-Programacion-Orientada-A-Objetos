package com.fintech.app.repository.memory;

import com.fintech.app.model.Cuenta;
import com.fintech.app.repository.RepositorioCuenta;

import java.util.*;

public class RepositorioCuentaEnMemoria implements RepositorioCuenta {
    private final Map<Integer, Cuenta> repositorio;

    public RepositorioCuentaEnMemoria(){
        this.repositorio = new LinkedHashMap<Integer, Cuenta>();
    }

    @Override
    public Cuenta guardar(Cuenta cuenta){
        if(cuenta == null){
            throw new IllegalArgumentException("LA CUENTA A GUARDAR NO PUEDE SER NULL");
        }

        repositorio.put(cuenta.getID(), cuenta);
        return cuenta;
    }

    @Override
    public Optional<Cuenta> buscarPorID(int ID){
        return Optional.ofNullable(repositorio.get(ID));
    }

    @Override
    public List<Cuenta> listarCuentas(){
        return new ArrayList<>(repositorio.values());
    }

    @Override
    public List<Cuenta> listarPorUsuarioID(int ID){
        List<Cuenta> listado = new ArrayList<Cuenta>();

        for(Cuenta cuenta : repositorio.values()){
            if(cuenta.getUsuarioID() == ID){
                listado.add(cuenta);
            }
        }

        return listado;
    }

    @Override
    public void eliminarPorID(int ID){
        repositorio.remove(ID);
    }
}
