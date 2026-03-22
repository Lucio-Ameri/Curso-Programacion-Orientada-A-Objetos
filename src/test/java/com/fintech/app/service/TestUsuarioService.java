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
    private RepositorioUsuario memoria;
    private UsuarioService servicios;

    @BeforeEach
    public void iniciarPruebas() {
        memoria = new RepositorioUsuarioEnMemoria();
        servicios = new UsuarioService(memoria);
    }

    private Usuario crearUsuario(int ID, String email, String DNI) {
        return new Usuario(ID, "Pepito", "Pepon", email, "Password1234!", DNI);
    }

    @Test
    public void TestCrearUsuarioServiceLanzaExcepcionPorRepositorioNull() {
        assertThrows(IllegalArgumentException.class, () -> new UsuarioService(null));
    }

    @Test
    public void TestRegistrarUsuarioDevuelveElMismoUsuarioYLoGuardaCorrectamente() {
        Usuario usuario = crearUsuario(1, "Pepito@gmail.com", "12345678");

        Usuario guardado = servicios.registrarUsuario(usuario);

        assertEquals(guardado, usuario);
        assertTrue(servicios.buscarPorID(1).isPresent());
    }

    @Test
    public void TestRegistrarUsuarioLanzaExcepcionSiEsNull() {
        assertThrows(IllegalArgumentException.class, () -> servicios.registrarUsuario(null));
    }

    @Test
    public void TestRegistrarUsuarioLanzaExcepcionSiSeIntentaConUnIDYaExistente() {
        Usuario usuario = crearUsuario(1, "Pepito@gmail.com", "12345678");
        Usuario usuario2 = crearUsuario(1, "Pepito2@gmail.com", "11111111");

        servicios.registrarUsuario(usuario);
        assertThrows(IllegalArgumentException.class, () -> servicios.registrarUsuario(usuario2));
    }

    @Test
    public void TestRegistrarUsuarioLanzaExcepcionSiSeIntentaConUnEmailYaExistente() {
        Usuario usuario = crearUsuario(1, "Pepito@gmail.com", "12345678");
        Usuario usuario2 = crearUsuario(2, "Pepito@gmail.com", "11111111");

        servicios.registrarUsuario(usuario);
        assertThrows(IllegalArgumentException.class, () -> servicios.registrarUsuario(usuario2));
    }

    @Test
    public void TestRegistrarUsuarioLanzaExcepcionSiSeIntentaConUnDNIYaExistente() {
        Usuario usuario = crearUsuario(1, "Pepito@gmail.com", "12345678");
        Usuario usuario2 = crearUsuario(2, "Pepito2@gmail.com", "12345678");

        servicios.registrarUsuario(usuario);
        assertThrows(IllegalArgumentException.class, () -> servicios.registrarUsuario(usuario2));
    }

    @Test //Mismo ID.
    public void TestActualizarUsuarioNoLanzaExcepcionSiEsElMismoUsuario() {
        Usuario usuario = crearUsuario(1, "Pepito@gmail.com", "12345678");
        Usuario actualizado = new Usuario(1, "PepitoModificado", "Pepon", "Pepito2@gmail.com", "Password12345!", "11111111");

        servicios.registrarUsuario(usuario);
        Usuario resultado = servicios.actualizarUsuario(actualizado);

        assertEquals(actualizado, resultado);

        Optional<Usuario> buscar = servicios.buscarPorID(1);
        assertTrue(buscar.isPresent());
        assertEquals("PepitoModificado", buscar.get().getNombre());
        assertEquals(actualizado.getEmail(), buscar.get().getEmail());
        assertEquals("Password12345!", buscar.get().getPassword());
        assertEquals("11111111", buscar.get().getDNI());
    }

    @Test
    public void TestActualizarUsuarioLanzaExcepcionSiElMismoEsNull() {
        assertThrows(IllegalArgumentException.class, () -> servicios.actualizarUsuario(null));
    }

    @Test
    public void TestActualizarUsuarioLanzaExcepcionSiElMismoNoExiste() {
        Usuario inexistente = crearUsuario(99, "NoExiste@gmail.com", "00000000");
        assertThrows(IllegalArgumentException.class, () -> servicios.actualizarUsuario(inexistente));
    }

    @Test
    public void TestActualizarUsuarioLanzaExcepcionSiSeIntentaConElEmailDeOtroUsuario() {
        Usuario usuario = crearUsuario(1, "Pepito@gmail.com", "12345678");
        Usuario usuario2 = crearUsuario(2, "Pepito2@gmail.com", "11111111");

        servicios.registrarUsuario(usuario);
        servicios.registrarUsuario(usuario2);

        Usuario actualizado = crearUsuario(2, "Pepito@gmail.com", "00000000");
        assertThrows(IllegalArgumentException.class, () -> servicios.actualizarUsuario(actualizado));
    }

    @Test
    public void TestActualizarUsuarioLanzaExcepcionSiSeIntentaConElDNIDeOtroUsuario() {
        Usuario usuario = crearUsuario(1, "Pepito@gmail.com", "12345678");
        Usuario usuario2 = crearUsuario(2, "Pepito2@gmail.com", "11111111");

        servicios.registrarUsuario(usuario);
        servicios.registrarUsuario(usuario2);

        Usuario actualizado = crearUsuario(2, "Pepito2@gmail.com", "12345678");
        assertThrows(IllegalArgumentException.class, () -> servicios.actualizarUsuario(actualizado));
    }

    @Test
    public void TestBuscarPorIDDevuelveAlUsuarioSiEsQueExiste() {
        Usuario usuario = crearUsuario(1, "Pepito@gmail.com", "12345678");

        servicios.registrarUsuario(usuario);

        Optional<Usuario> buscado = servicios.buscarPorID(1);
        assertTrue(buscado.isPresent());
        assertEquals(usuario, buscado.get());
    }

    @Test
    public void TestBuscarPorIDDevuelveEmptySiEsQueNoExiste() {
        Optional<Usuario> buscado = servicios.buscarPorID(1);
        assertTrue(buscado.isEmpty());
    }

    @Test
    public void TestBuscarPorEmailDevuelveAlUsuarioSiEsQueExiste() {
        Usuario usuario = crearUsuario(1, "Pepito@gmail.com", "12345678");

        servicios.registrarUsuario(usuario);

        Optional<Usuario> buscado = servicios.buscarPorEmail(usuario.getEmail());
        assertTrue(buscado.isPresent());
        assertEquals(usuario, buscado.get());
    }

    @Test
    public void TestBuscarPorEmailDevuelveEmptySiEsQueNoExiste() {
        Optional<Usuario> buscado = servicios.buscarPorEmail("NoExiste@gmail.com");
        assertTrue(buscado.isEmpty());
    }

    @Test
    public void TestBuscarPorEmailLanzaExcepcionSiElEmailEsNull() {
        assertThrows(IllegalArgumentException.class, () -> servicios.buscarPorEmail(null));
    }

    @Test
    public void TestBuscarPorDNIDevuelveAlUsuarioSiEsQueExiste() {
        Usuario usuario = crearUsuario(1, "Pepito@gmail.com", "12345678");

        servicios.registrarUsuario(usuario);

        Optional<Usuario> buscado = servicios.buscarPorDNI(usuario.getDNI());
        assertTrue(buscado.isPresent());
        assertEquals(usuario, buscado.get());
    }

    @Test
    public void TestBuscarPorDNIDevuelveEmptySiEsQueNoExiste() {
        Optional<Usuario> buscado = servicios.buscarPorDNI("00000000");
        assertTrue(buscado.isEmpty());
    }

    @Test
    public void TestBuscarPorDNILanzaExcepcionSiElEmailEsNull() {
        assertThrows(IllegalArgumentException.class, () -> servicios.buscarPorDNI(null));
    }

    @Test
    public void TestListarUsuariosDevuelveLaListaCompletaSiEsQueHayUsuarios() {
        Usuario usuario = crearUsuario(1, "Pepito@gmail.com", "12345678");
        Usuario usuario2 = crearUsuario(2, "Pepito2@gmail.com", "11111111");
        Usuario usuario3 = crearUsuario(3, "Pepito3@gmail.com", "22222222");

        servicios.registrarUsuario(usuario);
        servicios.registrarUsuario(usuario2);
        servicios.registrarUsuario(usuario3);

        List<Usuario> lista = servicios.listarUsuarios();

        assertFalse(lista.isEmpty());
        assertEquals(3, lista.size());
        assertTrue(lista.contains(usuario));
        assertTrue(lista.contains(usuario2));
        assertTrue(lista.contains(usuario3));
    }

    @Test
    public void TestListarUsuariosDevuelveUnaListaVaciaSiEsQueNoHayUsuarios() {
        assertTrue(servicios.listarUsuarios().isEmpty());
    }

    @Test
    public void TestEliminarPorIDCuandoExisteElUsuarioDeberiaDesaparecer() {
        Usuario usuario = crearUsuario(1, "Pepito@gmail.com", "12345678");

        servicios.registrarUsuario(usuario);

        servicios.eliminarPorID(1);

        assertTrue(servicios.buscarPorID(1).isEmpty());
    }

    @Test
    public void TestEliminarPorIDCuandoNoExisteElUsuarioNoRompe() {
        assertDoesNotThrow(() -> servicios.eliminarPorID(99));
    }

    @Test
    public void TestExistePorEmailDevuelveTrueSiEsQueEstaElUsuario() {
        Usuario usuario = crearUsuario(1, "Pepito@gmail.com", "12345678");

        servicios.registrarUsuario(usuario);

        assertTrue(servicios.existePorEmail(usuario.getEmail()));
    }

    @Test
    public void TestExistePorEmailDevuelveFalseSiEsQueNoEstaElUsuario() {
        assertFalse(servicios.existePorEmail("NoExiste@gmail.com"));
    }

    @Test
    public void TestExistePorEmailLanzaExcepcionSiEsQueElEmailEsNull() {
        assertThrows(IllegalArgumentException.class, () -> servicios.existePorEmail(null));
    }

    @Test
    public void TestExistePorDNIDevuelveTrueSiEsQueEstaElUsuario() {
        Usuario usuario = crearUsuario(1, "Pepito@gmail.com", "12345678");

        servicios.registrarUsuario(usuario);

        assertTrue(servicios.existePorDNI(usuario.getDNI()));
    }

    @Test
    public void TestExistePorDNIDevuelveFalseSiEsQueNoEstaElUsuario() {
        assertFalse(servicios.existePorDNI("00000000"));
    }

    @Test
    public void TestExistePorDNILanzaExcepcionSiEsQueElEmailEsNull() {
        assertThrows(IllegalArgumentException.class, () -> servicios.existePorDNI(null));
    }

    @Test
    public void TestAutenticarUsuarioDevuelveAlUsuarioSiEsQueLaContraseñaYElEmailSonCorrectos() {

    }

    @Test
    public void TestAutenticarUsuarioLanzaExcepcionCuandoElEmailEsNullOEnBlanco() {

    }

    @Test
    public void TestAutenticarUsuarioLanzaExcepcionCuandoLaContraseñaEsNullOEnBlanco() {

    }

    @Test
    public void TestAutenticarUsuarioLanzaExcepcionCuandoNoExisteUnUsuarioConEseEmail() {

    }

    @Test
    public void TestAutenticarUsuarioLanzaExcepcionCuandoLaContraseñaEsIncorrecta() {

    }
}