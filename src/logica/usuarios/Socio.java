package logica.usuarios;

import java.util.ArrayList;
import java.util.List;

import logica.deportes.Estadistica;
import logica.deportes.EstadisticaFutbol;

public class Socio extends Usuario {
    private int edad;
    private Empleado empleadoDescuento;
    private List<Estadistica> estadisticas;

    public Socio(String login, String password, int edad) {
        super(login, password);
        this.edad = edad;
        this.empleadoDescuento = null;
        this.estadisticas = new ArrayList<Estadistica>();
    }

    public int getEdad() {
        return this.edad;
    }

    public Empleado getEmpleadoDescuento() {
        return this.empleadoDescuento;
    }

    public void setEmpleadoDescuento(Empleado empleadoDescuento) {
        this.empleadoDescuento = empleadoDescuento;
    }

    public List<Estadistica> getEstadisticas() {
        return this.estadisticas;
    }

    public void agregarEstadistica(Estadistica estadistica) {
        this.estadisticas.add(estadistica);
    }

    @Override
    public boolean puedeReservar() {
        return true;
    }

    public boolean estaSuspendido() {
        int amarillas = 0;
        int rojas = 0;
        for (Estadistica e : this.estadisticas) {
            if (e instanceof EstadisticaFutbol) {
                EstadisticaFutbol ef = (EstadisticaFutbol) e;
                amarillas += ef.getTarjetasAmarillas();
                rojas += ef.getTarjetasRojas();
            }
        }
        return amarillas >= 2 || rojas >= 1;
    }

    public boolean tieneDescuentoCompartido() {
        return this.empleadoDescuento != null;
    }
}
