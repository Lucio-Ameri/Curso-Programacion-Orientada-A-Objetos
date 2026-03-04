package modelo;

import modelo.enums.Moneda;

import java.util.Objects;

public class Dinero implements Comparable<Dinero>{
    private final Moneda moneda;
    private final double monto;

    public Dinero(double monto, Moneda moneda){
        if(moneda == null){
            throw(new IllegalArgumentException("EL TIPO DE LA MONEDA NO PUEDE SER NULL"));
        }

        if(Double.isNaN(monto) || Double.isInfinite(monto)){
            throw(new IllegalArgumentException("EL MONTO NO PUEDE SER NaN o INFINITO"));
        }

        if(monto < 0){
            throw(new IllegalArgumentException("EL MONTO NO PUEDE SER NEGATIVO"));
        }

        this.moneda = moneda;
        this.monto = Math.round(monto * 100.0) / 100.0;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public double getMonto() {
        return monto;
    }


    public Dinero sumarDinero(Dinero other){
        validarMismaMoneda(other);

        return new Dinero(this.monto + other.getMonto(), this.moneda);
    }

    public Dinero restarDinero(Dinero other){
        validarMismaMoneda(other);

        if(this.monto < other.getMonto()){
            throw(new IllegalArgumentException("LA RESTA NO SE PUEDE REALIZAR DEBIDO A QUE EL SALDO ES INSUFICIENTE"));
        }

        return new Dinero(this.monto - other.getMonto(), this.moneda);
    }

    public boolean isZero(){
        return this.monto == 0.00;
    }

    private void validarOther(Dinero dinero) {
        if(dinero == null){
            throw new IllegalArgumentException("EL TIPO DE LA MONEDA NO PUEDE SER NULL");
        }
    }

    private void validarMismaMoneda(Dinero other) {
        if (this.moneda != other.moneda) {
            throw new IllegalArgumentException("LAS MONEDAS NO PUEDEN SER DIFERENTES");
        }
    }

    @Override
    public int compareTo(Dinero other){
        validarOther(other);
        validarMismaMoneda(other);
        return Double.compare(this.monto, other.getMonto());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dinero)) return false;
        Dinero dinero = (Dinero) o;
        // como monto siempre está redondeado a 2 decimales, compare exacto es estable
        return Double.compare(dinero.monto, monto) == 0 && moneda == dinero.moneda;
    }

    @Override
    public int hashCode() {
        return Objects.hash(moneda, monto);
    }

    @Override
    public String toString() {
        return String.format("$%f (%s)\n", monto, moneda);
    }
}
