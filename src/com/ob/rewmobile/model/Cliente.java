package com.ob.rewmobile.model;

public class Cliente {

	private String ruc;
	private String razonSocial;
	private String direccion;
	private Distrito distrito;
	
	public Cliente() {}

	public Cliente(String ruc, String razonSocial, String direccion, Distrito distrito) {
		super();
		this.ruc = ruc;
		this.razonSocial = razonSocial;
		this.direccion = direccion;
		this.distrito = distrito;
	}

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public Distrito getDistrito() {
		return distrito;
	}

	public void setDistrito(Distrito distrito) {
		this.distrito = distrito;
	}

}
