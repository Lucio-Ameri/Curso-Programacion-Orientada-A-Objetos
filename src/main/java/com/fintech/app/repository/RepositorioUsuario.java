package com.fintech.app.repository;

import com.fintech.app.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface RepositorioUsuario {
    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorID(int usuarioID);

    Optional<Usuario> buscarPorEmail(String email);

    Optional<Usuario> buscarPorDNI(String DNI);

    List<Usuario> listarUsuarios();

    void eliminarPorID(int usuarioID);

    boolean existePorEmail(String email);

    boolean existePorDNI(String DNI);
}
