package java.com.fintech.app.model;

import main.java.com.fintech.app.model.Dinero;
import main.java.com.fintech.app.model.enums.Moneda;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestDinero {

    @Test
    public void TestCrearDineroConValoresValidos(){
        Dinero d = new Dinero(100.00, Moneda.ARS);
        Dinero d2 = new Dinero(100.00, Moneda.USD);
        Dinero d3 = new Dinero(100.00, Moneda.EUR);

        assertEquals(100.00, d.getMonto());
        assertEquals(Moneda.ARS, d.getMoneda());

        assertEquals(100.00, d2.getMonto());
        assertEquals(Moneda.USD, d2.getMoneda());

        assertEquals(100.00, d3.getMonto());
        assertEquals(Moneda.EUR, d3.getMoneda());
    }

    @Test
    public void TestConstructorNoAdmiteMonedaNula(){
        assertThrows(IllegalArgumentException.class, () -> new Dinero(100.00, null));
    }

    @Test
    public void TestConstructorNoAdmiteMontoNegativo(){
        assertThrows(IllegalArgumentException.class, () -> new Dinero(-100.00, Moneda.ARS));
        assertThrows(IllegalArgumentException.class, () -> new Dinero(-10.00, Moneda.ARS));
        assertThrows(IllegalArgumentException.class, () -> new Dinero(-1.00, Moneda.ARS));
        assertThrows(IllegalArgumentException.class, () -> new Dinero(-0.10, Moneda.ARS));
        assertThrows(IllegalArgumentException.class, () -> new Dinero(-0.01, Moneda.ARS));
        assertThrows(IllegalArgumentException.class, () -> new Dinero(-0.0000001, Moneda.ARS));
    }

    @Test
    public void TestConstructorNoAdmiteMontoInfinitoONaN(){
        assertThrows(IllegalArgumentException.class, () -> new Dinero(Double.NaN, Moneda.ARS));
        assertThrows(IllegalArgumentException.class, () -> new Dinero(Double.NEGATIVE_INFINITY, Moneda.ARS));
        assertThrows(IllegalArgumentException.class, () -> new Dinero(Double.POSITIVE_INFINITY, Moneda.ARS));
    }

    @Test
    public void TestConstructorRedondeaADosDecimales(){
        Dinero d2 = new Dinero(100.254, Moneda.ARS);
        Dinero d3 = new Dinero(100.256, Moneda.ARS);

        Dinero d5 = new Dinero(100.004, Moneda.USD);
        Dinero d6 = new Dinero(100.006, Moneda.USD);

        Dinero d7 = new Dinero(100.004, Moneda.EUR);
        Dinero d8 = new Dinero(100.006, Moneda.EUR);

        assertEquals(100.25, d2.getMonto());
        assertEquals(100.26, d3.getMonto());
        assertEquals(100.00, d5.getMonto());
        assertEquals(100.01, d6.getMonto());
        assertEquals(100.00, d7.getMonto());
        assertEquals(100.01, d8.getMonto());
    }

    @Test
    public void TestSumaMismaMoneda(){
        Dinero d = new Dinero(100.00, Moneda.ARS);
        Dinero d2 = new Dinero(100.00, Moneda.ARS);

        Dinero d3 = new Dinero(100.00, Moneda.USD);
        Dinero d4 = new Dinero(100.00, Moneda.USD);

        Dinero d5 = new Dinero(100.00, Moneda.EUR);
        Dinero d6 = new Dinero(100.00, Moneda.EUR);

        assertEquals(200.00, d.sumarDinero(d2).getMonto());
        assertEquals(200.00, d3.sumarDinero(d4).getMonto());
        assertEquals(200.00, d5.sumarDinero(d6).getMonto());
    }

    @Test
    public void TestSumaRechazaMonedasDiferentes(){
        Dinero d = new Dinero(100.00, Moneda.ARS);
        Dinero d2 = new Dinero(100.00, Moneda.USD);
        Dinero d3 = new Dinero(100.00, Moneda.EUR);

        assertThrows(IllegalArgumentException.class, () -> d.sumarDinero(d2));
        assertThrows(IllegalArgumentException.class, () -> d.sumarDinero(d3));
        assertThrows(IllegalArgumentException.class, () -> d2.sumarDinero(d));
        assertThrows(IllegalArgumentException.class, () -> d2.sumarDinero(d3));
        assertThrows(IllegalArgumentException.class, () -> d3.sumarDinero(d));
        assertThrows(IllegalArgumentException.class, () -> d3.sumarDinero(d2));
    }

    @Test
    public void TestRestMismaMoneda(){
        Dinero d = new Dinero(100.00, Moneda.ARS);
        Dinero d2 = new Dinero(100.00, Moneda.ARS);

        Dinero d3 = new Dinero(100.00, Moneda.USD);
        Dinero d4 = new Dinero(100.00, Moneda.USD);

        Dinero d5 = new Dinero(100.00, Moneda.EUR);
        Dinero d6 = new Dinero(100.00, Moneda.EUR);

        assertEquals(0.00, d.restarDinero(d2).getMonto());
        assertEquals(0.00, d3.restarDinero(d4).getMonto());
        assertEquals(0.00, d5.restarDinero(d6).getMonto());
    }

    @Test
    public void TestRestaRechazaMonedasDiferentes(){
        Dinero d = new Dinero(100.00, Moneda.ARS);
        Dinero d2 = new Dinero(100.00, Moneda.USD);
        Dinero d3 = new Dinero(100.00, Moneda.EUR);

        assertThrows(IllegalArgumentException.class, () -> d.restarDinero(d2));
        assertThrows(IllegalArgumentException.class, () -> d.restarDinero(d3));
        assertThrows(IllegalArgumentException.class, () -> d2.restarDinero(d));
        assertThrows(IllegalArgumentException.class, () -> d2.restarDinero(d3));
        assertThrows(IllegalArgumentException.class, () -> d3.restarDinero(d));
        assertThrows(IllegalArgumentException.class, () -> d3.restarDinero(d2));
    }

    @Test
    public void TestRestaRechazaSiElSaldoEsInsuficiente(){
        Dinero d = new Dinero(100.00, Moneda.ARS);
        Dinero d2 = new Dinero(150.00, Moneda.ARS);

        Dinero d3 = new Dinero(100.00, Moneda.USD);
        Dinero d4 = new Dinero(150.00, Moneda.USD);

        Dinero d5 = new Dinero(100.00, Moneda.EUR);
        Dinero d6 = new Dinero(150.00, Moneda.EUR);

        assertThrows(IllegalArgumentException.class, () -> d.restarDinero(d2));
        assertThrows(IllegalArgumentException.class, () -> d3.restarDinero(d4));
        assertThrows(IllegalArgumentException.class, () -> d5.restarDinero(d6));
    }

    @Test
    public void TestDineroEsCero(){
        assertTrue(new Dinero(0.0, Moneda.ARS).isZero());
        assertTrue(new Dinero(0.0, Moneda.USD).isZero());
        assertTrue(new Dinero(0.0, Moneda.EUR).isZero());

        assertTrue(new Dinero(0.000001, Moneda.ARS).isZero());
        assertTrue(new Dinero(0.000001, Moneda.USD).isZero());
        assertTrue(new Dinero(0.000001, Moneda.EUR).isZero());

        assertFalse(new Dinero(0.01, Moneda.ARS).isZero());
        assertFalse(new Dinero(0.01, Moneda.USD).isZero());
        assertFalse(new Dinero(0.01, Moneda.EUR).isZero());
    }

    @Test
    public void TestCompararMismaMoneda(){
        Dinero a = new Dinero(100.00, Moneda.EUR);
        Dinero b = new Dinero(150.00, Moneda.EUR);
        Dinero c = new Dinero(100.00, Moneda.EUR);

        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
        assertEquals(0, a.compareTo(c));

        Dinero d = new Dinero(100.00, Moneda.ARS);
        Dinero e = new Dinero(150.00, Moneda.ARS);
        Dinero f = new Dinero(100.00, Moneda.ARS);

        assertTrue(d.compareTo(e) < 0);
        assertTrue(e.compareTo(d) > 0);
        assertEquals(0, d.compareTo(f));

        Dinero g = new Dinero(100.00, Moneda.USD);
        Dinero h = new Dinero(150.00, Moneda.USD);
        Dinero i = new Dinero(100.00, Moneda.USD);

        assertTrue(g.compareTo(h) < 0);
        assertTrue(h.compareTo(g) > 0);
        assertEquals(0, g.compareTo(i));
    }

    @Test
    public void TestCompararMonedasDiferentesTiraError(){
        Dinero ars = new Dinero(100.00, Moneda.ARS);
        Dinero usd = new Dinero(100.00, Moneda.USD);
        Dinero eur = new Dinero(100.00, Moneda.EUR);

        assertThrows(IllegalArgumentException.class, () -> ars.compareTo(usd));
        assertThrows(IllegalArgumentException.class, () -> ars.compareTo(eur));
        assertThrows(IllegalArgumentException.class, () -> usd.compareTo(eur));
    }
}
