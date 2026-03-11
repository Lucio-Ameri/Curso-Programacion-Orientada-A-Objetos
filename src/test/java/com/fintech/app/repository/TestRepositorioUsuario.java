package com.fintech.app.repository;

import com.fintech.app.model.Usuario;
import com.fintech.app.repository.memory.RepositorioUsuarioEnMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TestRepositorioUsuario {

    private RepositorioUsuarioEnMemoria listado;

    @BeforeEach
    void inicializarListado(){
        listado = new RepositorioUsuarioEnMemoria();
    }

    private Usuario crearUsuario(int ID, String email, String DNI){
        return new Usuario(ID, "Pepito", "Pepon", email, "Password123!", DNI);
    }

    @Test
    public void TestGuardarDeberiaFuncionarCorrectamente(){
        Usuario u = crearUsuario(1, "PepitoPepon@gmail.com", "12345678");

        Usuario guardado = listado.guardar(u);

        assertEquals(u, guardado);
        assertTrue(listado.buscarPorID(1).isPresent());
    }

    @Test
    public void TestGuardarDeberiaLanzarExcepcionCuandoUsuarioEsNull(){
        assertThrows(IllegalArgumentException.class, () -> listado.guardar(null));
    }

    @Test
    public void TestBuscarPorIDCuandoExisteDevuelveAlUsuario(){
        Usuario u = crearUsuario(1, "PepitoPepon@gmail.com", "12345678");
        listado.guardar(u);

        Optional<Usuario> resultado = listado.buscarPorID(1);

        assertTrue(resultado.isPresent());
        assertEquals(u, resultado.get());
    }

    @Test
    public void TestBuscarPorIDCuandoNoExisteDevuelveEmpty(){
        Optional<Usuario> resultado = listado.buscarPorID(99);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void TestBuscarPorEmailCuandoExisteDevuevleAlUsuario(){
        Usuario u = crearUsuario(1, "PepitoPepon@gmail.com", "12345678");
        listado.guardar(u);

        Optional<Usuario> resultado = listado.buscarPorEmail("PepitoPepon@gmail.com");

        assertTrue(resultado.isPresent());
        assertEquals(u, resultado.get());
    }

    @Test
    public void TestBuscarPorEmailDeberiaLanzarExcepcionCuandoEmailEsNull(){
        assertThrows(IllegalArgumentException.class, () -> listado.buscarPorEmail(null));
    }

    @Test
    public void TestBuscarPorEmailCuandoNoExisteDevuelveEmpty(){
        Optional<Usuario> resultado = listado.buscarPorEmail("Pepito@gmail.com");
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void TestBuscarPorDNICuandoExisteDevuevleAlUsuario(){
        Usuario u = crearUsuario(1, "PepitoPepon@gmail.com", "12345678");
        listado.guardar(u);

        Optional<Usuario> resultado = listado.buscarPorDNI("12345678");

        assertTrue(resultado.isPresent());
        assertEquals(u, resultado.get());
    }

    @Test
    public void TestBuscarPorDNIDeberiaLanzarExcepcionCuandoDNIEsNull(){
        assertThrows(IllegalArgumentException.class, () -> listado.buscarPorDNI(null));
    }

    @Test
    public void TestBuscarPorDNICuandoNoExisteDevuelveEmpty(){
        Optional<Usuario> resultado = listado.buscarPorDNI("11111111");
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void TestListarUsuariosDevuelveListaCompletaSiEsQueHayUsuarios(){
        Usuario u = crearUsuario(1, "PepitoPepon@gmail.com", "12345678");
        Usuario u2 = crearUsuario(2, "PepitoPepon2@gmail.com", "12345679");

        listado.guardar(u);
        listado.guardar(u2);

        List<Usuario> usuarios = listado.listarUsuarios();

        assertEquals(2, usuarios.size());
        assertTrue(usuarios.contains(u));
        assertTrue(usuarios.contains(u2));
    }

    @Test
    public void TestListarUsuariosDevuelveListaVaciaSiEsQueNoHayUsuarios(){
        List<Usuario> usuarios = listado.listarUsuarios();
        assertTrue(usuarios.isEmpty());
    }

    @Test
    public void TestEliminarPorIDCuandoExisteDeberiaEliminarUsuario(){
        Usuario u = crearUsuario(1, "PepitoPepon@gmail.com", "12345678");
        listado.guardar(u);

        listado.eliminarPorID(1);

        assertTrue(listado.buscarPorID(1).isEmpty());
    }

    @Test
    public void TestEliminarPorIDCuandoNoExisteNoDeberiaRomper(){
        assertDoesNotThrow(() -> listado.eliminarPorID(12));
    }

    @Test
    public void TestExistePorEmailSiExisteDevuelveTrue(){
        Usuario u = crearUsuario(1, "PepitoPepon@gmail.com", "12345678");
        listado.guardar(u);

        boolean existe = listado.existePorEmail("PepitoPepon@gmail.com");

        assertTrue(existe);
    }

    @Test
    public void TestExistePorEmailSiNoExisteDevuelveFalse(){
        boolean existe = listado.existePorEmail("noexiste@gmail.com");
        assertFalse(existe);
    }

    @Test
    public void TestExisteEmailRechazaUnNull(){
        assertThrows(IllegalArgumentException.class, () -> listado.existePorEmail(null));
    }

    @Test
    public void TestExistePorDNISiExisteDevuelveTrue(){
        Usuario u = crearUsuario(1, "PepitoPepon@gmail.com", "12345678");
        listado.guardar(u);

        boolean existe = listado.existePorDNI("12345678");

        assertTrue(existe);
    }

    @Test
    public void TestExistePorDNISiNoExisteDevuelveFalse(){
        boolean existe = listado.existePorDNI("11111111");
        assertFalse(existe);
    }

    @Test
    public void TestExisteDNIRechazaUnNull(){
        assertThrows(IllegalArgumentException.class, () -> listado.existePorDNI(null));
    }

    @Test
    public void TestGuardarUsuarioConMismoIDDeberiaReemplazarUsuario(){
        Usuario usuarioOriginal = crearUsuario(1, "lucio@gmail.com", "12345678");
        Usuario usuarioNuevo = crearUsuario(1, "lucio.nuevo@gmail.com", "11111111");

        listado.guardar(usuarioOriginal);
        listado.guardar(usuarioNuevo);

        Optional<Usuario> resultado = listado.buscarPorID(1);

        assertTrue(resultado.isPresent());
        assertEquals(usuarioNuevo, resultado.get());
        assertEquals("lucio.nuevo@gmail.com", resultado.get().getEmail());
        assertEquals("11111111", resultado.get().getDNI());
        assertEquals(1, listado.listarUsuarios().size());
    }

    @Test
    public void TestGuardarUsuarioNoPermiteEmailsDuplicados(){
        Usuario usuarioOriginal = crearUsuario(1, "pepito@gmail.com", "12345678");
        Usuario emailRepetido = crearUsuario(2, "pepito@gmail.com", "11111111");

        listado.guardar(usuarioOriginal);
        assertThrows(IllegalArgumentException.class, () -> listado.guardar(emailRepetido));
    }

    @Test
    public void TestGuardarUsuarioNoPermiteDNIDuplicados(){
        Usuario usuarioOriginal = crearUsuario(1, "pepon@gmail.com", "12345678");
        Usuario DNIRepetido = crearUsuario(2, "pepito@gmail.com", "12345678");

        listado.guardar(usuarioOriginal);
        assertThrows(IllegalArgumentException.class, () -> listado.guardar(DNIRepetido));
    }

    @Test
    void TestGuardarUsuarioconMismoIdYMismoEmailYDnideberiaActualizarSinError() {
        Usuario usuarioOriginal = crearUsuario(1, "Pepito@gmail.com", "12345678");
        Usuario usuarioActualizado = new Usuario(1, "PepitoModificado", "Pepon", "Pepito@gmail.com", "Password123!", "12345678");

        listado.guardar(usuarioOriginal);

        assertDoesNotThrow(() -> listado.guardar(usuarioActualizado));
    }
}
