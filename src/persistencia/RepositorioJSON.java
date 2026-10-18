package persistencia;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import logica.deportes.DisciplinaIndividual;
import logica.deportes.InscripcionDisciplina;
import logica.deportes.Instalacion;
import logica.deportes.Reserva;
import logica.tienda.Articulo;
import logica.tienda.Bebida;
import logica.tienda.LineaVenta;
import logica.tienda.Producto;
import logica.tienda.Snack;
import logica.tienda.Venta;
import logica.tienda.VentaCafeteria;
import logica.tienda.VentaTienda;
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
	                if (socio.getFechaFinSuspension() != null) {
	                    json.put("fechaFinSuspension", socio.getFechaFinSuspension().toString());
	                }
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

	       
	        for (int i = 0; i < arreglo.length(); i++) {
	            JSONObject json = arreglo.getJSONObject(i);
	            String login = json.getString("login");
	            String password = json.getString("password");
	            Usuario usuario;
	            switch (json.getString("tipo")) {
	            case "SOCIO": {
	                Socio nuevo = new Socio(login, password, json.getInt("edad"));
	                if (json.has("fechaFinSuspension")) {
	                    nuevo.setFechaFinSuspension(LocalDate.parse(json.getString("fechaFinSuspension")));
	                }
	                usuario = nuevo;
	                break;
	            }
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
	    @Override
	    public void guardarDisciplinas(Collection<DisciplinaIndividual> disciplinas) throws Exception {
	        JSONArray arreglo = new JSONArray();
	        for (DisciplinaIndividual d : disciplinas) {
	            JSONObject jd = new JSONObject();
	            jd.put("nombre", d.getNombre());
	            jd.put("maxInstalacionesSimultaneas", d.getMaxInstalacionesSimultaneas());

	            JSONArray inscripciones = new JSONArray();
	            for (InscripcionDisciplina i : d.getInscripciones()) {
	                JSONObject ji = new JSONObject();
	                ji.put("socio", i.getSocio().getLogin());
	                ji.put("nivel", i.getNivel());
	                ji.put("puntajeRanking", i.getPuntajeRanking());
	                inscripciones.put(ji);
	            }
	            jd.put("inscripciones", inscripciones);

	            JSONArray instalaciones = new JSONArray();
	            for (Instalacion inst : d.getInstalaciones()) {
	                JSONObject jin = new JSONObject();
	                jin.put("nombre", inst.getNombre());
	                jin.put("id", inst.getId());
	                jin.put("capacidad", inst.getCapacidad());
	                jin.put("techada", inst.isTechada());
	                jin.put("tipoCancha", inst.getTipoCancha());

	                JSONArray reservas = new JSONArray();
	                for (Reserva r : inst.getReservas()) {
	                    JSONObject jr = new JSONObject();
	                    jr.put("socio", r.getSocio().getLogin());
	                    jr.put("fecha", r.getFecha().toString());
	                    jr.put("horaInicio", r.getHoraInicio().toString());
	                    jr.put("horaFin", r.getHoraFin().toString());
	                    jr.put("cantidadJugadores", r.getCantidadJugadores());
	                    jr.put("modalidad", r.getModalidad());
	                    JSONArray invitados = new JSONArray();
	                    for (Socio invitado : r.getInvitados()) {
	                        invitados.put(invitado.getLogin());
	                    }
	                    jr.put("invitados", invitados);
	                    reservas.put(jr);
	                }
	                jin.put("reservas", reservas);
	                instalaciones.put(jin);
	            }
	            jd.put("instalaciones", instalaciones);
	            arreglo.put(jd);
	        }
	        escribir("disciplinas.json", arreglo);
	    }

	    @Override
	    public ArrayList<DisciplinaIndividual> cargarDisciplinas(Map<String, Usuario> usuarios) throws Exception {
	        ArrayList<DisciplinaIndividual> disciplinas = new ArrayList<DisciplinaIndividual>();
	        JSONArray arreglo = leer("disciplinas.json");
	        for (int i = 0; i < arreglo.length(); i++) {
	            JSONObject jd = arreglo.getJSONObject(i);
	            DisciplinaIndividual disciplina = new DisciplinaIndividual(jd.getString("nombre"),
	                    jd.getInt("maxInstalacionesSimultaneas"));

	            JSONArray inscripciones = jd.getJSONArray("inscripciones");
	            for (int j = 0; j < inscripciones.length(); j++) {
	                JSONObject ji = inscripciones.getJSONObject(j);
	                Socio socio = (Socio) usuarios.get(ji.getString("socio"));
	                disciplina.agregarInscripcion(
	                        new InscripcionDisciplina(socio, ji.getString("nivel"), ji.getDouble("puntajeRanking")));
	            }

	            JSONArray instalaciones = jd.getJSONArray("instalaciones");
	            for (int j = 0; j < instalaciones.length(); j++) {
	                JSONObject jin = instalaciones.getJSONObject(j);
	                Instalacion instalacion = new Instalacion(jin.getInt("id"), jin.getString("nombre"),
	                        jin.getInt("capacidad"), jin.getBoolean("techada"), jin.getString("tipoCancha"));

	                JSONArray reservas = jin.getJSONArray("reservas");
	                for (int k = 0; k < reservas.length(); k++) {
	                    JSONObject jr = reservas.getJSONObject(k);
	                    Socio socio = (Socio) usuarios.get(jr.getString("socio"));
	                    Reserva reserva = new Reserva(socio, LocalDate.parse(jr.getString("fecha")),
	                            LocalTime.parse(jr.getString("horaInicio")), LocalTime.parse(jr.getString("horaFin")),
	                            jr.getInt("cantidadJugadores"), jr.getString("modalidad"));
	                    JSONArray invitados = jr.getJSONArray("invitados");
	                    for (int m = 0; m < invitados.length(); m++) {
	                        reserva.agregarInvitado((Socio) usuarios.get(invitados.getString(m)));
	                    }
	                    instalacion.agregarReserva(reserva);
	                }
	                disciplina.agregarInstalacion(instalacion);
	            }
	            disciplinas.add(disciplina);
	        }
	        return disciplinas;
	    }



	    @Override
	    public void guardarProductos(Collection<Producto> productos) throws Exception {
	        JSONArray arreglo = new JSONArray();
	        for (Producto p : productos) {
	            JSONObject json = new JSONObject();
	            json.put("id", p.getIdProducto());
	            json.put("nombre", p.getNombre());
	            json.put("precio", p.getPrecio());
	            if (p instanceof Articulo) {
	                Articulo a = (Articulo) p;
	                json.put("tipo", "ARTICULO");
	                json.put("categoria", a.getCategoria());
	                json.put("cantidadDisponible", a.getCantidadDisponible());
	                json.put("ubicacion", a.getUbicacion());
	            } else if (p instanceof Bebida) {
	                json.put("tipo", "BEBIDA");
	                json.put("caliente", ((Bebida) p).isCaliente());
	            } else {
	                json.put("tipo", "SNACK");
	                json.put("alergenos", new JSONArray(((Snack) p).getAlergenos()));
	            }
	            arreglo.put(json);
	        }
	        escribir("productos.json", arreglo);
	    }

	    @Override
	    public ArrayList<Producto> cargarProductos() throws Exception {
	        ArrayList<Producto> productos = new ArrayList<Producto>();
	        JSONArray arreglo = leer("productos.json");
	        for (int i = 0; i < arreglo.length(); i++) {
	            JSONObject json = arreglo.getJSONObject(i);
	            int id = json.getInt("id");
	            String nombre = json.getString("nombre");
	            double precio = json.getDouble("precio");
	            switch (json.getString("tipo")) {
	            case "ARTICULO":
	                productos.add(new Articulo(id, nombre, precio, json.getString("categoria"),
	                        json.getInt("cantidadDisponible"), json.getString("ubicacion")));
	                break;
	            case "BEBIDA":
	                productos.add(new Bebida(id, nombre, precio, json.getBoolean("caliente")));
	                break;
	            default:
	                ArrayList<String> alergenos = new ArrayList<String>();
	                JSONArray ja = json.getJSONArray("alergenos");
	                for (int j = 0; j < ja.length(); j++) {
	                    alergenos.add(ja.getString(j));
	                }
	                productos.add(new Snack(id, nombre, precio, alergenos));
	            }
	        }
	        return productos;
	    }

	    @Override
	    public void guardarVentas(Collection<Venta> ventas) throws Exception {
	        JSONArray arreglo = new JSONArray();
	        for (Venta v : ventas) {
	            JSONObject json = new JSONObject();
	            json.put("fecha", v.getFecha().toString());
	            json.put("comprador", v.getComprador().getLogin());
	            json.put("puntosRedimidos", v.getPuntosRedimidos());
	            if (v instanceof VentaCafeteria) {
	                json.put("tipo", "CAFETERIA");
	                json.put("propina", ((VentaCafeteria) v).getPorcentajePropina());
	            } else {
	                json.put("tipo", "TIENDA");
	            }
	            JSONArray lineas = new JSONArray();
	            for (LineaVenta l : v.getLineas()) {
	                JSONObject jl = new JSONObject();
	                jl.put("producto", l.getProducto().getIdProducto());
	                jl.put("cantidad", l.getCantidad());
	                jl.put("precioUnitario", l.getPrecioUnitario());
	                lineas.put(jl);
	            }
	            json.put("lineas", lineas);
	            arreglo.put(json);
	        }
	        escribir("ventas.json", arreglo);
	    }

	    @Override
	    public ArrayList<Venta> cargarVentas(Map<String, Usuario> usuarios, Map<Integer, Producto> productos)
	            throws Exception {
	        ArrayList<Venta> ventas = new ArrayList<Venta>();
	        JSONArray arreglo = leer("ventas.json");
	        for (int i = 0; i < arreglo.length(); i++) {
	            JSONObject json = arreglo.getJSONObject(i);
	            Usuario comprador = usuarios.get(json.getString("comprador"));
	            Venta venta;
	            if (json.getString("tipo").equals("CAFETERIA")) {
	                VentaCafeteria vc = new VentaCafeteria(comprador);
	                vc.setPorcentajePropina(json.getDouble("propina"));
	                venta = vc;
	            } else {
	                venta = new VentaTienda(comprador);
	            }
	            venta.setFecha(LocalDateTime.parse(json.getString("fecha")));
	            venta.setPuntosRedimidos(json.getInt("puntosRedimidos"));
	            JSONArray lineas = json.getJSONArray("lineas");
	            for (int j = 0; j < lineas.length(); j++) {
	                JSONObject jl = lineas.getJSONObject(j);
	                venta.agregarLinea(productos.get(jl.getInt("producto")), jl.getInt("cantidad"),
	                        jl.getDouble("precioUnitario"));
	            }
	            ventas.add(venta);
	        }
	        return ventas;
	    }

	    private void escribir(String nombreArchivo, JSONArray arreglo) throws Exception {
	        Files.createDirectories(carpeta.toPath());
	        Files.writeString(new File(carpeta, nombreArchivo).toPath(), arreglo.toString(2));
	    }

	    // Devuelve el contenido del archivo, o un arreglo vacio si todavia no existe.
	    private JSONArray leer(String nombreArchivo) throws Exception {
	        File archivo = new File(carpeta, nombreArchivo);
	        if (!archivo.exists()) {
	            return new JSONArray();
	        }
	        return new JSONArray(Files.readString(archivo.toPath()));
	    }
}
