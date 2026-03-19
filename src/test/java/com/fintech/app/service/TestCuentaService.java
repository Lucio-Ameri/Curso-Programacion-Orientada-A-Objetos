package com.fintech.app.service;

import com.fintech.app.model.Cuenta;
import com.fintech.app.model.Dinero;
import com.fintech.app.model.Usuario;
import com.fintech.app.model.enums.Moneda;
import com.fintech.app.model.enums.TipoCuenta;
import com.fintech.app.repository.RepositorioCuenta;
import com.fintech.app.repository.RepositorioUsuario;
import com.fintech.app.repository.memory.RepositorioCuentaEnMemoria;
import com.fintech.app.repository.memory.RepositorioUsuarioEnMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TestCuentaService {
    private RepositorioCuenta repositorioCuenta;
    private RepositorioUsuario repositorioUsuario;
    private CuentaService servicio;

    @BeforeEach
    public void inicializar(){
        repositorioCuenta = new RepositorioCuentaEnMemoria();
        repositorioUsuario = new RepositorioUsuarioEnMemoria();
        servicio = new CuentaService(repositorioCuenta, repositorioUsuario);
    }

    private Usuario crearUsuario(int ID, String email, String DNI){
        return new Usuario(ID, "Pepito", "Pepon", email, "Password1234!", DNI);
    }

    private Cuenta crearCuenta(int ID, int usuarioID){
        return new Cuenta(ID, usuarioID, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);
    }

    @Test
    public void TestConstructorLanzaExcepcionSiElRepositorioCuentaEsNull(){
        assertThrows(IllegalArgumentException.class, () -> new CuentaService(null, repositorioUsuario));
    }

    @Test
    public void TestConstructorLanzaExcepcionSiElRepositorioUsuarioEsNull(){
        assertThrows(IllegalArgumentException.class, () -> new CuentaService(repositorioCuenta, null));
    }

    @Test
    public void TestRegistrarCuentaCuandoEsValidaSeGuardaCorrectamente(){
        Usuario u = crearUsuario(1, "Pepito@gmail.com", "12345678");
        Cuenta c = crearCuenta(1, 1);

        repositorioUsuario.guardar(u);
        Cuenta guardada = servicio.registrarCuenta(c);

        assertEquals(c, guardada);
    }

    @Test
    public void TestRegistrarCuentaLanzaExcepcionCuandoLaCuentaEsNull(){
        assertThrows(IllegalArgumentException.class, () -> servicio.registrarCuenta(null));
    }

    @Test
    public void TestRegistrarCuentaLanzaExcepcionSiLaCuentaARegistrarTieneElMismoIDDeOtra(){
        Usuario u = crearUsuario(1, "Pepito@gmail.com", "12345678");
        repositorioUsuario.guardar(u);

        Cuenta c = crearCuenta(1, 1);
        Cuenta c2 = crearCuenta(1, 1);

        servicio.registrarCuenta(c);
        assertThrows(IllegalArgumentException.class, () -> servicio.registrarCuenta(c));
    }

    @Test
    public void TestRegistrarCuentaLanzaExcepcionSiElUsuarioNoExiste(){
        Cuenta c = crearCuenta(1, 99);
        assertThrows(IllegalArgumentException.class, () -> servicio.registrarCuenta(c));
    }

    @Test
    public void TestActualizarCuentaCuandoExisteDeberiaReemplazarCorrectamente(){
        Usuario u = crearUsuario(1, "Pepito@gmail.com", "12345678");
        Usuario u2 = crearUsuario(2, "Pepito2@gmail.com", "12345679");
        repositorioUsuario.guardar(u);
        repositorioUsuario.guardar(u2);

        Cuenta c = crearCuenta(1, 1);
        servicio.registrarCuenta(c);

        Cuenta c2 = crearCuenta(1, 2);
        assertDoesNotThrow(() -> servicio.actualizarCuenta(c2));
        assertNotEquals(1, repositorioCuenta.buscarPorID(1).get().getUsuarioID());
        assertEquals(2, repositorioCuenta.buscarPorID(1).get().getUsuarioID());
    }

    @Test
    public void TestActualizarCuentaLanzaExcepcionSiLaMismaEsNull(){
        assertThrows(IllegalArgumentException.class, () -> servicio.actualizarCuenta(null));
    }

    @Test
    public void TestActualizarCuentaLanzaExcepcionCuandoLaMismaNoEstaRegistrada(){
        Usuario u = crearUsuario(1, "Pepito@gmail.com", "12345678");
        repositorioUsuario.guardar(u);

        assertThrows(IllegalArgumentException.class, () -> servicio.actualizarCuenta(crearCuenta(99, 1)));
    }

    @Test
    public void TestActualizarCuentaLanzaExcepcionCuandoElUsuarioNoExiste(){
        assertThrows(IllegalArgumentException.class, () -> servicio.actualizarCuenta(crearCuenta(99, 1)));
    }

    @Test
    public void TestBuscarPorIDDevuelveLaCuentaSiExiste(){
        Usuario u = crearUsuario(1, "Pepito@gmail.com", "12345678");
        Cuenta c = crearCuenta(1, 1);

        repositorioUsuario.guardar(u);
        Cuenta guardada = servicio.registrarCuenta(c);

        Optional<Cuenta> buscada = servicio.buscarPorID(1);
        assertTrue(buscada.isPresent());
        assertEquals(c, buscada.get());
    }

    @Test
    public void TestBuscarPorIDDevuelveEmptySiNoExisteLaCuenta(){
        Optional<Cuenta> buscada = servicio.buscarPorID(99);
        assertTrue(buscada.isEmpty());
    }

    @Test
    public void TestListarCuentasCuandoHayLasDevuelveTodas(){
        Usuario u = crearUsuario(1, "Pepito@gmail.com", "12345678");
        repositorioUsuario.guardar(u);

        Cuenta c = crearCuenta(1, 1);
        Cuenta c2 = crearCuenta(2, 1);
        Cuenta c3 = crearCuenta(3, 1);
        Cuenta c4 = crearCuenta(4, 1);

        servicio.registrarCuenta(c);
        servicio.registrarCuenta(c2);
        servicio.registrarCuenta(c3);
        servicio.registrarCuenta(c4);

        List<Cuenta> listado = servicio.listarCuentas();
        assertFalse(listado.isEmpty());
        assertTrue(listado.size() == 4);
    }

    @Test
    public void TestListarCuentasCuandoNoHayDevuelveUnaListaVacia(){
        List<Cuenta> listado = servicio.listarCuentas();
        assertTrue(listado.isEmpty());
    }

    @Test
    public void TestListarPorUsuarioIDDevuelveTodasSusCuentas(){
        Usuario u = crearUsuario(1, "Pepito@gmail.com", "12345678");
        Usuario u2 = crearUsuario(12, "Pepito2@gmail.com", "12345679");
        repositorioUsuario.guardar(u);
        repositorioUsuario.guardar(u2);

        Cuenta c = crearCuenta(1, 1);
        Cuenta c2 = crearCuenta(2, 12);
        Cuenta c3 = crearCuenta(3, 12);
        Cuenta c4 = crearCuenta(4, 1);

        servicio.registrarCuenta(c);
        servicio.registrarCuenta(c2);
        servicio.registrarCuenta(c3);
        servicio.registrarCuenta(c4);

        List<Cuenta> listado = servicio.listarPorUsuarioID(12);
        assertFalse(listado.isEmpty());
        assertTrue(listado.size() == 2);
        assertEquals(c2, listado.get(0));
        assertEquals(c3, listado.get(1));
    }

    @Test
    public void TestEliminarPorIDCuandoExisteLaElimina(){
        Usuario u = crearUsuario(1, "Pepito@gmail.com", "12345678");
        repositorioUsuario.guardar(u);

        Cuenta c = crearCuenta(1, 1);
        Cuenta c2 = crearCuenta(2, 1);

        servicio.registrarCuenta(c);
        servicio.registrarCuenta(c2);

        servicio.eliminarPorID(2);

        List<Cuenta> listado = servicio.listarCuentas();
        assertFalse(listado.isEmpty());
        assertTrue(listado.size() == 1);
        assertEquals(c, listado.get(0));
    }

    @Test
    public void TestEliminarPorIDNoRompeCuandoNoExisteLaCuenta(){
        Usuario u = crearUsuario(1, "Pepito@gmail.com", "12345678");
        repositorioUsuario.guardar(u);

        Cuenta c = crearCuenta(1, 1);
        Cuenta c2 = crearCuenta(2, 1);

        servicio.registrarCuenta(c);
        servicio.registrarCuenta(c2);

        assertDoesNotThrow(() -> servicio.eliminarPorID(99));
    }

    @Test
    public void TestDepositarEnCuentaActualizaElSaldoSiEsQueExisteLaCuenta(){
        Usuario u = crearUsuario(1, "Pepito@gmail.com", "12345678");
        repositorioUsuario.guardar(u);

        Cuenta c = crearCuenta(1, 1);
        Cuenta c2 = crearCuenta(2, 1);

        servicio.registrarCuenta(c);
        servicio.registrarCuenta(c2);

        assertEquals(0.0, c.getMonto().getMonto());

        Cuenta actualizada = servicio.depositarDinero(1, new Dinero(400.50, Moneda.ARS));
        assertEquals(400.50, actualizada.getMonto().getMonto());
        assertEquals(Moneda.ARS, actualizada.getMonto().getMoneda());
    }

    @Test
    public void TestDepositarEnCuentaLanzaExcepcionSiElDineroEsNull(){
        Usuario u = crearUsuario(1, "Pepito@gmail.com", "12345678");
        repositorioUsuario.guardar(u);

        Cuenta c = crearCuenta(1, 1);
        servicio.registrarCuenta(c);

        assertThrows(IllegalArgumentException.class, () -> servicio.depositarDinero(1, null));
    }

    @Test
    public void TestDepositarEnCuentaLanzaExcepcionSiLaCuentaNoExiste(){
        assertThrows(IllegalArgumentException.class, () -> servicio.depositarDinero(99, new Dinero(400.50, Moneda.ARS)));
    }

    @Test
    public void TestRetirarEnCuentaActualizaElSaldoCuandoEsSuficiente(){
        Usuario u = crearUsuario(1, "Pepito@gmail.com", "12345678");
        repositorioUsuario.guardar(u);

        Cuenta c = crearCuenta(1, 1);
        servicio.registrarCuenta(c);

        Cuenta ingresado = servicio.depositarDinero(1, new Dinero(400.50, Moneda.ARS));
        assertEquals(400.50, ingresado.getMonto().getMonto());

        Cuenta retirado = servicio.retirarDinero(1, new Dinero(200.00, Moneda.ARS));
        assertEquals(200.50, retirado.getMonto().getMonto());
        assertEquals(Moneda.ARS, retirado.getMonto().getMoneda());
    }

    @Test
    public void TestRetirarEnCuentaLanzaExcepcionCuandoDineroEsNull(){
        Usuario u = crearUsuario(1, "Pepito@gmail.com", "12345678");
        repositorioUsuario.guardar(u);

        Cuenta c = crearCuenta(1, 1);
        servicio.registrarCuenta(c);

        assertThrows(IllegalArgumentException.class, () -> servicio.retirarDinero(1, null));
    }

    @Test
    public void TestRetirarEnCuentaLanzaExcepcionCuandoLaMismaNoExiste(){
        Usuario u = crearUsuario(1, "Pepito@gmail.com", "12345678");
        repositorioUsuario.guardar(u);

        Cuenta c = crearCuenta(1, 1);
        servicio.registrarCuenta(c);
        servicio.depositarDinero(1, new Dinero(400.50, Moneda.ARS));

        assertThrows(IllegalArgumentException.class, () -> servicio.retirarDinero(99, new Dinero(300.00, Moneda.ARS)));
    }

    @Test
    public void TestRetirarEnCuentaLanzaExcepcionCuandoLaMismaTieneUnSaldoInsuficiente(){
        Usuario usuario = crearUsuario(1, "pepito@gmail.com", "12345678");
        repositorioUsuario.guardar(usuario);

        Cuenta cuenta = crearCuenta(1, 1);
        servicio.registrarCuenta(cuenta);
        servicio.depositarDinero(1, new Dinero(100.0, Moneda.ARS));

        assertThrows(IllegalArgumentException.class, () -> servicio.retirarDinero(1, new Dinero(200.0, Moneda.ARS)));
    }
}
