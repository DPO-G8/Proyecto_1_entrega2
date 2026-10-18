package controlador;
import persistencia.RepositorioDatos;
public class Club {
	private final GestorUsuarios gestorUsuarios;

    public Club(RepositorioDatos repositorio) throws Exception {
        this.gestorUsuarios = new GestorUsuarios(repositorio);
    }

    public GestorUsuarios getGestorUsuarios() {
        return gestorUsuarios;
    }
}


