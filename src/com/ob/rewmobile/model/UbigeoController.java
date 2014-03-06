package com.ob.rewmobile.model;

import java.util.ArrayList;

public class UbigeoController {

	private ArrayList<Departamento> departamentos;
	private ArrayList<Provincia> provincias;
	private ArrayList<Distrito> distritos;
	
	public UbigeoController() {
		departamentos = new ArrayList<Departamento>();
		provincias = new ArrayList<Provincia>();
		distritos = new ArrayList<Distrito>();
	}

	public UbigeoController(ArrayList<Departamento> departamentos,
			ArrayList<Provincia> provincias, ArrayList<Distrito> distritos) {
		super();
		this.departamentos = departamentos;
		this.provincias = provincias;
		this.distritos = distritos;
	}

	public ArrayList<Departamento> getDepartamentos() {
		return departamentos;
	}

	public void setDepartamentos(ArrayList<Departamento> departamentos) {
		this.departamentos = departamentos;
	}

	public ArrayList<Provincia> getProvincias() {
		return provincias;
	}

	public void setProvincias(ArrayList<Provincia> provincias) {
		this.provincias = provincias;
	}

	public ArrayList<Distrito> getDistritos() {
		return distritos;
	}

	public void setDistritos(ArrayList<Distrito> distritos) {
		this.distritos = distritos;
	}
	
	public Distrito getDistritoById(int id) {
		Distrito dis = null;
		for (Distrito distrito : getDistritos()) {
			if (distrito.getId()==id) {
				dis = distrito;
			}
		}
		return dis;
	}

}
