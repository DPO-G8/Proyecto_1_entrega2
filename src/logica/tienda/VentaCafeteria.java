package logica.tienda;

import java.util.Map;

import logica.usuarios.Usuario;

public class VentaCafeteria extends Venta {
    public static final double IMPUESTO_CONSUMO = 0.08;
    public static final double PROPINA_SUGERIDA = 0.10;

    private double propina;

    public VentaCafeteria(Usuario comprador) {
        super(comprador);
        this.propina = PROPINA_SUGERIDA;
    }

    public double getPropina() {
        return this.propina;
    }

    public void setPropina(double propina) {
        this.propina = propina;
    }

    @Override
    public double calcularTotal(Map<Articulo, Integer> articulosYCantidades, boolean tieneDescuentoCompartido) {
        return super.calcularTotal(articulosYCantidades, tieneDescuentoCompartido) * (1 + IMPUESTO_CONSUMO + this.propina);
    }
}
