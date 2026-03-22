package com.fintech.app.service;

import com.fintech.app.model.Usuario;
import com.fintech.app.repository.RepositorioUsuario;

import java.util.List;
import java.util.Optional;

public class UsuarioService {
    private final RepositorioUsuario repositorio;

    public UsuarioService(RepositorioUsuario repositorio){
        if(repositorio == null){
            throw new IllegalArgumentException("EL REPOSITORIO NO PUEDE SER NULL");
        }
        this.repositorio = repositorio;
    }

    public Usuario registrarUsuario(Usuario usuario){
        if(usuario == null){
            throw new IllegalArgumentException("NO SE PUEDE REGISTRAR UN USUARIO NULL");
        }

        if(repositorio.buscarPorID(usuario.getUsuarioID()).isPresent()){
            throw new IllegalArgumentException("NO SE PUEDE REGISTRAR UN USUARIO CON UN ID YA EXISTENTE");
        }

        if(repositorio.existePorEmail(usuario.getEmail())){
            throw new IllegalArgumentException("NO SE PUEDE REGISTRAR UN USUARIO CON UN EMAIL YA EXISTENTE");
        }

        if(repositorio.existePorDNI(usuario.getDNI())){
            throw new IllegalArgumentException("NO SE PUEDE REGISTRAR UN USUARIO CON UN DNI YA EXISTENTE");
        }

        return repositorio.guardar(usuario);
    }

    public Usuario actualizarUsuario(Usuario usuario){
        if(usuario == null){
            throw new IllegalArgumentException("NO SE PUEDE ACTUALIZAR UN USUARIO NULL");
        }

        if(repositorio.buscarPorID(usuario.getUsuarioID()).isEmpty()){
            throw new IllegalArgumentException("NO SE PUEDE ACTUALIZAR UN USUARIO QUE NO EXISTE");
        }

        Optional<Usuario> mismoEmail = repositorio.buscarPorEmail(usuario.getEmail());
        if(mismoEmail.isPresent() && mismoEmail.get().getUsuarioID() != usuario.getUsuarioID()){
            throw new IllegalArgumentException("NO SE PUEDE ACTUALIZAR UN USUARIO QUE TIENE EL EMAIL DE OTRO");
        }

        Optional<Usuario> mismoDNI = repositorio.buscarPorDNI(usuario.getDNI());
        if(mismoDNI.isPresent() && mismoDNI.get().getUsuarioID() != usuario.getUsuarioID()){
            throw new IllegalArgumentException("NO SE PUEDE ACTUALIZAR UN USUARIO QUE TIENE EL DNI DE OTRO");
        }

        return repositorio.guardar(usuario);
    }

    public Optional<Usuario> buscarPorID(int ID){
        return repositorio.buscarPorID(ID);
    }

    public Optional<Usuario> buscarPorEmail(String email){
        if(email == null){
            throw new IllegalArgumentException("EMAIL NULL, NO SE PUEDE BUSCAR");
        }
        return repositorio.buscarPorEmail(email);
    }

    public Optional<Usuario> buscarPorDNI(String DNI){
        if(DNI == null){
            throw new IllegalArgumentException("DNI NULL, NO SE PUEDE BUSCAR");
        }
        return repositorio.buscarPorDNI(DNI);
    }

    public List<Usuario> listarUsuarios(){
        return repositorio.listarUsuarios();
    }

    public void eliminarPorID(int ID){
        repositorio.eliminarPorID(ID);
    }

    public boolean existePorEmail(String email){
        if(email == null){
            throw new IllegalArgumentException("EL EMAIL A BUSCAR NO PUEDE SER NULL");
        }

        return repositorio.existePorEmail(email);
    }

    public boolean existePorDNI(String DNI){
        if(DNI == null){
            throw new IllegalArgumentException("EL DNI A BUSCAR NO PUEDE SER NULL");
        }

        return repositorio.existePorDNI(DNI);
    }

    public Usuario autenticar(String email, String password){
        if(email == null || email.isBlank()){
            throw new IllegalArgumentException("EL EMAIL INGRESADO ESTA EN BLANCO O ES NULL...");
        }

        if(password == null || password.isBlank()){
            throw new IllegalArgumentException("LA CONTRASEÑA INGRESADA ESTA EN BLANCO O ES NULL...");
        }

        Usuario usuario = repositorio.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("NO EXISTE UN USUARIO CON ESE EMAIL"));

        if(!usuario.getPassword().equals(password)){
            throw new IllegalArgumentException("CONTRASEÑA INCORRECTA");
        }

        return usuario;
    }
}
