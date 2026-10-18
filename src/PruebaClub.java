import controlador.Club;
import persistencia.RepositorioJSON;

public class PruebaClub {
    public static void main(String[] args) throws Exception {
        Club club = new Club(new RepositorioJSON("datos"));
        try {
            club.getGestorUsuarios().registrarSocio("ana", "123", 20);
        } catch (Exception e) {
            System.out.println("Registro: " + e.getMessage());
        }
        System.out.println(club.getGestorUsuarios().autenticar("ana", "123").getLogin());
    }
}