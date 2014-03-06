package com.ob.rewmobile.adapter;

import java.util.ArrayList;

import com.ob.rewmobile.R;
import com.ob.rewmobile.model.Producto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProductoAdapter extends BaseAdapter {

	protected LayoutInflater layoutInflater;
	protected ArrayList<Producto> productos;

	public ProductoAdapter(Context context, ArrayList<Producto> productos) {
		this.layoutInflater = LayoutInflater.from(context);
		this.productos = productos;
	}

	public View getView(int position, View convertView, final ViewGroup parent) {
		
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.layout_bloque, parent, false);
		}

		TextView tv = (TextView) convertView.findViewById(R.id.textviewproducto);
		Producto producto = getItem(position);
		tv.setText(producto.getNombre());
		tv.setVisibility(View.VISIBLE);
		return convertView;
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

}