package com.ob.rewmobile.model;

public class Distrito {

	private int id;
	private String codigo;
	private String nombre;
	private Provincia provincia;
	
	public Distrito() {}

	public Distrito(int id, String codigo, String nombre, Provincia provincia) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.nombre = nombre;
		this.provincia = provincia;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Provincia getProvincia() {
		return provincia;
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

	@Override
	public String toString() {
		return nombre;
	}

}
