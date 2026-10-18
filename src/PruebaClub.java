import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import controlador.Club;
import logica.deportes.DisciplinaIndividual;
import logica.deportes.Instalacion;
import logica.usuarios.Socio;
import persistencia.RepositorioJSON;

public class PruebaClub {
    public static void main(String[] args) throws Exception {
        RepositorioJSON repo = new RepositorioJSON("datos");

        if (repo.cargarDisciplinas(repo.cargarUsuarios()).isEmpty()) {
            DisciplinaIndividual padel = new DisciplinaIndividual("Padel", 2);
            padel.agregarInstalacion(new Instalacion(1, "Cancha Padel 1", 4, false, "Padel"));
            ArrayList<DisciplinaIndividual> lista = new ArrayList<DisciplinaIndividual>();
            lista.add(padel);
            repo.guardarDisciplinas(lista);
        }

        Club club = new Club(repo);
        try {
            club.getGestorUsuarios().registrarSocio("ana", "123", 20);
        } catch (Exception e) {
            System.out.println("Registro: " + e.getMessage());
        }
        Socio ana = (Socio) club.getGestorUsuarios().autenticar("ana", "123");

        LocalDate manana = LocalDate.now().plusDays(1);
        for (int intento = 1; intento <= 2; intento++) {
            try {
                club.getGestorReservas().reservar(ana, 1, manana, LocalTime.of(10, 0), LocalTime.of(11, 0), 4,
                        "Dobles");
                System.out.println("Reserva creada para " + ana.getLogin());
            } catch (Exception e) {
                System.out.println("Rechazada: " + e.getMessage());
            }
        }
    }
}
