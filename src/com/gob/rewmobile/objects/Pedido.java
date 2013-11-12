package com.gob.rewmobile.objects;

import java.util.ArrayList;

public class Pedido {

	private String mesa;
	private int id;
	private String cajero;
	private String mozo;
	private ArrayList<Producto> producto;
	private int pax;

	public Pedido() {
		producto = new ArrayList<Producto>();
	}

	public Pedido(String mesa, int id, String cajero, String mozo, ArrayList<Producto> producto, int pax) {
		this.id = id;
		this.cajero = cajero;
		this.mozo = mozo;
		this.producto = producto;
		this.pax = pax;
	}

	public String getMesa() {
		return mesa;
	}

	public void setMesa(String mesa) {
		this.mesa = mesa;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCajero() {
		return cajero;
	}

	public void setCajero(String cajero) {
		this.cajero = cajero;
	}

	public String getMozo() {
		return mozo;
	}

	public void setMozo(String mozo) {
		this.mozo = mozo;
	}

	public ArrayList<Producto> getProducto() {
		return producto;
	}

	public void setProducto(ArrayList<Producto> producto) {
		this.producto = producto;
	}

	public int getPax() {
		return pax;
	}

	public void setPax(int pax) {
		this.pax = pax;
	}

	public Double getTotal() {
		Double total = 0.0;
		for (Producto producto : this.producto) {
			total += producto.getCantidad() * producto.getPrecio();
		}
		return total;
	}
	
	public ArrayList<Producto> getProductoByDestino(Destino destino) {
		ArrayList<Producto> productos = new ArrayList<Producto>();
		for (Producto obj : getProducto()) {
			if (obj.getDestino()==destino.getId() && !obj.isEnviado()) {
				productos.add(obj);
			}
		}
		return productos;
	}

}
