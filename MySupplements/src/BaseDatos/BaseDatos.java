package BaseDatos;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import Clases.Cliente;
import Clases.Pedido;
import Clases.Producto;
import Clases.ProductoMerchandise;
import Clases.ProductoSuplementos;
import Ventanas.VentanaProducto;

public class BaseDatos {
	private static Connection con;
	private static Logger logger = Logger.getLogger( "BaseDatos" );
	
	public static void initBD(String nombreBD) {
		
		
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:"+nombreBD);
			logger.log(Level.INFO, "Conexión establecida con jdbc:sqlite:"+nombreBD);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	

	public static void closeBD() {
		if(con!=null) {
			try {
				con.close();
				logger.log(Level.INFO, "Conexión cerrada");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 
	 */
	public static void crearTablas() {
		String sent1 = "CREATE TABLE IF NOT EXISTS Cliente(nom String, con String, dni String,fnac bigint,puntos String)";
		Statement st = null;
		String sent2 = "CREATE TABLE IF NOT EXISTS Pedidos(cped Integer, fpedido bigint , dnic String , cprod Integer)";
		try {
			st = con.createStatement();
			st.executeUpdate(sent1);
			st.executeUpdate(sent2);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(st!=null) {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * Metodo que elimina un producto de un Pedido de la tabla Pedidos de la base de datos
	 * @param pr Producto que se desea eliminar
	 */
	public static void eliminarProductodePedido(Producto pr) {
		String sent = "DELETE FROM Pedidos WHERE cprod ='"+pr.getCod()+"'";
		Statement st = null;
		try {
			st=con.createStatement();
			st.executeUpdate(sent);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				st.close();
				logger.log(Level.INFO,"Producto"+pr.getNombre()+"con un precio de "+pr.getPrecio()+"€ elimidao correctamente de su pedido");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	

	
	
	/**
	 * Metodo que elimina el pedido de la tabla Pedidos de la  base de datos
	 * @param P Pedido que se desea eliminar
	 */
	public static void eliminarPedido(Pedido P) {
		String sent = "DELETE * FROM Pedidos WHERE cped='"+P.getCod()+"'";
		Statement st = null;
		try {
			st=con.createStatement();
			st.executeUpdate(sent);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				st.close();
				logger.log(Level.INFO,"Pedido eliminado correctamente de la base de datos");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		}
	
	
	
	
	
	/**
	 * Metodo que obtiene una lista de los pedidos 
	 * @param c Cliente del que se quieren obtener sus pedidos
	 * @return Devuelve la lista de pedidos del Cliente
	 */
	
	//POR HACER
	
	/**
	 * Método que recibe el cliente y devuelve la lista con todos los pedidos de ese cliente
	 * @param c Cliente del que se quiere obtener la lista
	 * @return	Devuelve la lista de pedidos del cliente
	 */
	public static ArrayList<Pedido> obtenerPedidosdeCliente(Cliente c) {
		String sent = "SELECT * FROM Pedidos WHERE dnic='"+c.getDni()+"'";
		Statement st=null;
		ArrayList<Pedido> lc=new ArrayList<>();//necesito obtener los datos del pedido y luego la lista de productos
		Pedido p =null;
		try {		
			st= con.createStatement();//Pedidos(cped Integer, fpedido bigint , dnic String , cprod Integer)"
			ResultSet rs = st.executeQuery(sent);
			if(rs.next()) {
				int codpedido = rs.getInt("cped");
				long fpedido = rs.getLong("fpedido");
				p = new Pedido(fpedido, c, obtenerProductosdePedido(codpedido));			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lc;
	}
	
	/**
	 * Método que devuelve el producto con el codigo introducido
	 * @param cod codigo del producto
	 * @return Devuelve el producto con ese codigo
	 */
	public static Producto ObtenerProducto(int cod) {
		ArrayList<Producto>listaproductos = new ArrayList<>();
		VentanaProducto.cargarProductosdeFichero(listaproductos);
		Producto p = new Producto();
		for(Producto pr : listaproductos) {
			if(pr.getCod() == cod) {
				if(pr instanceof ProductoSuplementos) {
					p = (ProductoSuplementos)pr;	
				}else {
					p = (ProductoMerchandise)pr; 
				}
			}
		}
		return p;
	}
	
	
	
	/**
	 * Metodo que obtiene una lista con todos los productos del pedido
	 * @param codpedido codigo del pedido que queremos obtener
	 * @return devuelve una lista con los productos del pedido
	 */
	
	
	public static ArrayList<Producto> obtenerProductosdePedido(int codpedido){
		String sent= "SELECT * FROM Pedidos WHERE cped='"+codpedido+"'";
		ArrayList<Producto> lp = new ArrayList<>();
		Statement st=null;
		try {
			st = con.createStatement();
			ResultSet rs = st.executeQuery(sent);
			if(rs.next()) {
				int cod = rs.getInt("cprod");
				lp.add(ObtenerProducto(cod));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lp;
	}
	
	
	
	
	
	/**
	 * Metodo que inserta en la tabla de Pedidos los productos en fila
	 * @param p producto que se inserta en la tabla pedidos
	 * @throws SQLException 
	 */
	public static void insertarPedido(Pedido p) throws SQLException {
		Statement st = null;
		st= con.createStatement();
		for(Producto pr: p.getListaproductos()) {
			String sent = "INSERT INTO pedidos VALUES('"+p.getCod()+"','"+p.getFec()+"','"+p.getCliente().getDni()+"','"+pr.getCod()+"')";
			st.executeUpdate(sent);
			}
		st.close();	
		}
	
	
/**
 * Metodo que inserta el cliente en la tabla de clientes en la base de datos
 * @param nom Nombre del cliente
 * @param c Contraseña del cliente
 * @param d	Dni del cliente
 * @param ed Fecha de nacimiento del cliente
 */
	public static void insertarCliente(String nom, String c,String d, long fc) {//nombre,contraseña,dni y fecha de nacimiento
		String p = "0";//puntos del cliente empiezan en 0
		String sent = "INSERT INTO Cliente VALUES('"+nom+"','"+c+"','"+d+"','"+fc+"','"+p+"')";//la fecha de nacimiento hay que mirar si se mete a la bd como long
		Statement st = null;
		try {
			st = con.createStatement();
			st.executeUpdate(sent);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				st.close();
				logger.log(Level.INFO,"Cliente guardado correctamente en la base de datos");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Método que comprueba si un Cliente está registrado en la BBDD
	 * @param nom Nombre que ha insertado el Cliente en el jTexfield
	 * @param con Contraseña que ha insertado el Cliente en el jTexfield
	 * @return 0 si el Cliente no existe
	 * 		   1 si el Cliente sí existe pero su contraseña no es correcta
	 * 	       2 si el Cliente y la contraseña son correctos
	 */
	public static int existeCliente(String nom, String c) {
		String sent = "SELECT * FROM Cliente WHERE nom='"+nom+"'";
		Statement st = null;
		int resul = 0;
		try {
			st = con.createStatement();
			ResultSet rs = st.executeQuery(sent);
			if(rs.next()) {
				String contra = rs.getString("con");
				if(contra.equals(c)) {
					resul = 2;
				}else {
					resul = 1;
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return resul;
	}
	public static ArrayList<Cliente> ObtenerListaClientes() throws SQLException {
		ArrayList<Cliente> lc = new ArrayList<>();
		Statement st = con.createStatement();
		String sent = "SELECT * FROM Cliente";
		ResultSet rs = st.executeQuery(sent);
		if(rs.next()) {
			String nom= rs.getString("nom");
			String con = rs.getString("con");
			String dni = rs.getString("dni");
			long fnac = rs.getLong("fnac");
			float puntos = rs.getFloat("puntos");
			Cliente c = new Cliente(nom, con, dni, fnac, puntos);
			lc.add(c);
		}
		rs.close();
		st.close();
		return lc;
		//Cliente(nom String, con String, dni String,fnac bigint,puntos String)
	}
	
	/**
	 * Metodo que obtiene el cliente de la base de datos
	 * @param nom nombre del cliente
	 * @return Devuelve el cliente q
	 */
	public static Cliente ObtenerCliente(String dni) throws SQLException{
		
		Statement statement = con.createStatement();
		String sent = "SELECT * FROM Cliente WHERE dni='"+dni+"'";
		ResultSet rs = statement.executeQuery(sent);
		Cliente c = null;
		if(rs.next()) {
			String con = rs.getString("con");
			String nom = rs.getString("nom");
			long fecha = rs.getLong("fnac");
			float puntos = rs.getFloat("puntos");
			 c = new Cliente(nom, con, dni, fecha,puntos);
		}
		rs.close();
		logger.log(Level.INFO, "Cliente obtenido");
		return c;
	}
	
	/**
	 * Metodo que obtiene el cliente de la base de datos
	 * @param nom nombre del cliente
	 * @param pas contraseña del cliente
	 * @return Devuelve el cliente q
	 */
	public static Cliente ObtenerCliente(String nom,String pas) throws SQLException{
		
		Statement statement = con.createStatement();
		String sent = "SELECT * FROM Cliente WHERE nom='"+nom+"' AND con='"+pas+"'";
		ResultSet rs = statement.executeQuery(sent);
		Cliente c = null;
		if(rs.next()) {
			String dni = rs.getString("dni");
			long fecha = rs.getLong("fnac");
			float puntos = rs.getFloat("puntos");
			 c = new Cliente(nom, pas, dni, fecha,puntos);
		}
		rs.close();
		logger.log(Level.INFO, "Cliente obtenido");
		return c;
	}
	/**
	 * Método que elimina el cliente de la tabla de clientes de la base de datos
	 * @param dni Dni del cliente que ha introducido al iniciar sesión
	 */
	public static void eliminarCliente(String dni) throws SQLException {
		Statement stmnt = con.createStatement();
		String s = "DELETE FROM CLIENTE WHERE ID = " + dni;
		stmnt.executeUpdate(s);
		logger.log(Level.INFO, "EL cliente ha sido eliminado de la base de datos");
	}
	
	/**
	 * Método que modifica un cliente en la bd
	 * @param c Cliente que se quiere modificar
	 */
	public static void modificarCliente(Cliente c) throws SQLException {
		Statement st = con.createStatement();
		String sent= "update Cliente set nom="+c.getNom()+",con="+c.getCon()+",fnac="+c.getFechanac()+" where dni="+c.getDni();
		st.executeUpdate(sent);
				
	}
	
}