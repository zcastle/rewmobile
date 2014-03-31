package com.ob.rewmobile.model;

import android.annotation.SuppressLint;
import java.util.Date;

public class Producto implements Cloneable {

	private int id;
	private int idAtencion;
	private String codigo;
	private String nombre;
	private Double precio;
	private Double cantidad = 1.0;
	private String mensaje = "";
	private Destino destino;
	private boolean enviado = false;
	private Categoria categoria;
	private Date sync;

	public Producto() {
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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	@SuppressLint("DefaultLocale")
	public void setNombre(String nombre) {
		this.nombre = nombre.toUpperCase();
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

	public Destino getDestino() {
		return destino;
	}

	public void setDestino(Destino destino) {
		this.destino = destino;
	}

	public boolean isEnviado() {
		return enviado;
	}

	public void setEnviado(boolean enviado) {
		this.enviado = enviado;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	@Override
	public String toString() {
		return this.nombre;
	}
	
	public Date getSync() {
		return sync;
	}

	public void setSync(Date sync) {
		this.sync = sync;
	}	

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}
