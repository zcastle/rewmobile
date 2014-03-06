package com.ob.rewmobile.model;

public class CentroCosto {

	private int id;
	private String codigo;
	private String nombre;
	private String direccion;
	private String distrito;
	private Empresa empresa;
	
	public CentroCosto() {}

	public CentroCosto(int id, String codigo, String nombre, String direccion,
			String distrito, Empresa empresa) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.nombre = nombre;
		this.direccion = direccion;
		this.distrito = distrito;
		this.empresa = empresa;
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

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
}
