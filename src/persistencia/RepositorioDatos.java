package persistencia;
import java.util.Collection;
import java.util.Map;
import logica.usuarios.Usuario;
import java.util.ArrayList;
import logica.tienda.Producto;
import logica.tienda.Venta;
import logica.deportes.DisciplinaIndividual;

public interface RepositorioDatos {
	Map<String, Usuario> cargarUsuarios() throws Exception;
	void guardarUsuarios(Collection<Usuario> usuarios) throws Exception;

	ArrayList<DisciplinaIndividual> cargarDisciplinas(Map<String, Usuario> usuarios) throws Exception;

	void guardarDisciplinas(Collection<DisciplinaIndividual> disciplinas) throws Exception;

	ArrayList<Producto> cargarProductos() throws Exception;

	void guardarProductos(Collection<Producto> productos) throws Exception;

	ArrayList<Venta> cargarVentas(Map<String, Usuario> usuarios, Map<Integer, Producto> productos) throws Exception;

	void guardarVentas(Collection<Venta> ventas) throws Exception;
}
