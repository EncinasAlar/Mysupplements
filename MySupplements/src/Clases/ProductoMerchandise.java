package Clases;

public class ProductoMerchandise extends Producto {
	
		
		
		private String material ;
		
		
		//EL CODIGO ES PAR
		
		public ProductoMerchandise(int codigo_Producto, float precio, String nom,String urlimagen,String material) {
			super(codigo_Producto, precio, nom,urlimagen);
			this.material = material;
		}
		
		@Override
		public String toString() {
			return  getNombre()+" Precio: "+getPrecio()+"€	Material"+material;
		}
		
		
		public String getMaterial() {
			return material;
		}
		public void setMaterial(String material) {
			this.material = material;
		}
		
		
}
