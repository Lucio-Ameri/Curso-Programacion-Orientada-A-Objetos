package com.fintech.app.service;

import com.fintech.app.model.Cuenta;
import com.fintech.app.model.Dinero;
import com.fintech.app.model.Movimiento;
import com.fintech.app.model.enums.Moneda;
import com.fintech.app.model.enums.TipoCuenta;
import com.fintech.app.model.enums.TipoMovimiento;
import com.fintech.app.repository.RepositorioCuenta;
import com.fintech.app.repository.RepositorioMovimiento;
import com.fintech.app.repository.memory.RepositorioCuentaEnMemoria;
import com.fintech.app.repository.memory.RepositorioMovimientoEnMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TestMovimientoService {
    private RepositorioMovimiento movimientos;
    private RepositorioCuenta cuentas;
    private MovimientoService servicio;

    @BeforeEach
    public void inicializar(){
        movimientos = new RepositorioMovimientoEnMemoria();
        cuentas = new RepositorioCuentaEnMemoria();
        servicio = new MovimientoService(movimientos, cuentas);
    }

    private Cuenta crearCuenta(int cuentaID, int usuarioID){
        return new Cuenta(cuentaID, usuarioID, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);
    }

    private Movimiento crearMovimiento(int movimientoID, int cuentaID, TipoMovimiento tipo, Dinero dinero){
        return new Movimiento(movimientoID, tipo, dinero, LocalDateTime.now(), cuentaID);
    }

    @Test
    public void TestConstructorLanzaExcepcionSiRepositorioMovimientoEsNull(){
        assertThrows(IllegalArgumentException.class, () -> new MovimientoService(null, new RepositorioCuentaEnMemoria()));
    }

    @Test
    public void TestConstructorLanzaExcepcionSiRepositorioCuentaEsNull(){
        assertThrows(IllegalArgumentException.class, () -> new MovimientoService(new RepositorioMovimientoEnMemoria(), null));
    }

    @Test
    public void TestRegistrarMovimientoSeGuardaYLoDevuelveCorrectamenteSiEsValido(){
        Cuenta c = crearCuenta(1, 1);
        cuentas.guardar(c);

        Movimiento m = crearMovimiento(1, 1, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        Movimiento guardado = servicio.registrarMovimiento(m);

        assertEquals(m, guardado);
    }

    @Test
    public void TestRegistrarMovimientoLanzaExcepcionCuandoElMismoEsNull(){
        assertThrows(IllegalArgumentException.class, () -> servicio.registrarMovimiento(null));
    }

    @Test
    public void TestRegistrarMovimientoLanzaExcepxionSiLaCuentaNoExiste(){
        Movimiento m = crearMovimiento(1, 99, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        assertThrows(IllegalArgumentException.class, () -> servicio.registrarMovimiento(m));
    }

    @Test
    public void TestRegistrarMovimientosLanzaExcepcionCuandoSeIntentaConElIDDeOtroMovimiento(){
        Cuenta c = crearCuenta(1, 1);
        cuentas.guardar(c);

        Movimiento m = crearMovimiento(1, 1, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        Movimiento m2 = crearMovimiento(1, 2, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        servicio.registrarMovimiento(m);

        assertThrows(IllegalArgumentException.class, () -> servicio.registrarMovimiento(m2));
    }

    @Test
    public void TestBuscarPorIDDevuelveElMovimientoSiEsQueExiste(){
        Cuenta c = crearCuenta(1, 1);
        Cuenta c2 = crearCuenta(2, 1);
        Cuenta c3 = crearCuenta(3, 1);
        Cuenta c4 = crearCuenta(4, 1);
        cuentas.guardar(c);
        cuentas.guardar(c2);
        cuentas.guardar(c3);
        cuentas.guardar(c4);

        Movimiento m = crearMovimiento(1, 1, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        Movimiento m2 = crearMovimiento(2, 2, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        Movimiento m3 = crearMovimiento(3, 3, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        Movimiento m4 = crearMovimiento(4, 4, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));

        servicio.registrarMovimiento(m);
        servicio.registrarMovimiento(m2);
        servicio.registrarMovimiento(m3);
        servicio.registrarMovimiento(m4);

        Optional<Movimiento> obtenido = servicio.buscarPorID(2);
        assertTrue(obtenido.isPresent());
        assertEquals(m2, obtenido.get());
    }

    @Test
    public void TestBuscarPorIDDevuelveEmptySiEsQueNoExisteElMovimiento(){
        Cuenta c = crearCuenta(1, 1);
        cuentas.guardar(c);

        Movimiento m = crearMovimiento(1, 1, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        Movimiento m2 = crearMovimiento(2, 1, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        Movimiento m3 = crearMovimiento(3, 1, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        Movimiento m4 = crearMovimiento(4, 1, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));

        servicio.registrarMovimiento(m);
        servicio.registrarMovimiento(m2);
        servicio.registrarMovimiento(m3);
        servicio.registrarMovimiento(m4);

        Optional<Movimiento> obtenido = servicio.buscarPorID(99);
        assertTrue(obtenido.isEmpty());
    }

    @Test
    public void TestListarPorCuentaIDDevuelveUnaListaCompletaDeLosMovimientosSiTiene(){
        Cuenta c = crearCuenta(1, 1);
        Cuenta c2 = crearCuenta(2, 1);
        cuentas.guardar(c);
        cuentas.guardar(c2);

        Movimiento m = crearMovimiento(1, 1, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        Movimiento m2 = crearMovimiento(2, 1, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        Movimiento m3 = crearMovimiento(3, 1, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        Movimiento m4 = crearMovimiento(4, 1, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        Movimiento m5 = crearMovimiento(5, 2, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        Movimiento m6 = crearMovimiento(6, 2, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));

        servicio.registrarMovimiento(m);
        servicio.registrarMovimiento(m2);
        servicio.registrarMovimiento(m3);
        servicio.registrarMovimiento(m4);
        servicio.registrarMovimiento(m5);
        servicio.registrarMovimiento(m6);

        List<Movimiento> listado = servicio.listarPorCuentaID(1);
        assertEquals(4, listado.size());
        assertTrue(listado.contains(m));
        assertTrue(listado.contains(m2));
        assertTrue(listado.contains(m3));
        assertTrue(listado.contains(m4));
        assertFalse(listado.contains(m5));
        assertFalse(listado.contains(m6));
    }

    @Test
    public void TestListarPorCuentaIDDevuelveUnaListaVaciaSiEsQueNoHayMovimientosDeEsaCuenta(){
        Cuenta c = crearCuenta(1, 1);
        Cuenta c2 = crearCuenta(2, 1);
        cuentas.guardar(c);
        cuentas.guardar(c2);

        Movimiento m = crearMovimiento(1, 1, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        Movimiento m2 = crearMovimiento(2, 1, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        Movimiento m3 = crearMovimiento(3, 1, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        servicio.registrarMovimiento(m);
        servicio.registrarMovimiento(m2);
        servicio.registrarMovimiento(m3);

        List<Movimiento> listado = servicio.listarPorCuentaID(2);
        assertTrue(listado.isEmpty());
    }

    @Test
    public void TestListarPorUsuarioIDDevuelveUnaListaCompletaDeLosMovimientosSiTiene(){
        Cuenta c = crearCuenta(1, 1);
        Cuenta c2 = crearCuenta(2, 2);
        Cuenta c3 = crearCuenta(3, 2);
        cuentas.guardar(c);
        cuentas.guardar(c2);
        cuentas.guardar(c3);

        Movimiento m = crearMovimiento(1, 1, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        Movimiento m2 = crearMovimiento(2, 1, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        Movimiento m3 = crearMovimiento(3, 3, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        Movimiento m4 = crearMovimiento(4, 2, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        Movimiento m5 = crearMovimiento(5, 2, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        Movimiento m6 = crearMovimiento(6, 3, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));

        servicio.registrarMovimiento(m);
        servicio.registrarMovimiento(m2);
        servicio.registrarMovimiento(m3);
        servicio.registrarMovimiento(m4);
        servicio.registrarMovimiento(m5);
        servicio.registrarMovimiento(m6);

        List<Movimiento> listado = servicio.listarPorUsuarioID(2);
        assertEquals(4, listado.size());
        assertTrue(listado.contains(m3));
        assertTrue(listado.contains(m4));
        assertTrue(listado.contains(m5));
        assertTrue(listado.contains(m6));
        assertFalse(listado.contains(m));
        assertFalse(listado.contains(m2));
    }

    @Test
    public void TestListarPorUsuarioIDDevuelveUnaListaVaciaSiEsQueNoHayMovimientosDeEseUsuario(){
        Cuenta c = crearCuenta(1, 1);
        Cuenta c2 = crearCuenta(2, 2);
        Cuenta c3 = crearCuenta(3, 2);
        cuentas.guardar(c);
        cuentas.guardar(c2);
        cuentas.guardar(c3);

        Movimiento m = crearMovimiento(1, 2, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        Movimiento m2 = crearMovimiento(2, 2, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        Movimiento m3 = crearMovimiento(3, 3, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        Movimiento m4 = crearMovimiento(4, 2, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        Movimiento m5 = crearMovimiento(5, 2, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));
        Movimiento m6 = crearMovimiento(6, 3, TipoMovimiento.DEPOSITO, new Dinero(300.00, Moneda.ARS));

        servicio.registrarMovimiento(m);
        servicio.registrarMovimiento(m2);
        servicio.registrarMovimiento(m3);
        servicio.registrarMovimiento(m4);
        servicio.registrarMovimiento(m5);
        servicio.registrarMovimiento(m6);

        List<Movimiento> listado = servicio.listarPorUsuarioID(1);
        assertTrue(listado.isEmpty());
    }
}
