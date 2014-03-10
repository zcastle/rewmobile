package com.ob.rewmobile.model;

public class CajaController {

	private Caja caja;
	
	public CajaController() {
		this.caja = new Caja();
	}
	
	public CajaController(Caja equipo){
		this.caja = equipo;
	}
	
	public Caja getCaja() {
		return caja;
	}

	public void setCaja(Caja equipo) {
		this.caja = equipo;
	}
	
	public void incrementaBoleta(){
		this.caja.setNumeroBoleta(this.caja.getNumeroBoleta()+1);
	}
	
	public void incrementaFactura(){
		this.caja.setNumeroFactura(this.caja.getNumeroFactura()+1);
	}

}
