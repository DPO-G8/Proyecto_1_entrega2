package logica.deportes;

import java.time.LocalDate;

public class PartidoCompetencia extends Partido {
    private String institucionRival;
    private String escenario;
    private String fase;
    private Competencia competencia;
    private Equipo equipo;

    public PartidoCompetencia(LocalDate fecha, String institucionRival, String escenario, String fase,
            Competencia competencia, Equipo equipo) {
        super(fecha);
        this.institucionRival = institucionRival;
        this.escenario = escenario;
        this.fase = fase;
        this.competencia = competencia;
        this.equipo = equipo;
    }

    public String getInstitucionRival() {
        return this.institucionRival;
    }

    public String getEscenario() {
        return this.escenario;
    }

    public String getFase() {
        return this.fase;
    }

    public Competencia getCompetencia() {
        return this.competencia;
    }

    public Equipo getEquipo() {
        return this.equipo;
    }
}
