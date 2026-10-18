package logica.deportes;

import java.util.ArrayList;
import java.util.List;

public class Instalacion {
	private String nombre;
    private int capacidad;
    private boolean techada;
    private String tipoCancha;
    private List<Reserva> reservas;

    public Instalacion(int capacidad, boolean techada, String tipoCancha) {
        this.capacidad = capacidad;
        this.techada = techada;
        this.tipoCancha = tipoCancha;
        this.reservas = new ArrayList<Reserva>();
    }
    

    public String getNombre() {
		return nombre;
	}


	public int getCapacidad() {
        return this.capacidad;
    }

    public boolean isTechada() {
        return this.techada;
    }

    public String getTipoCancha() {
        return this.tipoCancha;
    }

    public List<Reserva> getReservas() {
        return this.reservas;
    }

    public void agregarReserva(Reserva reserva) {
        this.reservas.add(reserva);
    }
}
