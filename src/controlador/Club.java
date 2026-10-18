package controlador;
import persistencia.RepositorioDatos;
public class Club {
	private final GestorUsuarios gestorUsuarios;
	private final GestorReservas gestorReservas;

    public Club(RepositorioDatos repositorio) throws Exception {
        this.gestorUsuarios = new GestorUsuarios(repositorio);
        this.gestorReservas = new GestorReservas(repositorio, gestorUsuarios.getUsuarios());
    }

    public GestorUsuarios getGestorUsuarios() {
        return gestorUsuarios;
    }

    public GestorReservas getGestorReservas() {
        return gestorReservas;
    }
}
