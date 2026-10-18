package controlador;
import java.util.Map;

import logica.usuarios.Socio;
import logica.usuarios.Usuario;
import persistencia.RepositorioDatos;
public class GestorUsuarios {
	private final RepositorioDatos repositorio;
    private final Map<String, Usuario> usuarios;
//Ctor
    public GestorUsuarios(RepositorioDatos repositorio) throws Exception {
        this.repositorio = repositorio;
        this.usuarios = repositorio.cargarUsuarios();
    }
//Metodos
    public Usuario autenticar (String login, String password) throws Exception {
    	Usuario usuario = usuarios.get(login);
        if (usuario == null) {
            throw new Exception("No existe un usuario con el login '" + login + "'");
        }
        if (!usuario.getPassword().equals(password)) {
            throw new Exception("La contraseña es incorrecta");
        }
        return usuario;
    }
    public Socio registrarSocio(String login, String password, int edad) throws Exception {
        if (usuarios.containsKey(login)) {
            throw new Exception("Ya existe un usuario con el login '" + login + "'");
        }
        Socio socio = new Socio(login, password, edad);
        usuarios.put(login, socio);
        repositorio.guardarUsuarios(usuarios.values());
        return socio;
    }
}

    
