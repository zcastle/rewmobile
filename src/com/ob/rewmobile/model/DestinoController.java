package com.ob.rewmobile.model;

import java.util.ArrayList;


public class DestinoController {

	private ArrayList<Destino> destinos;
	
	public DestinoController() {
		destinos = new ArrayList<Destino>();
	}

	public DestinoController(ArrayList<Destino> destinos) {
		super();
		this.destinos = destinos;
	}

	public void setDestinos(ArrayList<Destino> destinos) {
		this.destinos = destinos;
	}

	public ArrayList<Destino> getDestinos() {
		return destinos;
	}
	
	public Destino getDestinoById(int id) {
		Destino destino = null;
		for (Destino obj: getDestinos()) {
			if (obj.getId()==id) {
				destino = obj;
				break;
			}
		}
		return destino;
	}

}
