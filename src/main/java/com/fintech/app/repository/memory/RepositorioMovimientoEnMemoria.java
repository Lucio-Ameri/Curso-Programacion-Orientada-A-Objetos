package com.fintech.app.repository.memory;

import com.fintech.app.model.Movimiento;
import com.fintech.app.repository.RepositorioMovimiento;

import java.util.*;

public class RepositorioMovimientoEnMemoria implements RepositorioMovimiento {
    private final Map<Integer, Movimiento> repositorio;

    public RepositorioMovimientoEnMemoria(){
        this.repositorio = new LinkedHashMap<Integer, Movimiento>();
    }

    @Override
    public Movimiento guardar(Movimiento movimiento){
        if(movimiento == null){
            throw new IllegalArgumentException("NO SE PUEDE GUARDAR UN MOVIMIENTO NULL");
        }

        if(repositorio.containsKey(movimiento.getMovimientoID())){
            throw new IllegalArgumentException("NO SE ESTA INTENTANDO GUARDAR UN MOVIMIENTO CON UN ID YA EXISTENTE");
        }

        repositorio.put(movimiento.getMovimientoID(), movimiento);
        return movimiento;
    }

    @Override
    public Optional<Movimiento> buscarPorID(int ID){
        return Optional.ofNullable(repositorio.get(ID));
    }

    @Override
    public List<Movimiento> listarTodos(){
        return new ArrayList<>(repositorio.values());
    }

    @Override
    public List<Movimiento> listarPorCuentaID(int ID){
        List<Movimiento> movimientos = new ArrayList<Movimiento>();

        for(Movimiento m: repositorio.values()){
            if(m.getCuentaID() == ID){
                movimientos.add(m);
            }
        }

        return movimientos;
    }
}
