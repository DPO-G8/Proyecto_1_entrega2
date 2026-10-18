package logica.deportes;

import logica.usuarios.Socio;

public class EstadisticaTenisMesa extends Estadistica {
    private int setsGanados;
    private int puntosTotales;

    public EstadisticaTenisMesa(Socio socio, int setsGanados, int puntosTotales) {
        super(socio);
        this.setsGanados = setsGanados;
        this.puntosTotales = puntosTotales;
    }

    public int getSetsGanados() {
        return this.setsGanados;
    }

    public int getPuntosTotales() {
        return this.puntosTotales;
    }
}
