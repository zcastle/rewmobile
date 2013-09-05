package com.gob.rewmobile.objects;

public class Producto {

	private int id;
	private int codigo;
	private String nombre;
	private Double precio;
	private Double cantidad;
	
	public Producto(){}
	
	public Producto(int id, int codigo, String nombre, Double cantidad, Double precio) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.nombre = nombre;
		this.precio = precio;
		this.cantidad = cantidad;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getCodigo() {
		return codigo;
	}
	
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Double getPrecio() {
		return precio;
	}
	
	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Double getCantidad() {
		return cantidad;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}

	public Double getTotal(){
		return this.cantidad * this.precio;
	}
	
	@Override
	public String toString() {
		return this.nombre;
	}

}
