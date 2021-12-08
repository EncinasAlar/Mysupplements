package Clases;

public class Producto{
	
	private String cod;
	private float precio;
	private String nombre;
	
	private TreeSet<Producto> conjuntoProductos;
	
	public Producto(String codigo_Producto, float precio, String nom) {
		super();
		this.codigo_producto = codigo_Producto;
		this.precio = precio;
		this.nombre = nom;
	}
	public Producto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getCodigo_Producto() {
		return codigo_producto;
	}
	public void setCodigo_Producto(String codigo_Producto) {
		this.codigo_producto = codigo_Producto;
	}
	public float getPrecio() {
		return precio;
	}
	public void setPrecio(float precio) {
		this.precio = precio;
	}
	public String getNom() {
		return nombre;
	}
	public void setNom(String nom) {
		this.nombre = nom;
	}
	@Override
	public String toString() {
		return "Producto [nom=" + nombre + ", precio=" + precio + "]";
	}
	
}

