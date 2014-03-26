package com.ob.rewmobile.model;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;

import com.ob.rewmobile.listener.PedidoListener;
import com.ob.rewmobile.util.Data;
import com.ob.rewmobile.util.Globals;


public class PedidoController {

	private String mesa;
	private int id;
	private Usuario cajero;
	private Usuario mozo = null;
	private ArrayList<Producto> productos;
	private int pax = 1;
	private boolean servicio = true;
	private Cliente cliente = null;
	private ArrayList<Pago> pagos = new ArrayList<Pago>();

	public PedidoController(String mesa) {
		this.productos = new ArrayList<Producto>();
		this.mesa = mesa;
	}

	public PedidoController(String mesa, int id, Usuario cajero, Usuario mozo, ArrayList<Producto> producto, int pax) {
		this.id = id;
		this.cajero = cajero;
		this.mozo = mozo;
		this.productos = producto;
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

	public Usuario getCajero() {
		return cajero;
	}

	public void setCajero(Usuario cajero) {
		this.cajero = cajero;
	}

	public Usuario getMozo() {
		return mozo;
	}

	public void setMozo(Usuario mozo) {
		this.mozo = mozo;
	}

	public ArrayList<Producto> getProductos() {
		return productos;
	}

	public void setProductos(ArrayList<Producto> producto) {
		this.productos = producto;
	}

	public int getPax() {
		return pax;
	}

	public void setPax(int pax) {
		this.pax = pax;
	}

	public boolean isServicio() {
		return servicio;
	}

	public void setServicio(Context context, boolean servicio) throws ClientProtocolException, JSONException, IOException {
		this.servicio = servicio;
		new Data(context).updatePedido(this, false);
	}

	public void setServicio(boolean servicio) throws ClientProtocolException, JSONException, IOException {
		this.servicio = servicio;
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public ArrayList<Pago> getPagos() {
		return pagos;
	}

	public void setPagos(ArrayList<Pago> pagos) {
		this.pagos = pagos;
	}
	
	public boolean isPagoCompleto() {
		//boolean success = false;
		double valorPagado = 0.0;
		for (Pago pago: getPagos())
			valorPagado += pago.getMoneda().equals(Globals.MONEDA_D) ? pago.getValor() * pago.getCambio() : pago.getValor();
		//if (valorPagado>=getTotal()) success = true;
		return valorPagado>=getTotal();
	}

	public Double getTotal() {
		Double total = 0.0;
		for (Producto producto : this.productos) {
			total += producto.getCantidad() * producto.getPrecio();
		}
		return total;
	}
	
	public ArrayList<Producto> getProductosByDestino(Destino destino) {
		ArrayList<Producto> productos = new ArrayList<Producto>();
		for (Producto obj : getProductos()) {
			if (obj.getDestino().getId()==destino.getId() && !obj.isEnviado()) {
				productos.add(obj);
			}
		}
		return productos;
	}
	
	public void updateEnviado() {
		for (Producto obj : getProductos()) {
			obj.setEnviado(true);
		}
	}
	
	public void removeProducto(Producto producto) {
		productos.remove(producto);
	}

}
