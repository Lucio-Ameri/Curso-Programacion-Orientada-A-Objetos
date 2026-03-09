package java.com.fintech.app.model;

import main.java.com.fintech.app.model.Cuenta;
import main.java.com.fintech.app.model.Dinero;
import main.java.com.fintech.app.model.enums.Moneda;
import main.java.com.fintech.app.model.enums.TipoCuenta;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class TestCuenta {

    @Test
    public void TestCrearCuentaConDatosValidos(){
        Cuenta c = new Cuenta(0, 200, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);
        Cuenta c2 = new Cuenta(1, 200, TipoCuenta.CAJA_DE_AHORRO_EN_DOLARES);
        Cuenta c3 = new Cuenta(2, 200, TipoCuenta.CAJA_DE_AHORRO_EN_EUROS);

        assertEquals(0, c.getID());
        assertEquals(200, c.getUsuarioID());
        assertEquals(TipoCuenta.CAJA_DE_AHORRO_EN_PESOS, c.getTipoCuenta());

        assertEquals(1, c2.getID());
        assertEquals(200, c2.getUsuarioID());
        assertEquals(TipoCuenta.CAJA_DE_AHORRO_EN_DOLARES, c2.getTipoCuenta());

        assertEquals(2, c3.getID());
        assertEquals(200, c3.getUsuarioID());
        assertEquals(TipoCuenta.CAJA_DE_AHORRO_EN_EUROS, c3.getTipoCuenta());
    }

    @Test
    public void TestConstructorRechazaIDInvalido(){
        assertThrows(IllegalArgumentException.class, () -> new Cuenta(-1, 0, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS));
        assertThrows(IllegalArgumentException.class, () -> new Cuenta(0, -1, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS));
    }

    @Test
    public void TestConstructorRechazaTipoCuentaNull(){
        assertThrows(IllegalArgumentException.class, () -> new Cuenta(0, 0, null));
    }

    @Test
    public void TestDepositarDineroMismaMoneda(){
        Cuenta c = new Cuenta(0, 200, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);
        Cuenta c2 = new Cuenta(1, 200, TipoCuenta.CAJA_DE_AHORRO_EN_DOLARES);
        Cuenta c3 = new Cuenta(2, 200, TipoCuenta.CAJA_DE_AHORRO_EN_EUROS);

        Dinero d = new Dinero(100, Moneda.ARS);
        Dinero d2 = new Dinero(100, Moneda.USD);
        Dinero d3 = new Dinero(100, Moneda.EUR);

        c.depositar(d);
        c2.depositar(d2);
        c3.depositar(d3);

        assertEquals(100.00, c.getMonto().getMonto());
        assertEquals(100.00, c2.getMonto().getMonto());
        assertEquals(100.00, c3.getMonto().getMonto());
    }

    @Test
    public void TestDepositarDineroRechazaNull(){
        Cuenta c = new Cuenta(0, 200, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);
        Cuenta c2 = new Cuenta(1, 200, TipoCuenta.CAJA_DE_AHORRO_EN_DOLARES);
        Cuenta c3 = new Cuenta(2, 200, TipoCuenta.CAJA_DE_AHORRO_EN_EUROS);

        assertThrows(IllegalArgumentException.class, () -> c.depositar(null));
        assertThrows(IllegalArgumentException.class, () -> c2.depositar(null));
        assertThrows(IllegalArgumentException.class, () -> c3.depositar(null));
    }

    @Test
    public void TestDepositarDineroRechazaDiferenteTipoDeDineroAlDeLaCuenta(){
        Cuenta c = new Cuenta(0, 200, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);
        Cuenta c2 = new Cuenta(1, 200, TipoCuenta.CAJA_DE_AHORRO_EN_DOLARES);
        Cuenta c3 = new Cuenta(2, 200, TipoCuenta.CAJA_DE_AHORRO_EN_EUROS);

        assertThrows(IllegalArgumentException.class, () -> c.depositar(new Dinero(100, Moneda.EUR)));
        assertThrows(IllegalArgumentException.class, () -> c.depositar(new Dinero(100, Moneda.USD)));

        assertThrows(IllegalArgumentException.class, () -> c2.depositar(new Dinero(100, Moneda.ARS)));
        assertThrows(IllegalArgumentException.class, () -> c2.depositar(new Dinero(100, Moneda.EUR)));

        assertThrows(IllegalArgumentException.class, () -> c3.depositar(new Dinero(100, Moneda.ARS)));
        assertThrows(IllegalArgumentException.class, () -> c3.depositar(new Dinero(100, Moneda.USD)));
    }

    @Test
    public void TestDepositarDineroRechazaCantidadNegativaOCero(){
        Cuenta c = new Cuenta(0, 200, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);
        Cuenta c2 = new Cuenta(1, 200, TipoCuenta.CAJA_DE_AHORRO_EN_DOLARES);
        Cuenta c3 = new Cuenta(2, 200, TipoCuenta.CAJA_DE_AHORRO_EN_EUROS);

        assertThrows(IllegalArgumentException.class, () -> c.depositar(new Dinero(-100, Moneda.ARS)));
        assertThrows(IllegalArgumentException.class, () -> c2.depositar(new Dinero(-100, Moneda.USD)));
        assertThrows(IllegalArgumentException.class, () -> c3.depositar(new Dinero(-100, Moneda.EUR)));

        assertThrows(IllegalArgumentException.class, () -> c.depositar(new Dinero(0, Moneda.ARS)));
        assertThrows(IllegalArgumentException.class, () -> c2.depositar(new Dinero(0, Moneda.USD)));
        assertThrows(IllegalArgumentException.class, () -> c3.depositar(new Dinero(0, Moneda.EUR)));
    }

    @Test
    public void TestRetirarDineroMismaMoneda(){
        Cuenta c = new Cuenta(0, 200, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);
        Cuenta c2 = new Cuenta(1, 200, TipoCuenta.CAJA_DE_AHORRO_EN_DOLARES);
        Cuenta c3 = new Cuenta(2, 200, TipoCuenta.CAJA_DE_AHORRO_EN_EUROS);

        Dinero d = new Dinero(100, Moneda.ARS);
        Dinero d2 = new Dinero(100, Moneda.USD);
        Dinero d3 = new Dinero(100, Moneda.EUR);

        c.depositar(d);
        c2.depositar(d2);
        c3.depositar(d3);

        Dinero d4 = new Dinero(50, Moneda.ARS);
        Dinero d5 = new Dinero(50, Moneda.USD);
        Dinero d6 = new Dinero(50, Moneda.EUR);

        c.retirar(d4);
        c2.retirar(d5);
        c3.retirar(d6);

        assertEquals(50.00, c.getMonto().getMonto());
        assertEquals(50.00, c2.getMonto().getMonto());
        assertEquals(50.00, c3.getMonto().getMonto());
    }

    @Test
    public void TestRetirarDineroRechazaNull(){
        Cuenta c = new Cuenta(0, 200, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);
        Cuenta c2 = new Cuenta(1, 200, TipoCuenta.CAJA_DE_AHORRO_EN_DOLARES);
        Cuenta c3 = new Cuenta(2, 200, TipoCuenta.CAJA_DE_AHORRO_EN_EUROS);

        assertThrows(IllegalArgumentException.class, () -> c.retirar(null));
        assertThrows(IllegalArgumentException.class, () -> c2.retirar(null));
        assertThrows(IllegalArgumentException.class, () -> c3.retirar(null));
    }

    @Test
    public void TestRestarDineroRechazaDiferenteTipoDeDineroAlDeLaCuenta(){
        Cuenta c = new Cuenta(0, 200, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);
        Cuenta c2 = new Cuenta(1, 200, TipoCuenta.CAJA_DE_AHORRO_EN_DOLARES);
        Cuenta c3 = new Cuenta(2, 200, TipoCuenta.CAJA_DE_AHORRO_EN_EUROS);

        assertThrows(IllegalArgumentException.class, () -> c.retirar(new Dinero(100, Moneda.EUR)));
        assertThrows(IllegalArgumentException.class, () -> c.retirar(new Dinero(100, Moneda.USD)));

        assertThrows(IllegalArgumentException.class, () -> c2.retirar(new Dinero(100, Moneda.ARS)));
        assertThrows(IllegalArgumentException.class, () -> c2.retirar(new Dinero(100, Moneda.EUR)));

        assertThrows(IllegalArgumentException.class, () -> c3.retirar(new Dinero(100, Moneda.ARS)));
        assertThrows(IllegalArgumentException.class, () -> c3.retirar(new Dinero(100, Moneda.USD)));
    }

    @Test
    public void TestRestarDineroRechazaCantidadNegativaOCero(){
        Cuenta c = new Cuenta(0, 200, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);
        Cuenta c2 = new Cuenta(1, 200, TipoCuenta.CAJA_DE_AHORRO_EN_DOLARES);
        Cuenta c3 = new Cuenta(2, 200, TipoCuenta.CAJA_DE_AHORRO_EN_EUROS);

        assertThrows(IllegalArgumentException.class, () -> c.retirar(new Dinero(-100, Moneda.ARS)));
        assertThrows(IllegalArgumentException.class, () -> c2.retirar(new Dinero(-100, Moneda.USD)));
        assertThrows(IllegalArgumentException.class, () -> c3.retirar(new Dinero(-100, Moneda.EUR)));

        assertThrows(IllegalArgumentException.class, () -> c.retirar(new Dinero(0, Moneda.ARS)));
        assertThrows(IllegalArgumentException.class, () -> c2.retirar(new Dinero(0, Moneda.USD)));
        assertThrows(IllegalArgumentException.class, () -> c3.retirar(new Dinero(0, Moneda.EUR)));
    }

    @Test
    public void TestRestarDineroRechazaCantidadMayorAlDelSaldo(){
        Cuenta c = new Cuenta(0, 200, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);
        Cuenta c2 = new Cuenta(1, 200, TipoCuenta.CAJA_DE_AHORRO_EN_DOLARES);
        Cuenta c3 = new Cuenta(2, 200, TipoCuenta.CAJA_DE_AHORRO_EN_EUROS);

        assertThrows(IllegalArgumentException.class, () -> c.retirar(new Dinero(300, Moneda.ARS)));
        assertThrows(IllegalArgumentException.class, () -> c2.retirar(new Dinero(300, Moneda.USD)));
        assertThrows(IllegalArgumentException.class, () -> c3.retirar(new Dinero(300, Moneda.EUR)));
    }

    @Test
    void TestEqualsHashCodePorId() {
        Cuenta a = new Cuenta(1, 10, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);
        Cuenta b = new Cuenta(1, 999, TipoCuenta.CAJA_DE_AHORRO_EN_DOLARES);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void TestHashSetNoAdmiteDuplicadosPorId() {
        Cuenta a = new Cuenta(1, 10, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);
        Cuenta b = new Cuenta(1, 20, TipoCuenta.CAJA_DE_AHORRO_EN_EUROS);

        HashSet<Cuenta> set = new HashSet<>();
        assertTrue(set.add(a));
        assertFalse(set.add(b));
        assertEquals(1, set.size());
    }

    @Test
    void TestToStringNoRevientaYContieneCamposBasicos() {
        Cuenta c = new Cuenta(7, 10, TipoCuenta.CAJA_DE_AHORRO_EN_PESOS);

        String s = assertDoesNotThrow(c::toString);
        assertTrue(s.contains("id=7"));
        assertTrue(s.contains("usuarioId=10"));
        assertTrue(s.contains("tipoCuenta="));
        assertTrue(s.contains("dinero="));
    }
}
