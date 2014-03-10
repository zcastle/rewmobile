package com.ob.rewmobile.model;

public class Caja {

	private int id = 0;
	private String nombre;
	private String tipo;
	private CentroCosto centroCosto;
	private String serieBoleta;
	private int numeroBoleta;
	private String serieFactura;
	private int numeroFactura;
	private String impresoraP;
	private String impresoraB;
	private String impresoraF;
	private Double tc;
	private Double servicio;
	private int dia;

	public Caja() {}
	
	public Caja(int id, String nombre, String tipo, CentroCosto centroCosto, String serieBoleta, int numeroBoleta, String serieFactura, int numeroFactura, String impresoraP, String impresoraB, String impresoraF, Double tc, Double servicio, int dia) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.tipo = tipo;
		this.centroCosto = centroCosto;
		this.serieBoleta = serieBoleta;
		this.numeroBoleta = numeroBoleta;
		this.serieFactura = serieFactura;
		this.numeroFactura = numeroFactura;
		this.impresoraP = impresoraP;
		this.impresoraB = impresoraB;
		this.impresoraF = impresoraF;
		this.tc = tc;
		this.servicio = servicio;
		this.dia = dia;
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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public CentroCosto getCentroCosto() {
		return centroCosto;
	}

	public void setCentroCosto(CentroCosto centroCosto) {
		this.centroCosto = centroCosto;
	}

	public String getSerieBoleta() {
		return serieBoleta;
	}

	public void setSerieBoleta(String serieBoleta) {
		this.serieBoleta = serieBoleta;
	}

	public int getNumeroBoleta() {
		return numeroBoleta;
	}

	public void setNumeroBoleta(int numeroBoleta) {
		this.numeroBoleta = numeroBoleta;
	}

	public String getSerieFactura() {
		return serieFactura;
	}

	public void setSerieFactura(String serieFactura) {
		this.serieFactura = serieFactura;
	}

	public int getNumeroFactura() {
		return numeroFactura;
	}

	public void setNumeroFactura(int numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	public String getImpresoraP() {
		return impresoraP;
	}

	public void setImpresoraP(String impresoraP) {
		this.impresoraP = impresoraP;
	}

	public String getImpresoraB() {
		return impresoraB;
	}

	public void setImpresoraB(String impresoraB) {
		this.impresoraB = impresoraB;
	}

	public String getImpresoraF() {
		return impresoraF;
	}

	public void setImpresoraF(String impresoraF) {
		this.impresoraF = impresoraF;
	}

	public Double getTc() {
		return tc;
	}

	public void setTc(Double tc) {
		this.tc = tc;
	}

	public Double getServicio() {
		return servicio;
	}

	public void setServicio(Double servicio) {
		this.servicio = servicio;
	}

	public int getDia() {
		return dia;
	}

	public void setDia(int dia) {
		this.dia = dia;
	}
	
}
