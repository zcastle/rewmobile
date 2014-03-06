package com.ob.rewmobile.model;

public class EquipoController {

	private Equipo equipo;
	
	public Equipo getEquipo() {
		return equipo;
	}

	public void setEquipo(Equipo equipo) {
		this.equipo = equipo;
	}

	public EquipoController() {
		this.equipo = new Equipo();
	}
	
	public EquipoController(Equipo equipo){
		this.equipo = equipo;
	}
	
	public void incrementaBoleta(){
		this.equipo.setNumeroBoleta(this.equipo.getNumeroBoleta()+1);
	}
	
	public void incrementaFactura(){
		this.equipo.setNumeroFactura(this.equipo.getNumeroFactura()+1);
	}

}
