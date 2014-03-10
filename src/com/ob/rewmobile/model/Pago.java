package com.ob.rewmobile.model;

import com.ob.rewmobile.util.Globals;

public class Pago {
	
	private String moneda = Globals.MONEDA;
	private Double valor = 0.0;
	private Double cambio = 0.0;
	private String tarjeta;
	
	public Pago() {
	}

	public Pago(String tarjeta, String moneda, Double valor, Double cambio) {
		super();
		this.moneda = moneda;
		this.valor = valor;
		this.cambio = cambio;
		this.tarjeta = tarjeta;
	}
	
	public Pago(String tarjeta, Double valor, Double cambio) {
		super();
		this.valor = valor;
		this.cambio = cambio;
		this.tarjeta = tarjeta;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Double getCambio() {
		return cambio;
	}

	public void setCambio(Double cambio) {
		this.cambio = cambio;
	}

	public String getTarjeta() {
		return tarjeta;
	}

	public void setTarjeta(String tarjeta) {
		this.tarjeta = tarjeta;
	}

}
