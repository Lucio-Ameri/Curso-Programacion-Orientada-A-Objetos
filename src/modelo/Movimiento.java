package modelo;

import modelo.enums.TipoMovimiento;

import java.time.LocalDateTime;

public class Movimiento {
    private final int movimientoID;
    private final TipoMovimiento mov;
    private final Dinero monto;
    private final LocalDateTime fecha;
    private final int cuentaID;

    public Movimiento(int movimientoID, TipoMovimiento mov, Dinero monto, LocalDateTime fecha, int cuentaID){

        if(mov == null){
            throw new IllegalArgumentException("MOVIMIENTO INVALIDO, ESTE NO PUEDE SER NULL");
        }

        if(monto == null){
            throw new IllegalArgumentException("MONTO INVALIDO, ESTE NO PUEDE SER NULL");
        }

        if(fecha == null){
            throw new IllegalArgumentException("FECHA INVALIDA, ESTE NO PUEDE SER NULL");
        }

        if(movimientoID < 0 || cuentaID < 0){
            throw new IllegalArgumentException("ID INVALIDO, ESTE NO PUEDE SER < 0");
        }

        this.movimientoID = movimientoID;
        this.mov = mov;
        this.monto = monto;
        this.fecha = fecha;
        this.cuentaID = cuentaID;
    }


    public int getMovimientoID() {
        return movimientoID;
    }

    public TipoMovimiento getTipoMovimiento() {
        return mov;
    }

    public Dinero getMonto() {
        return monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public int getCuentaID() {
        return cuentaID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movimiento)) return false;
        Movimiento that = (Movimiento) o;
        return movimientoID == that.movimientoID;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(movimientoID);
    }

    @Override
    public String toString() {
        return "Movimiento{" +
                "movimientoID=" + movimientoID +
                ", tipoMovimiento=" + mov +
                ", monto=" + monto +
                ", fecha=" + fecha +
                ", cuentaID=" + cuentaID +
                '}';
    }
}
