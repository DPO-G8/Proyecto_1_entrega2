package logica.tienda;

import java.util.Map;

import logica.usuarios.Usuario;

public class VentaTienda extends Venta {
    public static final double IVA = 0.19;

    public VentaTienda(Usuario comprador) {
        super(comprador);
    }

    @Override
    public double calcularTotal(Map<Articulo, Integer> articulosYCantidades, boolean tieneDescuentoCompartido) {
        return super.calcularTotal(articulosYCantidades, tieneDescuentoCompartido) * (1 + IVA);
    }
}
