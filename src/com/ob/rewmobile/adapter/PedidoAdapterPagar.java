package com.ob.rewmobile.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ob.rewmobile.R;
import com.ob.rewmobile.model.PedidoController;
import com.ob.rewmobile.model.Producto;

public class PedidoAdapterPagar extends BaseAdapter {

	protected LayoutInflater layoutInflater;
	protected ArrayList<Producto> productos;

	public PedidoAdapterPagar(Context context, PedidoController pedido) {
		this.productos = pedido.getProductos();
		this.layoutInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return productos.size();
	}

	public Producto getItem(int i) {
		return productos.get(i);
	}

	public long getItemId(int i) {
		return (long) i;
	}

	public View getView(final int position, View convertView, final ViewGroup parent) {
		
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.pedido_item_pagar, parent, false);
		}

		Producto producto = getItem(position);
		
		TextView txtEstado, txtProducto, txtCantidad, txtUnidad, txtTotal;
		
		txtEstado = (TextView) convertView.findViewById(R.id.txtEstado);
		txtProducto = (TextView) convertView.findViewById(R.id.txtProducto);
		txtCantidad = (TextView) convertView.findViewById(R.id.txtCantidad);
		txtUnidad = (TextView) convertView.findViewById(R.id.txtUnitario);
		txtTotal = (TextView) convertView.findViewById(R.id.txtTotal);

		if (producto.isEnviado()) {
			txtEstado.setBackgroundResource(android.R.color.holo_orange_light);
		} else {
			txtEstado.setBackgroundResource(android.R.color.holo_green_light);
		}

		txtProducto.setText(producto.getNombre());
		txtCantidad.setText(producto.getCantidad().toString());
		txtUnidad.setText(producto.getPrecio().toString());
		txtTotal.setText(producto.getTotal().toString());

		return convertView;
	}

}