import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import controlador.Club;
import logica.tienda.Articulo;
import logica.tienda.Bebida;
import logica.tienda.Snack;
import logica.usuarios.Socio;
import persistencia.RepositorioJSON;

public class PruebaVentas {
    public static void main(String[] args) throws Exception {
        Club club = new Club(new RepositorioJSON("datos"));
        if (club.getGestorVentas().getProductos().isEmpty()) {
            club.getGestorVentas().agregarProducto(new Articulo(1, "Raqueta", 100000, "Raquetas", 5, "Estante A"));
            club.getGestorVentas().agregarProducto(new Bebida(2, "Cafe", 5000, true));
            club.getGestorVentas().agregarProducto(new Bebida(3, "Limonada", 4000, false));
            ArrayList<String> al = new ArrayList<String>();
            al.add("mani");
            club.getGestorVentas().agregarProducto(new Snack(4, "Barra", 3000, al));
        }
        try { club.getGestorUsuarios().registrarSocio("ana", "123", 20); } catch (Exception e) { }
        Socio ana = (Socio) club.getGestorUsuarios().autenticar("ana", "123");

        probar(club, ana, 1, 2, 0.10, 0);
        probar(club, ana, 1, 10, 0.10, 0);
        probar(club, ana, 3, 1, 0.15, 0);
        probar(club, ana, 1, 1, 0.10, 99999999);
        System.out.println("Puntos de ana: " + ana.getPuntosFidelidad());
        probar(club, ana, 1, 1, 0.10, 1000);
        System.out.println("Puntos de ana: " + ana.getPuntosFidelidad());
        System.out.println("Alergenos: " + club.getGestorVentas().alergenosDe(4));

        // bebida caliente con reserva proxima
        java.time.LocalDateTime en10 = java.time.LocalDateTime.now().plusMinutes(10);
        try {
            club.getGestorReservas().reservar(ana, 1, en10.toLocalDate(), en10.toLocalTime(),
                    en10.toLocalTime().plusMinutes(50).isBefore(en10.toLocalTime()) ? LocalTime.of(23, 59) : en10.toLocalTime().plusMinutes(50), 4, "Dobles");
            System.out.println("Reserva creada");
        } catch (Exception e) { System.out.println("Reserva: " + e.getMessage()); }
        probar(club, ana, 2, 1, 0.10, 0);
        probar(club, ana, 3, 1, 0.10, 0);
    }

    static void probar(Club club, Socio s, int id, int cant, double propina, int puntos) {
        Map<Integer, Integer> pedido = new LinkedHashMap<Integer, Integer>();
        pedido.put(id, cant);
        try {
            double t = club.getGestorVentas().comprar(s, pedido, propina, puntos).calcularTotal();
            System.out.println("OK producto " + id + " x" + cant + " total=" + t);
        } catch (Exception e) {
            System.out.println("Rechazada: " + e.getMessage());
        }
    }
}
