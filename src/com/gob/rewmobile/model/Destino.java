package com.gob.rewmobile.model;

public class Destino {

	private int id;
	private String nombre;
	private String ip;
	
	public Destino() {
	}

	public Destino(int id, String nombre, String ip) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.ip = ip;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@Override
	public String toString() {
		return this.nombre;
	}

}
