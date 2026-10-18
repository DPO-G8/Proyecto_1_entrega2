package logica.tienda;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import logica.usuarios.Empleado;
import logica.usuarios.Socio;
import logica.usuarios.Usuario;

public abstract class Venta {
    public static final double DESCUENTO_EMPLEADO = 0.15;
    public static final double DESCUENTO_COMPARTIDO = 0.08;

    private LocalDateTime fecha;
    private Usuario comprador;
    private List<LineaVenta> lineas;

    public Venta(Usuario comprador) {
        this.comprador = comprador;
        this.fecha = LocalDateTime.now();
        this.lineas = new ArrayList<LineaVenta>();
    }

    public LocalDateTime getFecha() {
        return this.fecha;
    }

    public Usuario getComprador() {
        return this.comprador;
    }

    public List<LineaVenta> getLineas() {
        return this.lineas;
    }

    public static Venta crearVenta(Socio socio) {
        return new VentaTienda(socio);
    }

    public void agregarLinea(Articulo articulo, int cantidad, double precio) {
        this.lineas.add(new LineaVenta(articulo, cantidad, precio));
    }

    public double calcularTotal(Map<Articulo, Integer> articulosYCantidades, boolean tieneDescuentoCompartido) {
        double subtotal = 0;
        for (Articulo articulo : articulosYCantidades.keySet()) {
            subtotal += articulo.getPrecio() * articulosYCantidades.get(articulo);
        }
        if (this.comprador instanceof Empleado) {
            subtotal = subtotal * (1 - DESCUENTO_EMPLEADO);
        } else if (tieneDescuentoCompartido) {
            subtotal = subtotal * (1 - DESCUENTO_COMPARTIDO);
        }
        return subtotal;
    }
}
