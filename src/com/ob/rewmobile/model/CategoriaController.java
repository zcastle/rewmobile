package com.ob.rewmobile.model;

import java.util.ArrayList;

public class CategoriaController {

	private ArrayList<Categoria> categorias;
	
	public CategoriaController() {
		categorias = new ArrayList<Categoria>();
	}

	public CategoriaController(ArrayList<Categoria> categorias) {
		super();
		this.categorias = categorias;
	}

	public void setCategorias(ArrayList<Categoria> categorias) {
		this.categorias = categorias;
	}

	public ArrayList<Categoria> getCategorias() {
		return categorias;
	}
	
	public Categoria getCategoriaById(int id) {
		Categoria categoria = null;
		for (Categoria obj: getCategorias()) {
			if (obj.getId()==id) {
				categoria = obj;
				break;
			}
		}
		return categoria;
	}
	
	public Categoria getCategoriaByCodigo(String codigo) {
		Categoria categoria = null;
		for (Categoria obj: getCategorias()) {
			if (obj.getCodigo().equals(codigo)) {
				categoria = obj;
				break;
			}
		}
		return categoria;
	}

}
