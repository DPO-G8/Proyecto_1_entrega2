package controlador;
import persistencia.RepositorioDatos;
public class Club {
	private final GestorUsuarios gestorUsuarios;
	private final GestorReservas gestorReservas;
	private final GestorVentas gestorVentas;

    public Club(RepositorioDatos repositorio) throws Exception {
        this.gestorUsuarios = new GestorUsuarios(repositorio);
        this.gestorReservas = new GestorReservas(repositorio, gestorUsuarios.getUsuarios());
        this.gestorVentas = new GestorVentas(repositorio, gestorUsuarios.getUsuarios(), gestorReservas);
    }

    public GestorUsuarios getGestorUsuarios() {
        return gestorUsuarios;
    }

    public GestorReservas getGestorReservas() {
        return gestorReservas;
    }

    public GestorVentas getGestorVentas() {
        return gestorVentas;
    }
}
