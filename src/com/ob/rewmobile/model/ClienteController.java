package com.ob.rewmobile.model;

import java.util.ArrayList;

import android.content.Context;

import com.ob.rewmobile.util.Data;

public class ClienteController {
	
	private ArrayList<Cliente> clientes;
	
	public ClienteController() {
		clientes = new ArrayList<Cliente>();
	}

	public ClienteController(ArrayList<Cliente> clientes) {
		super();
		this.clientes = clientes;
	}

	public ArrayList<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(ArrayList<Cliente> clientes) {
		this.clientes = clientes;
	}
	
	public Cliente getClienteByRuc(String ruc) {
		Cliente cliente = null;
		for (Cliente cl : getClientes()) {
			if (cl.getRuc().equals(ruc)) {
				cliente = cl;
				break;
			}
		}
		return cliente;
	}
	
	public ArrayList<Cliente> getClienteContainRazon(String razon) {
		ArrayList<Cliente> clientes = new ArrayList<Cliente>();
		for (Cliente cl : getClientes()) {
			if (cl.getRuc().contains(razon)) {
				clientes.add(cl);
			}
		}
		return clientes;
	}
	
	public void addCliente(Context context, Cliente cliente) {
		clientes.add(cliente);
		new Data(context).addCliente(cliente);
	}

}
