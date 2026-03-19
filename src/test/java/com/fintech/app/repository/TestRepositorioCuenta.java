package com.fintech.app.repository;

import com.fintech.app.model.Cuenta;
import com.fintech.app.model.enums.TipoCuenta;
import com.fintech.app.repository.memory.RepositorioCuentaEnMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TestRepositorioCuenta {
    private RepositorioCuentaEnMemoria listado;

    @BeforeEach
    public void inicializar(){
        listado = new RepositorioCuentaEnMemoria();
    }

    @Test
    public void TestGuardarCuentaDeberiaDevolverLaMismaCuentaYGuardarla(){
        Cuenta c = new Cuenta(1, 1, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);

        Cuenta guardada = listado.guardar(c);

        assertEquals(c, guardada);
    }

    @Test
    public void TestGuardarCuentaNoDeberiaLanzarExcepcionSiSeIntentaGuardarUnaCuentaConElMismoID(){
        Cuenta c = new Cuenta(1, 1, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);
        Cuenta c2 = new Cuenta(1, 2, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);

        listado.guardar(c);
        assertDoesNotThrow(() -> listado.guardar(c2));
        assertEquals(2, listado.buscarPorID(1).get().getUsuarioID());
    }

    @Test
    public void TestGuardarCuentaDeberiaLanzarExcepcionCuandoEstaEsNull(){
        assertThrows(IllegalArgumentException.class, () -> listado.guardar(null));
    }

    @Test
    public void TestBuscarPorIDCuandoExisteDevuelveLaCuentaBuscada(){
        Cuenta c = new Cuenta(1, 1, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);

        listado.guardar(c);

        Optional<Cuenta> buscada = listado.buscarPorID(1);

        assertTrue(buscada.isPresent());
        assertEquals(c, buscada.get());
    }

    @Test
    public void TestBuscarPorIDSiNoExisteDeberiaDevolverEmpty(){
        Optional<Cuenta> buscada = listado.buscarPorID(99);
        assertTrue(buscada.isEmpty());
    }

    @Test
    public void TestListarCuentasDebeDevolverTodasLasCuentasSiEsQueHay(){
        Cuenta c = new Cuenta(1, 1, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);
        Cuenta c2 = new Cuenta(2, 1, TipoCuenta.CAJA_DE_AHORRO_EN_EUROS);
        Cuenta c3 = new Cuenta(3, 1, TipoCuenta.CAJA_DE_AHORRO_EN_DOLARES);
        Cuenta c4 = new Cuenta(4, 1, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);

        listado.guardar(c);
        listado.guardar(c2);
        listado.guardar(c3);
        listado.guardar(c4);

        assertEquals(4, listado.listarCuentas().size());
    }

    @Test
    public void TestListarCuentasDebeDevolverUnaListaVaciaSiEsQueNoHay(){
        assertTrue(listado.listarCuentas().isEmpty());
    }

    @Test
    public void TestListarPorUsuarioIDDebeDevolverTodasLasCuentasDelUsuarioSiTienen(){
        Cuenta c = new Cuenta(1, 1, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);
        Cuenta c2 = new Cuenta(2, 1, TipoCuenta.CAJA_DE_AHORRO_EN_EUROS);
        Cuenta c3 = new Cuenta(3, 1, TipoCuenta.CAJA_DE_AHORRO_EN_DOLARES);
        Cuenta c4 = new Cuenta(4, 1, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);

        listado.guardar(c);
        listado.guardar(c2);
        listado.guardar(c3);
        listado.guardar(c4);

        assertEquals(4, listado.listarPorUsuarioID(1).size());
    }

    @Test
    public void TestListarPorUsuarioIDDebeDevolverUnaListaVaciaSiNoTieneCuentas(){
        Cuenta c = new Cuenta(1, 1, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);
        listado.guardar(c);

        assertTrue(listado.listarPorUsuarioID(99).isEmpty());
    }

    @Test
    public void ListarPorUsuarioIDCuandohayCuentasDeVariosUsuariosFiltraCorrectamente(){
        Cuenta c = new Cuenta(1, 1, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);
        Cuenta c2 = new Cuenta(2, 2, TipoCuenta.CAJA_DE_AHORRO_EN_EUROS);
        Cuenta c3 = new Cuenta(3, 2, TipoCuenta.CAJA_DE_AHORRO_EN_DOLARES);
        Cuenta c4 = new Cuenta(4, 1, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);

        listado.guardar(c);
        listado.guardar(c2);
        listado.guardar(c3);
        listado.guardar(c4);

        List<Cuenta> cuentas = listado.listarPorUsuarioID(1);
        assertEquals(2, cuentas.size());
        assertEquals(c, cuentas.get(0));
        assertEquals(c4, cuentas.get(1));
    }

    @Test
    public void TestEliminarPorIDDebeEliminarLaCuentaSiExiste(){
        Cuenta c = new Cuenta(1, 1, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);
        listado.guardar(c);

        listado.eliminarPorID(1);
        assertTrue(listado.listarCuentas().isEmpty());
    }

    @Test
    public void TestEliminarPorIDNoLanzaExcepcionSiLaCuentaNoExiste(){
        Cuenta c = new Cuenta(1, 1, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);
        listado.guardar(c);

        assertDoesNotThrow(() -> listado.eliminarPorID(99));
    }
}
