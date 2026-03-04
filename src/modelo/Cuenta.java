package modelo;

import modelo.enums.Moneda;
import modelo.enums.TipoCuenta;

import java.util.Objects;

public class Cuenta {
    private final int ID;
    private final int usuarioID;
    private final TipoCuenta cuenta;
    private Dinero monto;

    public Cuenta(int ID, int usuarioID, TipoCuenta cuenta){

        if(ID < 0){
            throw new IllegalArgumentException("ID INVALIDO, ESTE NO PUEDE SER < 0");
        }

        if(usuarioID < 0){
            throw new IllegalArgumentException("USUARIO ID INVALIDO, ESTE NO PUEDE SER < 0");
        }

        if(cuenta == null){
            throw new IllegalArgumentException("TIPO CUENTA NULL, DATO INVALIDO");
        }

        this.ID = ID;
        this.usuarioID = usuarioID;
        this.cuenta = cuenta;

        switch (cuenta){
            case CAJA_DE_AHORRO_EN_PESOS -> this.monto = new Dinero(0.00, Moneda.ARS);
            case CAJA_DE_AHORRO_EN_DOLARES -> this.monto = new Dinero(0.00, Moneda.USD);
            case CAJA_DE_AHORRO_EN_EUROS -> this.monto = new Dinero(0.00, Moneda.EUR);
        }
    }

    private void setMonto(Dinero monto){
        this.monto = monto;
    }

    public int getID() {
        return ID;
    }

    public int getUsuarioID() {
        return usuarioID;
    }

    public TipoCuenta getTipoCuenta() {
        return cuenta;
    }

    public Dinero getMonto() {
        return monto;
    }


    private Moneda monedaEsperada(){
        return switch (cuenta){
            case CAJA_DE_AHORRO_EN_PESOS -> Moneda.ARS;
            case CAJA_DE_AHORRO_EN_DOLARES -> Moneda.USD;
            case CAJA_DE_AHORRO_EN_EUROS -> Moneda.EUR;
        };
    }

    public void depositar(Dinero dinero){

        if(dinero == null){
            throw new IllegalArgumentException("DINERO NULL, DATO INVALIDO");
        }

        if(dinero.getMoneda() != this.monedaEsperada()){
            throw new IllegalArgumentException("MONEDAS DE DIFERENTE TIPO, NO SE PUEDE REALIZAR LA ACCION");
        }

        if(dinero.isZero() || dinero.getMonto() < 0.00){
            throw new IllegalArgumentException("MONTO INVALIDO, MONTO NO PUEDE SER <= A 0");
        }

        setMonto(monto.sumarDinero(dinero));
    }

    public void retirar(Dinero dinero){

        if(dinero == null){
            throw new IllegalArgumentException("DINERO NULL, DATO INVALIDO");
        }

        if(dinero.getMoneda() != this.monedaEsperada()){
            throw new IllegalArgumentException("MONEDAS DE DIFERENTE TIPO, NO SE PUEDE REALIZAR LA ACCION");
        }

        if(dinero.isZero() || dinero.getMonto() < 0.00){
            throw new IllegalArgumentException("MONTO INVALIDO, MONTO NO PUEDE SER <= A 0");
        }

        if(monto.compareTo(dinero) < 0){
            throw new IllegalArgumentException("SALDO DE CUENTA INSUFICIENTE, EL MISMO ES MENOR AL PRETENDIDO A RETIRAR");
        }

        setMonto(monto.restarDinero(dinero));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cuenta other)) return false;
        return ID == other.getID();
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    @Override
    public String toString() {
        // No revienta aunque algún campo sea null (en runtime) y muestra campos básicos.
        return "Cuenta{" +
                "id=" + ID +
                ", usuarioId=" + usuarioID +
                ", tipoCuenta=" + cuenta +
                ", dinero=" + monto +
                '}';
    }
}
