package com.ob.rewmobile.model;

import com.ob.rewmobile.util.Globals;
import com.ob.rewmobile.util.Util;

public class Usuario {

	private int id;
	private String nombre;
	private String clave;
	private int rol;
	
	public Usuario(){}
	
	public Usuario(int id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.rol = Globals.ROL_ADMIN;
	}
	
	public Usuario(int id, String nombre, String clave, int rol) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.clave = clave;
		this.rol = rol;
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

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public int getRol() {
		return rol;
	}

	public void setRol(int rol) {
		this.rol = rol;
	}
	
	public boolean isPasswordOk(String password) {
		return Util.getMD5(password).equals(getClave());
	}
	
	@Override
	public String toString() {
		return this.nombre;
	}
}
