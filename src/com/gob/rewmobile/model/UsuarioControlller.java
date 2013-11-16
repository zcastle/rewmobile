package com.gob.rewmobile.model;

import java.util.ArrayList;

public class UsuarioControlller {

	private ArrayList<Usuario> usuarios;
	
	public UsuarioControlller() {
		usuarios = new ArrayList<Usuario>();
	}

	public UsuarioControlller(ArrayList<Usuario> usuarios) {
		super();
		this.usuarios = usuarios;
	}

	public ArrayList<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(ArrayList<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	
	public Usuario getUsuarioById(int id) {
		Usuario usuario = null;
		for (Usuario obj: getUsuarios()) {
			if (obj.getId()==id) {
				usuario = obj;
				break;
			}
		}
		return usuario;
	}
	
	public ArrayList<Usuario> getUsuariosByRol(String rol) {
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
		for (Usuario obj : getUsuarios()) {
			if (obj.getRol().equals(rol)) {
				usuarios.add(obj);
			}
		}
		return usuarios;
	}
}
