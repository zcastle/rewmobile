package com.gob.rewmobile.objects;

import java.util.ArrayList;

public class Destinos {

	private ArrayList<Destino> destinos = new ArrayList<Destino>();
	
	public Destinos() {
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Destino> getDestinos() {
		return destinos;
	}
	
	public Destino getDestinoById(int id) {
		Destino destino = null;
		for (Destino obj: destinos) {
			if (obj.getId()==id) {
				destino = obj;
				break;
			}
		}
		return destino;
	}

}
