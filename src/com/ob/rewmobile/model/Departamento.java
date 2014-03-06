package com.ob.rewmobile.model;

import java.util.ArrayList;

public class Departamento {

	private int id;
	private String codigo;
	private String nombre;
	private ArrayList<Provincia> provincias;
	
	public Departamento() {
		provincias = new ArrayList<Provincia>();
	}

	public Departamento(int id, String codigo, String nombre,ArrayList<Provincia> provincias) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.nombre = nombre;
		this.provincias = provincias;
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

	public ArrayList<Provincia> getProvincias() {
		return provincias;
	}

	public void setProvincias(ArrayList<Provincia> provincias) {
		this.provincias = provincias;
	}
	
	@Override
	public String toString() {
		return nombre;
	}

}
