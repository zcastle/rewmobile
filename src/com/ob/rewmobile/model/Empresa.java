package com.ob.rewmobile.model;

public class Empresa {

	private int id;
	private String codigo;
	private String ruc;
	private String razon;
	private String nombre;
	private String direccion;
	private String distrito;
	private Double igv;
	
	public Empresa() {}

	public Empresa(int id, String codigo, String ruc, String razon, String nombre, String direccion, String distrito) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.ruc = ruc;
		this.razon = razon;
		this.nombre = nombre;
		this.direccion = direccion;
		this.distrito = distrito;
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

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public String getRazon() {
		return razon;
	}

	public void setRazon(String razon) {
		this.razon = razon;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getDistrito() {
		return distrito;
	}

	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}

	public Double getIgv() {
		return igv;
	}

	public void setIgv(Double igv) {
		this.igv = igv;
	}
	
}
