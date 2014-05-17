package com.ob.rewmobile.model;

import java.util.ArrayList;

import android.util.Log;

public class ProductoController {

	private ArrayList<Producto> productos;
	
	public ProductoController() {
		productos = new ArrayList<Producto>();
	}

	public ProductoController(ArrayList<Producto> productos) {
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
		Log.i("CATE01", categoria.getId()+"");
		for (Producto obj : getProductos()) {
			//Log.i("CATE02", obj.getNombre());
			//Log.i("CATE02", obj.getCategoria().getId()+"");
			if (obj.getCategoria()!=null) {
				if (obj.getCategoria().getId()==categoria.getId()) {
					Log.i("CATE03", obj.getNombre()+" ADD");
					productos.add(obj);
				}
			}
		}
		return productos;
	}
	
	public ArrayList<Producto> getProductoContainName(String nombre) {
		ArrayList<Producto> productos = new ArrayList<Producto>();
		nombre = nombre.toUpperCase();
		for (Producto p: getProductos()) {
			if(p.getNombre().contains(nombre) || p.getCodigo().equals(nombre)) {
				//Log.e("ERROR", p.getCodigo());
				productos.add(p);
			}
		}
		return productos;
	}
	
	public Destino getDestinoByProducto(Producto producto) {
		Destino destino = null;
		for (Producto p: getProductos()) {
			if(p.getId()==producto.getId()) {
				destino = p.getDestino();
				break;
			}
		}
		return destino;
	}
}
