package com.fintech.app.repository.memory;

import com.fintech.app.model.Usuario;
import com.fintech.app.repository.RepositorioUsuario;

import java.util.*;

public class RepositorioUsuarioEnMemoria implements RepositorioUsuario {

    private final Map<Integer, Usuario> listadoUsuarios;

    public RepositorioUsuarioEnMemoria(){
        this.listadoUsuarios = new LinkedHashMap<>();
    }

    private String normalizarEmail(String email){
        return email.trim().toLowerCase(Locale.ROOT);
    }

    @Override
    public Usuario guardar(Usuario usuario){
        if(usuario == null){
            throw new IllegalArgumentException("EL USUARIO A GUARDAR NO PUEDE SER NULL");
        }

        Optional<Usuario> usuarioConMismoEmail = buscarPorEmail(usuario.getEmail());
        if(usuarioConMismoEmail.isPresent() && usuarioConMismoEmail.get().getUsuarioID() != usuario.getUsuarioID()){
            throw new IllegalArgumentException("EL EMAIL YA ESTA EN USO, NO PUEDEN HABER REPETIDOS");
        }

        Optional<Usuario> usuarioConMismoDni = buscarPorDNI(usuario.getDNI());
        if(usuarioConMismoDni.isPresent() && usuarioConMismoDni.get().getUsuarioID() != usuario.getUsuarioID()){
            throw new IllegalArgumentException("EL DNI YA ESTA EN USO, NO PUEDEN HABER REPETIDOS");
        }

        listadoUsuarios.put(usuario.getUsuarioID(), usuario);
        return usuario;
    }

    @Override
    public Optional<Usuario> buscarPorID(int usuarioID){
        return Optional.ofNullable(listadoUsuarios.get(usuarioID));
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email){
        if(email == null){
            throw new IllegalArgumentException("EL EMAIL A BUSCAR NO PUEDE SER NULL");
        }

        String buscar = normalizarEmail(email);
        return listadoUsuarios.values().stream().filter(usuario -> normalizarEmail(usuario.getEmail()).equals(buscar)).findFirst();
    }

    @Override
    public Optional<Usuario> buscarPorDNI(String DNI){
        if(DNI == null){
            throw new IllegalArgumentException("EL DNI A BUSCAR NO PUEDE SER NULL");
        }

        return listadoUsuarios.values().stream().filter(usuario -> DNI.equals(usuario.getDNI())).findFirst();
    }

    @Override
    public List<Usuario> listarUsuarios(){
        return new ArrayList<>(listadoUsuarios.values());
    }

    @Override
    public void eliminarPorID(int usuarioID){
        listadoUsuarios.remove(usuarioID);
    }

    @Override
    public boolean existePorEmail(String email){
        if(email == null){
            throw new IllegalArgumentException("NO SE PUEDE BUSCAR UN EMAIL NULL");
        }

        String buscar = normalizarEmail(email);
        return listadoUsuarios.values().stream().anyMatch(usuario -> normalizarEmail(usuario.getEmail()).equals(buscar));
    }

    @Override
    public boolean existePorDNI(String DNI){
        if(DNI == null){
            throw new IllegalArgumentException("NO SE PUEDE BUSCAR UN DNI NULL");
        }

        return listadoUsuarios.values().stream().anyMatch(usuario -> DNI.equals(usuario.getDNI()));
    }
}
