package logica.deportes;

import logica.usuarios.Socio;

public abstract class Estadistica {
    private Socio socio;

    public Estadistica(Socio socio) {
        this.socio = socio;
        socio.agregarEstadistica(this);
    }

    public Socio getSocio() {
        return this.socio;
    }
}
