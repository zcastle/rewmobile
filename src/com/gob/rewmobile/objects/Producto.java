package com.gob.rewmobile.objects;

public class Producto {

	private int id;
	private int idAtencion;
	private int codigo;
	private String nombre;
	private Double precio;
	private Double cantidad = 1.0;
	private String mensaje = "";
	private int destino = 1;
	private boolean enviado = false;

	public Producto() {
	}

	public Producto(int id, int idAtencion, int codigo, String nombre, Double precio, Double cantidad, String mensaje, int destino) {
		super();
		this.id = id;
		this.idAtencion = idAtencion;
		this.codigo = codigo;
		this.nombre = nombre;
		this.precio = precio;
		this.cantidad = cantidad;
		this.mensaje = mensaje;
		this.destino = destino;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdAtencion() {
		return idAtencion;
	}

	public void setIdAtencion(int idAtencion) {
		this.idAtencion = idAtencion;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre.toUpperCase();
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Double getCantidad() {
		return cantidad;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}

	public Double getTotal() {
		return this.cantidad * this.precio;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public int getDestino() {
		return destino;
	}

	public void setDestino(int destino) {
		this.destino = destino;
	}

	public boolean isEnviado() {
		return enviado;
	}

	public void setEnviado(boolean enviado) {
		this.enviado = enviado;
	}

	@Override
	public String toString() {
		return this.nombre;
	}
}
