package com.gob.rewmobile.model;

public class Categoria {

	private int id;
	private String codigo;
	private String Nombre;
	
	public Categoria() {}
	
	public Categoria(String codigo, String nombre) {
		super();
		this.codigo = codigo;
		this.Nombre = nombre.toUpperCase();
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
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

	@Override
	public String toString() {
		return this.Nombre;
	}
	
}
