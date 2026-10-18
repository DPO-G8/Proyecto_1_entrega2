package logica.tienda;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import logica.usuarios.Usuario;

public abstract class Venta {
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

    public void agregarLinea(Producto producto, int cantidad, double precio) {
        this.lineas.add(new LineaVenta(producto, cantidad, precio));
    }

    public double calcularTotal() {
        double subtotal = 0;
        for (LineaVenta linea : this.lineas) {
            subtotal += linea.getPrecioUnitario() * linea.getCantidad();
        }
        return subtotal * (1 - this.comprador.getPorcentajeDescuento());
    }
}
