package main.java.com.fintech.app.model.enums;

public enum Moneda {
    ARS(1.00, 1.00),
    USD(1435.00, 1350.00),
    EUR(1710.00, 1630.00);

    private final double compra;
    private final double venta;

    Moneda(double compra, double venta){
        this.compra = compra;
        this.venta = venta;
    }

    public double getCompra(){
        return compra;
    }

    public double getVenta() {
        return venta;
    }
}
