package com.fintech.app.repository;

import com.fintech.app.model.Dinero;
import com.fintech.app.model.Movimiento;
import com.fintech.app.model.enums.Moneda;
import com.fintech.app.model.enums.TipoMovimiento;
import com.fintech.app.repository.memory.RepositorioMovimientoEnMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TestRepositorioMovimiento {
    private RepositorioMovimientoEnMemoria listado;

    @BeforeEach
    public void inicializar(){
        listado = new RepositorioMovimientoEnMemoria();
    }

    private Movimiento crearMovimiento(int ID, TipoMovimiento tipo){
        return new Movimiento(ID, tipo, new Dinero(300.00, Moneda.ARS), LocalDateTime.now(), 1);
    }

    @Test
    public void TestGuardarMovimientoDeberiaDevolverElMismoMovimiento(){
        Movimiento m = crearMovimiento(1, TipoMovimiento.DEPOSITO);

        Movimiento guardado = listado.guardar(m);

        assertEquals(m, guardado);
    }

    @Test
    public void TestGuardarMovimientoLanzaExcepcionCuandoElMismoEsNull(){
        assertThrows(IllegalArgumentException.class, () -> listado.guardar(null));
    }

    @Test
    public void TestBuscarPorIDDevuelveAlMovimientoSiExiste(){
        Movimiento m = crearMovimiento(1, TipoMovimiento.DEPOSITO);
        Movimiento m2 = crearMovimiento(12, TipoMovimiento.DEPOSITO);

        listado.guardar(m);
        listado.guardar(m2);

        Optional<Movimiento> buscado = listado.buscarPorID(1);
        assertTrue(buscado.isPresent());
        assertEquals(m, buscado.get());
    }

    @Test
    public void TestBuscarPorIDDevuelveEmptySiNoExisteElMovimiento(){
        Movimiento m = crearMovimiento(1, TipoMovimiento.DEPOSITO);
        Movimiento m2 = crearMovimiento(12, TipoMovimiento.DEPOSITO);

        listado.guardar(m);
        listado.guardar(m2);

        Optional<Movimiento> buscado = listado.buscarPorID(99);
        assertTrue(buscado.isEmpty());
    }

    @Test
    public void TestListarTodosDevuelveUnaListaCompletaSiEsQueHayMovimientos(){
        Movimiento m = crearMovimiento(1, TipoMovimiento.DEPOSITO);
        Movimiento m2 = crearMovimiento(2, TipoMovimiento.DEPOSITO);
        Movimiento m3 = crearMovimiento(3, TipoMovimiento.DEPOSITO);
        Movimiento m4 = crearMovimiento(4, TipoMovimiento.DEPOSITO);
        Movimiento m5 = crearMovimiento(5, TipoMovimiento.DEPOSITO);
        Movimiento m6 = crearMovimiento(6, TipoMovimiento.DEPOSITO);

        listado.guardar(m);
        listado.guardar(m2);
        listado.guardar(m3);
        listado.guardar(m4);
        listado.guardar(m5);
        listado.guardar(m6);

        List<Movimiento> obtenidos = listado.listarTodos();
        assertFalse(obtenidos.isEmpty());
        assertEquals(6, obtenidos.size());
        assertTrue(obtenidos.contains(m));
        assertTrue(obtenidos.contains(m2));
        assertTrue(obtenidos.contains(m3));
        assertTrue(obtenidos.contains(m4));
        assertTrue(obtenidos.contains(m5));
        assertTrue(obtenidos.contains(m6));
    }

    @Test
    public void TestListarTodosDevuelveUnaListaVaciaCuandoNoHayMovimientos(){
        List<Movimiento> obtenidos = listado.listarTodos();
        assertTrue(obtenidos.isEmpty());
    }

    @Test
    public void TestListarPorCuentaIDDevuelveElListadoConTodosSusMovimientosSiTIene(){
        Movimiento m = crearMovimiento(1, TipoMovimiento.DEPOSITO);
        Movimiento m2 = crearMovimiento(2, TipoMovimiento.DEPOSITO);
        Movimiento m3 = crearMovimiento(3, TipoMovimiento.DEPOSITO);
        Movimiento m4 = new Movimiento(5, TipoMovimiento.RETIRO, new Dinero(100.00, Moneda.ARS), LocalDateTime.now(), 33);

        listado.guardar(m);
        listado.guardar(m2);
        listado.guardar(m3);
        listado.guardar(m4);

        List<Movimiento> obtenidos = listado.listarPorCuentaID(1);
        assertFalse(obtenidos.isEmpty());
        assertEquals(3, obtenidos.size());
        assertTrue(obtenidos.contains(m));
        assertTrue(obtenidos.contains(m2));
        assertTrue(obtenidos.contains(m3));
        assertFalse(obtenidos.contains(m4));
    }

    @Test
    public void TestListarPorCuentaIDDevuelveUnaListaVaciaSiNoHayMovimientoDeEsaCuenta(){
        Movimiento m = crearMovimiento(1, TipoMovimiento.DEPOSITO);
        Movimiento m2 = crearMovimiento(2, TipoMovimiento.DEPOSITO);
        Movimiento m3 = crearMovimiento(3, TipoMovimiento.DEPOSITO);

        listado.guardar(m);
        listado.guardar(m2);
        listado.guardar(m3);

        List<Movimiento> obtenidos = listado.listarPorCuentaID(99);
        assertTrue(obtenidos.isEmpty());
    }

    @Test
    public void TestListarPorCuentaIDDevuelveSoloLosMovimientosDeLaCuentaBuscadaCuandoHayMuchasCuentas(){
        Movimiento m = new Movimiento(1, TipoMovimiento.RETIRO, new Dinero(100.00, Moneda.ARS), LocalDateTime.now(), 1);
        Movimiento m2 = new Movimiento(2, TipoMovimiento.RETIRO, new Dinero(100.00, Moneda.ARS), LocalDateTime.now(), 2);
        Movimiento m3 = new Movimiento(3, TipoMovimiento.RETIRO, new Dinero(100.00, Moneda.ARS), LocalDateTime.now(), 3);
        Movimiento m4 = new Movimiento(4, TipoMovimiento.RETIRO, new Dinero(100.00, Moneda.ARS), LocalDateTime.now(), 2);
        Movimiento m5 = new Movimiento(5, TipoMovimiento.RETIRO, new Dinero(100.00, Moneda.ARS), LocalDateTime.now(), 4);
        Movimiento m6 = new Movimiento(6, TipoMovimiento.RETIRO, new Dinero(100.00, Moneda.ARS), LocalDateTime.now(), 5);
        Movimiento m7 = new Movimiento(7, TipoMovimiento.RETIRO, new Dinero(100.00, Moneda.ARS), LocalDateTime.now(), 2);
        Movimiento m8 = new Movimiento(8, TipoMovimiento.RETIRO, new Dinero(100.00, Moneda.ARS), LocalDateTime.now(), 2);

        listado.guardar(m);
        listado.guardar(m2);
        listado.guardar(m3);
        listado.guardar(m4);
        listado.guardar(m5);
        listado.guardar(m6);
        listado.guardar(m7);
        listado.guardar(m8);

        List<Movimiento> obtenidos = listado.listarPorCuentaID(2);
        assertFalse(obtenidos.isEmpty());
        assertEquals(4, obtenidos.size());
        assertTrue(obtenidos.contains(m2));
        assertTrue(obtenidos.contains(m4));
        assertTrue(obtenidos.contains(m7));
        assertTrue(obtenidos.contains(m8));
    }

    @Test
    public void TestGuardarMovimientoLanzaExcepcionCuandoSeIntentaConUnMismoID(){
        Movimiento m = crearMovimiento(1, TipoMovimiento.DEPOSITO);
        Movimiento m2 = crearMovimiento(1, TipoMovimiento.DEPOSITO);

        listado.guardar(m);
        assertThrows(IllegalArgumentException.class, () -> listado.guardar(m2));
    }
}
