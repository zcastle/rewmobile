package com.ob.rewmobile.model;

import java.util.ArrayList;

public class Provincia {

	private int id;
	private String codigo;
	private String nombre;
	private Departamento departamento;
	private ArrayList<Distrito> distritos;
	
	public Provincia() {
		distritos = new ArrayList<Distrito>();
	}

	public Provincia(int id, String codigo, String nombre, Departamento departamento, ArrayList<Distrito> distritos) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.nombre = nombre;
		this.departamento = departamento;
		this.distritos = distritos;
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

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public ArrayList<Distrito> getDistritos() {
		return distritos;
	}

	public void setDistritos(ArrayList<Distrito> distritos) {
		this.distritos = distritos;
	}
	
	@Override
	public String toString() {
		return nombre;
	}

}
