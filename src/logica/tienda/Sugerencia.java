package logica.tienda;

public class Sugerencia {
    private String estado;
    private Producto producto;

    public Sugerencia(Producto producto) {
        this.producto = producto;
        this.estado = "CREADA";
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Producto getProducto() {
        return this.producto;
    }

    public void sugerirProducto() {
        this.estado = "PENDIENTE";
    }
}
