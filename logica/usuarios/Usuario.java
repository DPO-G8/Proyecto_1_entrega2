package logica.usuarios;

public abstract class Usuario {
    private String login;
    private String password;
    private int puntosFidelidad;

    public Usuario(String login, String password) {
        this.login = login;
        this.password = password;
        this.puntosFidelidad = 0;
    }

    public String getLogin() {
        return this.login;
    }

    public String getPassword() {
        return this.password;
    }

    public int getPuntosFidelidad() {
        return this.puntosFidelidad;
    }

    public boolean puedeReservar() {
        return false;
    }

    public boolean puedeRegistrarEquipo() {
        return false;
    }

    public boolean puedeAprobarTurno() {
        return false;
    }

    public void acreditaPuntos(int puntos) {
        this.puntosFidelidad += puntos;
    }
}
