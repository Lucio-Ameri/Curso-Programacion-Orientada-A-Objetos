package com.fintech.app.service;

import com.fintech.app.model.Usuario;
import com.fintech.app.repository.RepositorioUsuario;
import com.fintech.app.repository.memory.RepositorioUsuarioEnMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TestUsuarioService {
    private UsuarioService servicio;
    private RepositorioUsuario repositorio;

    @BeforeEach
    void setUp() {
        repositorio = new RepositorioUsuarioEnMemoria();
        servicio = new UsuarioService(repositorio);
    }

    private Usuario crearUsuario(int id, String email, String dni) {
        return new Usuario(id, "Pepito", "Perez", email, "Password123!", dni);
    }

    // =========================
    // CONSTRUCTOR
    // =========================

    @Test
    void TestConstructorConRepositorioNullDeberiaLanzarExcepcion() {
        assertThrows(NullPointerException.class, () -> new UsuarioService(null));
    }

    // =========================
    // REGISTRAR USUARIO
    // =========================

    @Test
    void TestRegistrarUsuarioDeberiaGuardarCorrectamente() {
        Usuario usuario = crearUsuario(1, "pepito@gmail.com", "12345678");

        Usuario guardado = servicio.registrarUsuario(usuario);

        assertEquals(usuario, guardado);
        assertTrue(servicio.buscarPorID(1).isPresent());
    }

    @Test
    void TestRegistrarUsuarioNullDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> servicio.registrarUsuario(null));
    }

    @Test
    void TestRegistrarUsuarioConMismoIdDeberiaLanzarExcepcion() {
        Usuario usuario1 = crearUsuario(1, "pepito@gmail.com", "12345678");
        Usuario usuario2 = crearUsuario(1, "juan@gmail.com", "87654321");

        servicio.registrarUsuario(usuario1);

        assertThrows(IllegalArgumentException.class, () -> servicio.registrarUsuario(usuario2));
    }

    @Test
    void TestRegistrarUsuarioConMismoEmailDeberiaLanzarExcepcion() {
        Usuario usuario1 = crearUsuario(1, "pepito@gmail.com", "12345678");
        Usuario usuario2 = crearUsuario(2, "pepito@gmail.com", "87654321");

        servicio.registrarUsuario(usuario1);

        assertThrows(IllegalArgumentException.class, () -> servicio.registrarUsuario(usuario2));
    }

    @Test
    void TestRegistrarUsuarioConMismoDniDeberiaLanzarExcepcion() {
        Usuario usuario1 = crearUsuario(1, "pepito@gmail.com", "12345678");
        Usuario usuario2 = crearUsuario(2, "juan@gmail.com", "12345678");

        servicio.registrarUsuario(usuario1);

        assertThrows(IllegalArgumentException.class, () -> servicio.registrarUsuario(usuario2));
    }

    // =========================
    // ACTUALIZAR USUARIO
    // =========================

    @Test
    void TestActualizarUsuarioDeberiaActualizarCorrectamente() {
        Usuario original = crearUsuario(1, "pepito@gmail.com", "12345678");
        Usuario actualizado = new Usuario(1, "PepitoModificado", "Pepon", "pepito@gmail.com", "Password123!", "12345678");

        servicio.registrarUsuario(original);

        Usuario resultado = servicio.actualizarUsuario(actualizado);

        assertEquals(actualizado, resultado);

        Optional<Usuario> buscado = servicio.buscarPorID(1);
        assertTrue(buscado.isPresent());
        assertEquals("PepitoModificado", buscado.get().getNombre());
        assertEquals("Pepon", buscado.get().getApellido());
    }

    @Test
    void TestActualizarUsuarioNullDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> servicio.actualizarUsuario(null));
    }

    @Test
    void TestActualizarUsuarioInexistenteDeberiaLanzarExcepcion() {
        Usuario usuario = crearUsuario(1, "pepito@gmail.com", "12345678");

        assertThrows(IllegalArgumentException.class, () -> servicio.actualizarUsuario(usuario));
    }

    @Test
    void TestActualizarUsuarioConEmailDeOtroUsuarioDeberiaLanzarExcepcion() {
        Usuario usuario1 = crearUsuario(1, "pepito@gmail.com", "12345678");
        Usuario usuario2 = crearUsuario(2, "juan@gmail.com", "87654321");
        Usuario usuario2Actualizado = new Usuario(2, "Juan", "Perez", "pepito@gmail.com", "Password123!", "87654321");

        servicio.registrarUsuario(usuario1);
        servicio.registrarUsuario(usuario2);

        assertThrows(IllegalArgumentException.class, () -> servicio.actualizarUsuario(usuario2Actualizado));
    }

    @Test
    void TestActualizarUsuarioConDniDeOtroUsuarioDeberiaLanzarExcepcion() {
        Usuario usuario1 = crearUsuario(1, "pepito@gmail.com", "12345678");
        Usuario usuario2 = crearUsuario(2, "juan@gmail.com", "87654321");
        Usuario usuario2Actualizado = new Usuario(2, "Juan", "Perez", "juan@gmail.com", "Password123!", "12345678");

        servicio.registrarUsuario(usuario1);
        servicio.registrarUsuario(usuario2);

        assertThrows(IllegalArgumentException.class, () -> servicio.actualizarUsuario(usuario2Actualizado));
    }

    @Test
    void TestActualizarUsuarioConMismoEmailYMismoDniDeberiaPermitirActualizar() {
        Usuario original = crearUsuario(1, "pepito@gmail.com", "12345678");
        Usuario actualizado = new Usuario(1, "PepitoNuevo", "PerezNuevo", "pepito@gmail.com", "Password123!", "12345678");

        servicio.registrarUsuario(original);

        assertDoesNotThrow(() -> servicio.actualizarUsuario(actualizado));
    }

    // =========================
    // BUSCAR POR ID
    // =========================

    @Test
    void TestBuscarPorIdCuandoExisteDeberiaDevolverUsuario() {
        Usuario usuario = crearUsuario(1, "pepito@gmail.com", "12345678");
        servicio.registrarUsuario(usuario);

        Optional<Usuario> resultado = servicio.buscarPorID(1);

        assertTrue(resultado.isPresent());
        assertEquals(usuario, resultado.get());
    }

    @Test
    void TestBuscarPorIdCuandoNoExisteDeberiaDevolverEmpty() {
        Optional<Usuario> resultado = servicio.buscarPorID(99);

        assertTrue(resultado.isEmpty());
    }

    // =========================
    // BUSCAR POR EMAIL
    // =========================

    @Test
    void TestBuscarPorEmailCuandoExisteDeberiaDevolverUsuario() {
        Usuario usuario = crearUsuario(1, "pepito@gmail.com", "12345678");
        servicio.registrarUsuario(usuario);

        Optional<Usuario> resultado = servicio.buscarPorEmail("pepito@gmail.com");

        assertTrue(resultado.isPresent());
        assertEquals(usuario, resultado.get());
    }

    @Test
    void TestBuscarPorEmailCuandoNoExisteDeberiaDevolverEmpty() {
        Optional<Usuario> resultado = servicio.buscarPorEmail("noexiste@gmail.com");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void TestBuscarPorEmailNullDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> servicio.buscarPorEmail(null));
    }

    // =========================
    // BUSCAR POR DNI
    // =========================

    @Test
    void TestBuscarPorDniCuandoExisteDeberiaDevolverUsuario() {
        Usuario usuario = crearUsuario(1, "pepito@gmail.com", "12345678");
        servicio.registrarUsuario(usuario);

        Optional<Usuario> resultado = servicio.buscarPorDNI("12345678");

        assertTrue(resultado.isPresent());
        assertEquals(usuario, resultado.get());
    }

    @Test
    void TestBuscarPorDniCuandoNoExisteDeberiaDevolverEmpty() {
        Optional<Usuario> resultado = servicio.buscarPorDNI("99999999");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void TestBuscarPorDniNullDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> servicio.buscarPorDNI(null));
    }

    // =========================
    // LISTAR USUARIOS
    // =========================

    @Test
    void TestListarUsuariosCuandoNoHayUsuariosDeberiaDevolverListaVacia() {
        List<Usuario> usuarios = servicio.listarUsuarios();

        assertTrue(usuarios.isEmpty());
    }

    @Test
    void TestListarUsuariosCuandoHayUsuariosDeberiaDevolverListaCompleta() {
        Usuario usuario1 = crearUsuario(1, "pepito@gmail.com", "12345678");
        Usuario usuario2 = crearUsuario(2, "juan@gmail.com", "87654321");

        servicio.registrarUsuario(usuario1);
        servicio.registrarUsuario(usuario2);

        List<Usuario> usuarios = servicio.listarUsuarios();

        assertEquals(2, usuarios.size());
        assertTrue(usuarios.contains(usuario1));
        assertTrue(usuarios.contains(usuario2));
    }

    // =========================
    // ELIMINAR POR ID
    // =========================

    @Test
    void TestEliminarPorIdCuandoExisteDeberiaEliminarUsuario() {
        Usuario usuario = crearUsuario(1, "pepito@gmail.com", "12345678");
        servicio.registrarUsuario(usuario);

        servicio.eliminarPorID(1);

        assertTrue(servicio.buscarPorID(1).isEmpty());
    }

    @Test
    void TestEliminarPorIdCuandoNoExisteNoDeberiaRomper() {
        assertDoesNotThrow(() -> servicio.eliminarPorID(99));
    }

    // =========================
    // EXISTE POR EMAIL
    // =========================

    @Test
    void TestExistePorEmailCuandoExisteDeberiaDevolverTrue() {
        Usuario usuario = crearUsuario(1, "pepito@gmail.com", "12345678");
        servicio.registrarUsuario(usuario);

        assertTrue(servicio.existePorEmail("pepito@gmail.com"));
    }

    @Test
    void TestExistePorEmailCuandoNoExisteDeberiaDevolverFalse() {
        assertFalse(servicio.existePorEmail("noexiste@gmail.com"));
    }

    @Test
    void TestExistePorEmailNullDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> servicio.existePorEmail(null));
    }

    // =========================
    // EXISTE POR DNI
    // =========================

    @Test
    void TestExistePorDniCuandoExisteDeberiaDevolverTrue() {
        Usuario usuario = crearUsuario(1, "pepito@gmail.com", "12345678");
        servicio.registrarUsuario(usuario);

        assertTrue(servicio.existePorDNI("12345678"));
    }

    @Test
    void TestExistePorDniCuandoNoExisteDeberiaDevolverFalse() {
        assertFalse(servicio.existePorDNI("99999999"));
    }

    @Test
    void TestExistePorDniNullDeberiaLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> servicio.existePorDNI(null));
    }
}
