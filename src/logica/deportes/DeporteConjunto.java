package logica.deportes;

import java.util.ArrayList;
import java.util.List;

public class DeporteConjunto {
    private String nombre;
    private int minimoJugadoresHabilitados;
    private List<Equipo> equipos;
    private List<Competencia> competencias;

    public DeporteConjunto(String nombre, int minimoJugadoresHabilitados) {
        this.nombre = nombre;
        this.minimoJugadoresHabilitados = minimoJugadoresHabilitados;
        this.equipos = new ArrayList<Equipo>();
        this.competencias = new ArrayList<Competencia>();
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getMinimoJugadores() {
        return this.minimoJugadoresHabilitados;
    }

    public List<Equipo> getEquipos() {
        return this.equipos;
    }

    public void agregarEquipo(Equipo equipo) {
        this.equipos.add(equipo);
    }

    public List<Competencia> getCompetencias() {
        return this.competencias;
    }

    public void agregarCompetencia(Competencia competencia) {
        this.competencias.add(competencia);
    }
}
