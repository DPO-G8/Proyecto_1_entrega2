package controlador;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;

import logica.deportes.DisciplinaIndividual;
import logica.deportes.Instalacion;
import logica.deportes.Reserva;
import logica.usuarios.Socio;
import logica.usuarios.Usuario;
import persistencia.RepositorioDatos;

public class GestorReservas {

    private final RepositorioDatos repositorio;
    private final ArrayList<DisciplinaIndividual> disciplinas;

    public GestorReservas(RepositorioDatos repositorio, Map<String, Usuario> usuarios) throws Exception {
        this.repositorio = repositorio;
        this.disciplinas = repositorio.cargarDisciplinas(usuarios);
    }

    public ArrayList<DisciplinaIndividual> getDisciplinas() {
        return disciplinas;
    }

    /** Crea una reserva o lanza una excepcion con el motivo del rechazo. */
    public Reserva reservar(Usuario usuario, int idInstalacion, LocalDate fecha, LocalTime horaInicio,
            LocalTime horaFin, int cantidadJugadores, String modalidad) throws Exception {

        if (!(usuario instanceof Socio) || !usuario.puedeReservar()) {
            throw new Exception("Este usuario no tiene permiso para reservar");
        }
        if (!horaFin.isAfter(horaInicio)) {
            throw new Exception("La hora de fin debe ser posterior a la hora de inicio");
        }
        if (fecha.isBefore(LocalDate.now())) {
            throw new Exception("No se puede reservar en una fecha pasada");
        }

        DisciplinaIndividual disciplina = buscarDisciplinaDe(idInstalacion);
        Instalacion instalacion = buscarInstalacion(disciplina, idInstalacion);

        if (!instalacion.cabe(cantidadJugadores, modalidad)) {
            throw new Exception("La instalacion '" + instalacion.getNombre() + "' no admite " + cantidadJugadores
                    + " jugadores en la modalidad " + modalidad);
        }
        if (estaOcupada(instalacion, fecha, horaInicio, horaFin)) {
            throw new Exception("La instalacion '" + instalacion.getNombre() + "' ya esta reservada en ese horario");
        }

        // R17: maximo de instalaciones de la disciplina usadas al mismo tiempo
        int enUso = 0;
        for (Instalacion otra : disciplina.getInstalaciones()) {
            if (otra != instalacion && estaOcupada(otra, fecha, horaInicio, horaFin)) {
                enUso++;
            }
        }
        if (enUso + 1 > disciplina.getMaxInstalacionesSimultaneas()) {
            throw new Exception("La disciplina " + disciplina.getNombre() + " ya usa el maximo de "
                    + disciplina.getMaxInstalacionesSimultaneas() + " instalaciones en ese horario");
        }

        Reserva reserva = new Reserva((Socio) usuario, fecha, horaInicio, horaFin, cantidadJugadores, modalidad);
        instalacion.agregarReserva(reserva);
        repositorio.guardarDisciplinas(disciplinas);
        return reserva;
    }

    // ---------------------------------------------------------------- ayudas

    private DisciplinaIndividual buscarDisciplinaDe(int idInstalacion) throws Exception {
        for (DisciplinaIndividual d : disciplinas) {
            for (Instalacion i : d.getInstalaciones()) {
                if (i.getId() == idInstalacion) {
                    return d;
                }
            }
        }
        throw new Exception("No existe una instalacion con id " + idInstalacion);
    }

    private Instalacion buscarInstalacion(DisciplinaIndividual disciplina, int idInstalacion) throws Exception {
        for (Instalacion i : disciplina.getInstalaciones()) {
            if (i.getId() == idInstalacion) {
                return i;
            }
        }
        throw new Exception("No existe una instalacion con id " + idInstalacion);
    }

    /** Hay solapamiento si una reserva empieza antes de que termine la nueva y termina despues de que empiece. */
    private boolean estaOcupada(Instalacion instalacion, LocalDate fecha, LocalTime inicio, LocalTime fin) {
        for (Reserva r : instalacion.getReservas()) {
            if (r.getFecha().equals(fecha) && inicio.isBefore(r.getHoraFin()) && fin.isAfter(r.getHoraInicio())) {
                return true;
            }
        }
        return false;
    }
}
