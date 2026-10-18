package logica.deportes;

import java.util.ArrayList;
import java.util.List;

import logica.usuarios.Socio;

public class Equipo {
    private String nombre;
    private String categoria;
    private int cupoMaximo;
    private DeporteConjunto deporte;
    private List<InscripcionEquipo> plantilla;
    private List<InscripcionCompetencia> inscripcionesCompetencia;

    public Equipo(String nombre, String categoria, int cupoMaximo, DeporteConjunto deporte) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.cupoMaximo = cupoMaximo;
        this.deporte = deporte;
        this.plantilla = new ArrayList<InscripcionEquipo>();
        this.inscripcionesCompetencia = new ArrayList<InscripcionCompetencia>();
        deporte.agregarEquipo(this);
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getCategoria() {
        return this.categoria;
    }

    public int getCupoMaximo() {
        return this.cupoMaximo;
    }

    public DeporteConjunto getDeporte() {
        return this.deporte;
    }

    public List<InscripcionEquipo> getPlantilla() {
        return this.plantilla;
    }

    public void agregarInscripcion(InscripcionEquipo inscripcion) {
        this.plantilla.add(inscripcion);
    }

    public List<InscripcionCompetencia> getInscripcionesCompetencia() {
        return this.inscripcionesCompetencia;
    }

    public boolean inscribirseEnCompetencia(String nombreCompetencia, List<Socio> nomina) {
        Competencia competencia = null;
        for (Competencia c : this.deporte.getCompetencias()) {
            if (c.getNombre().equals(nombreCompetencia)) {
                competencia = c;
            }
        }
        if (competencia == null) {
            return false;
        }
        if (!competencia.categoriaValida(this.categoria)) {
            return false;
        }
        if (nomina.size() < competencia.getNominaMinima()) {
            return false;
        }
        for (Socio jugador : nomina) {
            if (!estaEnPlantilla(jugador)) {
                return false;
            }
        }
        InscripcionCompetencia inscripcion = InscripcionCompetencia.crearEquipo(this, competencia, nomina);
        this.inscripcionesCompetencia.add(inscripcion);
        return true;
    }

    public boolean alinearJugadores(Partido partido, List<Socio> jugadores) {
        if (jugadores.size() < this.deporte.getMinimoJugadores()) {
            return false;
        }
        for (Socio jugador : jugadores) {
            if (!estaEnPlantilla(jugador) || jugador.estaSuspendido()) {
                return false;
            }
        }
        return partido.registroAlineacion(jugadores);
    }

    private boolean estaEnPlantilla(Socio socio) {
        for (InscripcionEquipo inscripcion : this.plantilla) {
            if (inscripcion.getSocio() == socio) {
                return true;
            }
        }
        return false;
    }
}
