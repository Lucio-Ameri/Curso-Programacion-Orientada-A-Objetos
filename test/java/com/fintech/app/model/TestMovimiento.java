package java.com.fintech.app.model;

import main.java.com.fintech.app.model.Dinero;
import main.java.com.fintech.app.model.Movimiento;
import main.java.com.fintech.app.model.enums.Moneda;
import main.java.com.fintech.app.model.enums.TipoMovimiento;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestMovimiento {

    @Test
    public void TestCrearMovimientoValido(){
        LocalDateTime tiempo = LocalDateTime.now();
        Dinero monto = new Dinero(300, Moneda.ARS);
        Movimiento mov = new Movimiento(1, TipoMovimiento.RETIRO, monto, tiempo, 1);


        assertEquals(1, mov.getMovimientoID());
        assertEquals(TipoMovimiento.RETIRO, mov.getTipoMovimiento());
        assertEquals(monto, mov.getMonto());
        assertEquals(tiempo, mov.getFecha());
        assertEquals(1, mov.getCuentaID());
    }

    @Test
    public void TestConstructorRechazaNulls(){
        LocalDateTime tiempo = LocalDateTime.now();
        Dinero monto = new Dinero(300, Moneda.ARS);

        assertThrows(IllegalArgumentException.class, () -> new Movimiento(1, TipoMovimiento.RETIRO, null, tiempo, 1));
        assertThrows(IllegalArgumentException.class, () -> new Movimiento(1, TipoMovimiento.RETIRO, monto, null, 1));
        assertThrows(IllegalArgumentException.class, () -> new Movimiento(1, null, monto, tiempo, 1));
    }

    @Test
    public void TestConstructorRechazaIDInvalido(){
        LocalDateTime tiempo = LocalDateTime.now();
        Dinero monto = new Dinero(300, Moneda.ARS);

        assertThrows(IllegalArgumentException.class, () -> new Movimiento(-1, TipoMovimiento.RETIRO, monto, tiempo, 1));
        assertThrows(IllegalArgumentException.class, () -> new Movimiento(1, TipoMovimiento.RETIRO, monto, tiempo, -1));
    }

    @Test
    void TestEqualsHashCodePorId(){
        LocalDateTime tiempo1 = LocalDateTime.now();
        LocalDateTime tiempo2 = tiempo1.plusDays(1);

        Dinero monto1 = new Dinero(300, Moneda.ARS);
        Dinero monto2 = new Dinero(500, Moneda.ARS);

        Movimiento mov1 = new Movimiento(1, TipoMovimiento.RETIRO, monto1, tiempo1, 1);
        Movimiento mov2 = new Movimiento(1, TipoMovimiento.DEPOSITO, monto2, tiempo2, 2);

        assertEquals(mov1, mov2);
        assertEquals(mov1.hashCode(), mov2.hashCode());
    }

    @Test
    void TestHashSetNoAdmiteDuplicadosPorId(){
        LocalDateTime tiempo = LocalDateTime.now();
        Dinero monto = new Dinero(300, Moneda.ARS);

        Movimiento mov1 = new Movimiento(1, TipoMovimiento.RETIRO, monto, tiempo, 1);
        Movimiento mov2 = new Movimiento(1, TipoMovimiento.DEPOSITO, monto, tiempo.plusHours(1), 2);

        HashSet<Movimiento> movimientos = new HashSet<>();
        movimientos.add(mov1);
        movimientos.add(mov2);

        assertEquals(1, movimientos.size());
        assertTrue(movimientos.contains(mov1));
        assertTrue(movimientos.contains(mov2));
    }

    @Test
    void TestToStringNoRevientaYContieneCamposBasicos(){
        LocalDateTime tiempo = LocalDateTime.now();
        Dinero monto = new Dinero(300, Moneda.ARS);
        Movimiento mov = new Movimiento(1, TipoMovimiento.RETIRO, monto, tiempo, 1);

        String texto = assertDoesNotThrow(mov::toString);

        assertNotNull(texto);
        assertTrue(texto.contains("1"));
        assertTrue(texto.contains("RETIRO"));
    }
}
