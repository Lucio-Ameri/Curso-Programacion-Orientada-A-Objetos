package com.fintech.app.service;

import com.fintech.app.model.Usuario;
import com.fintech.app.repository.RepositorioUsuario;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UsuarioService {
    private final RepositorioUsuario repositorioUsuario;

    public UsuarioService(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = Objects.requireNonNull(
                repositorioUsuario,
                "EL REPOSITORIO DE USUARIO NO PUEDE SER NULL"
        );
    }

    public Usuario registrarUsuario(Usuario usuario) {
        validarUsuarioNoNull(usuario, "EL USUARIO A REGISTRAR NO PUEDE SER NULL");

        if (repositorioUsuario.buscarPorID(usuario.getUsuarioID()).isPresent()) {
            throw new IllegalArgumentException("YA EXISTE UN USUARIO CON ESE ID");
        }

        if (repositorioUsuario.existePorEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("YA EXISTE UN USUARIO CON ESE EMAIL");
        }

        if (repositorioUsuario.existePorDNI(usuario.getDNI())) {
            throw new IllegalArgumentException("YA EXISTE UN USUARIO CON ESE DNI");
        }

        return repositorioUsuario.guardar(usuario);
    }

    public Usuario actualizarUsuario(Usuario usuario) {
        validarUsuarioNoNull(usuario, "EL USUARIO A ACTUALIZAR NO PUEDE SER NULL");

        repositorioUsuario.buscarPorID(usuario.getUsuarioID()).orElseThrow(() -> new IllegalArgumentException("NO EXISTE UN USUARIO CON ESE ID"));

        Optional<Usuario> usuarioConMismoEmail = repositorioUsuario.buscarPorEmail(usuario.getEmail());
        if (usuarioConMismoEmail.isPresent() && usuarioConMismoEmail.get().getUsuarioID() != usuario.getUsuarioID()) {
            throw new IllegalArgumentException("YA EXISTE OTRO USUARIO CON ESE EMAIL");
        }

        Optional<Usuario> usuarioConMismoDni = repositorioUsuario.buscarPorDNI(usuario.getDNI());
        if (usuarioConMismoDni.isPresent() && usuarioConMismoDni.get().getUsuarioID() != usuario.getUsuarioID()) {
            throw new IllegalArgumentException("YA EXISTE OTRO USUARIO CON ESE DNI");
        }

        return repositorioUsuario.guardar(usuario);
    }

    public Optional<Usuario> buscarPorID(int usuarioID) {
        return repositorioUsuario.buscarPorID(usuarioID);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        validarEmailNoNull(email);
        return repositorioUsuario.buscarPorEmail(email);
    }

    public Optional<Usuario> buscarPorDNI(String dni) {
        validarDniNoNull(dni);
        return repositorioUsuario.buscarPorDNI(dni);
    }

    public List<Usuario> listarUsuarios() {
        return repositorioUsuario.listarUsuarios();
    }

    public void eliminarPorID(int usuarioID) {
        repositorioUsuario.eliminarPorID(usuarioID);
    }

    public boolean existePorEmail(String email) {
        validarEmailNoNull(email);
        return repositorioUsuario.existePorEmail(email);
    }

    public boolean existePorDNI(String dni) {
        validarDniNoNull(dni);
        return repositorioUsuario.existePorDNI(dni);
    }

    private void validarUsuarioNoNull(Usuario usuario, String mensaje) {
        if (usuario == null) {
            throw new IllegalArgumentException(mensaje);
        }
    }

    private void validarEmailNoNull(String email) {
        if (email == null) {
            throw new IllegalArgumentException("EL EMAIL A BUSCAR NO PUEDE SER NULL");
        }
    }

    private void validarDniNoNull(String DNI) {
        if (DNI == null) {
            throw new IllegalArgumentException("EL DNI A BUSCAR NO PUEDE SER NULL");
        }
    }
}
