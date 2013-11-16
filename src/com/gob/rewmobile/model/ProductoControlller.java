package com.gob.rewmobile.model;

import java.util.ArrayList;

import android.util.Log;

public class ProductoControlller {

	private ArrayList<Producto> productos;
	
	public ProductoControlller() {
		productos = new ArrayList<Producto>();
	}

	public ProductoControlller(ArrayList<Producto> productos) {
		super();
		this.productos = productos;
	}

	public ArrayList<Producto> getProductos() {
		return productos;
	}

	public void setProducto(ArrayList<Producto> productos) {
		this.productos = productos;
	}
	
	public ArrayList<Producto> getProductoByDestino(Destino destino) {
		ArrayList<Producto> productos = new ArrayList<Producto>();
		for (Producto obj : getProductos()) {
			if (obj.getDestino().equals(destino) && !obj.isEnviado()) {
				productos.add(obj);
			}
		}
		return productos;
	}
	
	public ArrayList<Producto> getProductoByCategoria(Categoria categoria) {
		ArrayList<Producto> productos = new ArrayList<Producto>();
		for (Producto obj : getProductos()) {
			Log.i("CAT2", categoria.toString());
			Log.i("CAT1", obj.getCategoria().toString());
			if (obj.getCategoria()==categoria) {
				productos.add(obj);
			}
		}
		return productos;
	}
	
	public ArrayList<Producto> getProductoContainName(String nombre) {
		ArrayList<Producto> productos = new ArrayList<Producto>();
		for (Producto p: getProductos()) {
			if(p.getNombre().contains(nombre)) {
				productos.add(p);
			}
		}
		return productos;
	}
}
