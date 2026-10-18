package controlador;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import logica.tienda.Articulo;
import logica.tienda.Bebida;
import logica.tienda.Producto;
import logica.tienda.Snack;
import logica.tienda.Venta;
import logica.tienda.VentaCafeteria;
import logica.tienda.VentaTienda;
import logica.usuarios.Administrador;
import logica.usuarios.Socio;
import logica.usuarios.Usuario;
import persistencia.RepositorioDatos;

public class GestorVentas {

    /** Porcentaje de la compra que se acredita en puntos de fidelidad (R: 2%). */
    public static final double PORCENTAJE_PUNTOS = 0.02;
    /** Minutos antes de una reserva en los que ya no se vende bebida caliente. */
    public static final int MINUTOS_RESTRICCION = 30;

    private final RepositorioDatos repositorio;
    private final Map<String, Usuario> usuarios;
    private final GestorReservas gestorReservas;
    private final Map<Integer, Producto> productos;
    private final ArrayList<Venta> ventas;

    public GestorVentas(RepositorioDatos repositorio, Map<String, Usuario> usuarios, GestorReservas gestorReservas)
            throws Exception {
        this.repositorio = repositorio;
        this.usuarios = usuarios;
        this.gestorReservas = gestorReservas;
        this.productos = new LinkedHashMap<Integer, Producto>();
        for (Producto p : repositorio.cargarProductos()) {
            this.productos.put(p.getIdProducto(), p);
        }
        this.ventas = repositorio.cargarVentas(usuarios, productos);
    }

    public Map<Integer, Producto> getProductos() {
        return productos;
    }

    public ArrayList<Venta> getVentas() {
        return ventas;
    }

    public Producto buscarProducto(int idProducto) throws Exception {
        Producto p = productos.get(idProducto);
        if (p == null) {
            throw new Exception("No existe un producto con id " + idProducto);
        }
        return p;
    }

    public void agregarProducto(Producto producto) throws Exception {
        if (productos.containsKey(producto.getIdProducto())) {
            throw new Exception("Ya existe un producto con id " + producto.getIdProducto());
        }
        productos.put(producto.getIdProducto(), producto);
        repositorio.guardarProductos(productos.values());
    }

    /** Alergenos de un snack, para informarlos al socio antes de la compra. */
    public java.util.List<String> alergenosDe(int idProducto) throws Exception {
        Producto p = buscarProducto(idProducto);
        if (!(p instanceof Snack)) {
            throw new Exception("El producto '" + p.getNombre() + "' no es un snack");
        }
        return ((Snack) p).getAlergenos();
    }

    /**
     * Compra con la propina sugerida y sin redimir puntos.
     */
    public Venta comprar(Usuario comprador, Map<Integer, Integer> pedido) throws Exception {
        return comprar(comprador, pedido, VentaCafeteria.PROPINA_SUGERIDA, 0);
    }

    /**
     * Registra una venta o lanza una excepcion con el motivo del rechazo.
     *
     * @param pedido id de producto -> cantidad. Todos deben ser articulos (venta de tienda, IVA) o
     *               todos bebidas/snacks (venta de cafeteria, impuesto al consumo y propina).
     * @param propina porcentaje de propina (0 a 1); solo aplica a cafeteria.
     * @param puntosARedimir puntos de fidelidad usados como descuento (1 punto = 1 peso).
     */
    public Venta comprar(Usuario comprador, Map<Integer, Integer> pedido, double propina, int puntosARedimir)
            throws Exception {

        // ---- 1. validaciones: nada se modifica hasta que todas pasen
        if (comprador == null || comprador instanceof Administrador) {
            throw new Exception("Este usuario no puede realizar compras");
        }
        if (pedido == null || pedido.isEmpty()) {
            throw new Exception("El pedido no tiene productos");
        }
        if (puntosARedimir < 0) {
            throw new Exception("Los puntos a redimir no pueden ser negativos");
        }

        boolean hayArticulos = false;
        boolean hayCafeteria = false;
        boolean hayBebidaCaliente = false;
        for (Map.Entry<Integer, Integer> linea : pedido.entrySet()) {
            Producto p = buscarProducto(linea.getKey());
            if (linea.getValue() <= 0) {
                throw new Exception("La cantidad de '" + p.getNombre() + "' debe ser positiva");
            }
            if (p instanceof Articulo) {
                hayArticulos = true;
                Articulo a = (Articulo) p;
                if (linea.getValue() > a.getCantidadDisponible()) {
                    throw new Exception("No hay suficientes unidades de " + a.getNombre() + ": se piden "
                            + linea.getValue() + " y hay " + a.getCantidadDisponible());
                }
            } else {
                hayCafeteria = true;
                if (p instanceof Bebida && ((Bebida) p).isCaliente()) {
                    hayBebidaCaliente = true;
                }
            }
        }
        if (hayArticulos && hayCafeteria) {
            throw new Exception("No se pueden mezclar articulos de la tienda con productos de la cafeteria "
                    + "en una misma compra");
        }

        // RF5: no se vende bebida caliente a un socio con reserva en padel / tenis de mesa techado
        if (hayBebidaCaliente && comprador instanceof Socio) {
            LocalDateTime ahora = LocalDateTime.now();
            if (gestorReservas.reservaRestringidaCercana((Socio) comprador, ahora, MINUTOS_RESTRICCION) != null) {
                throw new Exception("No se puede vender bebida caliente: el socio tiene una reserva en padel o "
                        + "tenis de mesa techado en curso o que empieza en menos de " + MINUTOS_RESTRICCION
                        + " minutos");
            }
        }
        if (hayCafeteria && (propina < 0 || propina > 1)) {
            throw new Exception("La propina debe estar entre 0% y 100%");
        }

        // ---- 2. se arma la venta
        Venta venta;
        if (hayArticulos) {
            venta = new VentaTienda(comprador);
        } else {
            VentaCafeteria vc = new VentaCafeteria(comprador);
            vc.setPorcentajePropina(propina);
            venta = vc;
        }
        for (Map.Entry<Integer, Integer> linea : pedido.entrySet()) {
            Producto p = productos.get(linea.getKey());
            venta.agregarLinea(p, linea.getValue(), p.getPrecio());
        }

        double baseConDescuento = venta.calcularSubtotal() * (1 - comprador.getPorcentajeDescuento());
        if (puntosARedimir > baseConDescuento) {
            throw new Exception("No se pueden redimir " + puntosARedimir + " puntos: la compra con descuento solo "
                    + "vale " + Math.round(baseConDescuento));
        }

        // ---- 3. se aplican los cambios (redimir puede fallar, por eso va primero)
        if (puntosARedimir > 0) {
            comprador.redimirPuntos(puntosARedimir);
            venta.setPuntosRedimidos(puntosARedimir);
        }
        for (Map.Entry<Integer, Integer> linea : pedido.entrySet()) {
            Producto p = productos.get(linea.getKey());
            if (p instanceof Articulo) {
                ((Articulo) p).descontarInventario(linea.getValue());
            }
        }
        comprador.acreditaPuntos((int) Math.floor(venta.calcularTotal() * PORCENTAJE_PUNTOS));
        ventas.add(venta);

        repositorio.guardarProductos(productos.values());
        repositorio.guardarVentas(ventas);
        repositorio.guardarUsuarios(usuarios.values());
        return venta;
    }
}
