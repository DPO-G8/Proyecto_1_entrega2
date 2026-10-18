package persistencia;
import java.io.File;
import java.nio.file.Files;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import logica.usuarios.Administrador;
import logica.usuarios.Empleado;
import logica.usuarios.Entrenador;
import logica.usuarios.Fisioterapeuta;
import logica.usuarios.Socio;
import logica.usuarios.Usuario;
public class RepositorioJSON implements RepositorioDatos {
	 private final File carpeta;

	 public RepositorioJSON(String carpetaDatos) {
	        this.carpeta = new File(carpetaDatos);
	    }
	 @Override
	    public void guardarUsuarios(Collection<Usuario> usuarios) throws Exception {
	        JSONArray arreglo = new JSONArray();
	        for (Usuario u : usuarios) {
	            JSONObject json = new JSONObject();
	            json.put("login", u.getLogin());
	            json.put("password", u.getPassword());
	            json.put("puntos", u.getPuntosFidelidad());

	            if (u instanceof Socio) {
	                Socio socio = (Socio) u;
	                json.put("tipo", "SOCIO");
	                json.put("edad", socio.getEdad());
	                if (socio.getEmpleadoDescuento() != null) {
	                    json.put("empleadoDescuento", socio.getEmpleadoDescuento().getLogin());
	                }
	            } else if (u instanceof Administrador) {
	                json.put("tipo", "ADMINISTRADOR");
	            } else if (u instanceof Empleado) {
	                Empleado empleado = (Empleado) u;
	                if (u instanceof Entrenador) {
	                    json.put("tipo", "ENTRENADOR");
	                } else if (u instanceof Fisioterapeuta) {
	                    json.put("tipo", "FISIOTERAPEUTA");
	                } else {
	                    json.put("tipo", "EMPLEADO");
	                }
	                json.put("codigoDescuento", empleado.getCodigoDescuento());
	            }
	            arreglo.put(json);
	        }
	        escribir("usuarios.json", arreglo);
	    }

	    @Override
	    public Map<String, Usuario> cargarUsuarios() throws Exception {
	        Map<String, Usuario> usuarios = new LinkedHashMap<String, Usuario>();
	        JSONArray arreglo = leer("usuarios.json");

	        // Pasada 1: crear todos los usuarios
	        for (int i = 0; i < arreglo.length(); i++) {
	            JSONObject json = arreglo.getJSONObject(i);
	            String login = json.getString("login");
	            String password = json.getString("password");
	            Usuario usuario;
	            switch (json.getString("tipo")) {
	            case "SOCIO":
	                usuario = new Socio(login, password, json.getInt("edad"));
	                break;
	            case "ADMINISTRADOR":
	                usuario = new Administrador(login, password);
	                break;
	            case "ENTRENADOR":
	                usuario = new Entrenador(login, password, json.getString("codigoDescuento"));
	                break;
	            case "FISIOTERAPEUTA":
	                usuario = new Fisioterapeuta(login, password, json.getString("codigoDescuento"));
	                break;
	            default:
	                usuario = new Empleado(login, password, json.getString("codigoDescuento"));
	            }
	            usuario.acreditaPuntos(json.getInt("puntos"));
	            usuarios.put(login, usuario);
	        }

	        // Pasada 2: enlazar los socios con el empleado que comparte su descuento
	        for (int i = 0; i < arreglo.length(); i++) {
	            JSONObject json = arreglo.getJSONObject(i);
	            if (json.has("empleadoDescuento")) {
	                Socio socio = (Socio) usuarios.get(json.getString("login"));
	                Empleado empleado = (Empleado) usuarios.get(json.getString("empleadoDescuento"));
	                socio.setEmpleadoDescuento(empleado);
	            }
	        }
	        return usuarios;
	    }



	    private void escribir(String nombreArchivo, JSONArray arreglo) throws Exception {
	        Files.createDirectories(carpeta.toPath());
	        Files.writeString(new File(carpeta, nombreArchivo).toPath(), arreglo.toString(2));
	    }

	    /** Devuelve el contenido del archivo, o un arreglo vacio si todavia no existe. */
	    private JSONArray leer(String nombreArchivo) throws Exception {
	        File archivo = new File(carpeta, nombreArchivo);
	        if (!archivo.exists()) {
	            return new JSONArray();
	        }
	        return new JSONArray(Files.readString(archivo.toPath()));
	    }
}
