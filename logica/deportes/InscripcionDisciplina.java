package logica.deportes;

import logica.usuarios.Socio;

public class InscripcionDisciplina {
    private String nivel;
    private double puntajeRanking;
    private Socio socio;

    public InscripcionDisciplina(Socio socio, String nivel, double puntajeRanking) {
        this.socio = socio;
        this.nivel = nivel;
        this.puntajeRanking = puntajeRanking;
    }

    public String getNivel() {
        return this.nivel;
    }

    public double getPuntajeRanking() {
        return this.puntajeRanking;
    }

    public Socio getSocio() {
        return this.socio;
    }
}
