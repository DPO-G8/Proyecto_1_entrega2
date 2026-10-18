package logica.deportes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import logica.usuarios.Entrenador;

public class SesionEntrenamiento {
    private LocalDate fecha;
    private LocalTime hora;
    private int duracion;
    private String tipo;
    private Entrenador entrenador;
    private Instalacion instalacion;
    private Equipo equipo;
    private List<Asistencia> asistencias;

    public SesionEntrenamiento(LocalDate fecha, LocalTime hora, int duracion, String tipo, Entrenador entrenador,
            Instalacion instalacion) {
        this.fecha = fecha;
        this.hora = hora;
        this.duracion = duracion;
        this.tipo = tipo;
        this.entrenador = entrenador;
        this.instalacion = instalacion;
        this.equipo = null;
        this.asistencias = new ArrayList<Asistencia>();
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public LocalTime getHora() {
        return this.hora;
    }

    public int getDuracion() {
        return this.duracion;
    }

    public String getTipo() {
        return this.tipo;
    }

    public Entrenador getEntrenador() {
        return this.entrenador;
    }

    public Instalacion getInstalacion() {
        return this.instalacion;
    }

    public Equipo getEquipo() {
        return this.equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public List<Asistencia> getAsistencias() {
        return this.asistencias;
    }

    public void agregarAsistencia(Asistencia asistencia) {
        this.asistencias.add(asistencia);
    }
}
