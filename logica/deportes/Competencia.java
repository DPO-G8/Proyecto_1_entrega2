package logica.deportes;

import java.util.ArrayList;
import java.util.List;

public class Competencia {
    private String nombre;
    private String entidadOrganizadora;
    private String temporada;
    private String categoriaHabilitada;
    private String formatoJuego;
    private int nominaMinima;
    private DeporteConjunto deporte;
    private List<InscripcionCompetencia> inscripciones;

    public Competencia(String nombre, String entidadOrganizadora, String temporada, String categoriaHabilitada,
            String formatoJuego, int nominaMinima, DeporteConjunto deporte) {
        this.nombre = nombre;
        this.entidadOrganizadora = entidadOrganizadora;
        this.temporada = temporada;
        this.categoriaHabilitada = categoriaHabilitada;
        this.formatoJuego = formatoJuego;
        this.nominaMinima = nominaMinima;
        this.deporte = deporte;
        this.inscripciones = new ArrayList<InscripcionCompetencia>();
        deporte.agregarCompetencia(this);
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getEntidadOrganizadora() {
        return this.entidadOrganizadora;
    }

    public String getTemporada() {
        return this.temporada;
    }

    public String getCategoriaHabilitada() {
        return this.categoriaHabilitada;
    }

    public String getFormatoJuego() {
        return this.formatoJuego;
    }

    public int getNominaMinima() {
        return this.nominaMinima;
    }

    public DeporteConjunto getDeporte() {
        return this.deporte;
    }

    public List<InscripcionCompetencia> getInscripciones() {
        return this.inscripciones;
    }

    public void agregarInscripcion(InscripcionCompetencia inscripcion) {
        this.inscripciones.add(inscripcion);
    }

    public boolean categoriaValida(String categoria) {
        return this.categoriaHabilitada.equals(categoria);
    }
}
