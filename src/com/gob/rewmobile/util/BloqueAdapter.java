package com.gob.rewmobile.util;

import java.util.ArrayList;

import com.gob.rewmobile.R;
import com.gob.rewmobile.objects.BaseClass;
import com.gob.rewmobile.objects.MTableRow;
import com.gob.rewmobile.objects.Mesa;
import com.gob.rewmobile.objects.Producto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BloqueAdapter extends BaseAdapter {

	// private Context context = null;
	private LayoutInflater inflater = null;
	private ArrayList<?> baseClass;
	public static String ITEM_MOZO = "ITEM_MOZO";
	public static String ITEM_MESA = "ITEM_MESA";
	public static String ITEM_PRODUCTO = "ITEM_PRODUCTO";
	public static String ITEM_CATEGORIA = "ITEM_CATEGORIA";
	public static String ITEM_PEDIDO = "ITEM_PEDIDO";
	private String GRID_ITEM = null;

	public BloqueAdapter(Context context, ArrayList<?> baseClass, String item) {
		// this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.baseClass = baseClass;
		this.GRID_ITEM = item;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tv = null;
		MTableRow ll = null;
		if (convertView == null) {
			convertView = this.inflater.inflate(R.layout.layout_bloque, null);
			if (this.GRID_ITEM.equals(BloqueAdapter.ITEM_MOZO)) {
				tv = (TextView) convertView.findViewById(R.id.grid_item);
			} else if (this.GRID_ITEM.equals(BloqueAdapter.ITEM_MESA)) {
				tv = (Mesa) convertView.findViewById(R.id.grid_item_mesa);
				// tv = (TextView) getItem(position);
			} else if (this.GRID_ITEM.equals(BloqueAdapter.ITEM_PRODUCTO)) {
				tv = (TextView) convertView
						.findViewById(R.id.grid_item_producto);
			} else if (this.GRID_ITEM.equals(BloqueAdapter.ITEM_CATEGORIA)) {
				tv = (TextView) convertView
						.findViewById(R.id.list_item_categoria);
			} else if (this.GRID_ITEM.equals(BloqueAdapter.ITEM_PEDIDO)) {
				ll = (MTableRow) convertView
						.findViewById(R.id.list_item_pedido);
			} else {
				tv = (TextView) convertView.findViewById(R.id.grid_item);
			}

			if (this.GRID_ITEM.equals(BloqueAdapter.ITEM_PEDIDO)) {
				convertView.setTag(ll);
			} else {
				convertView.setTag(tv);
			}
		} else {
			if (this.GRID_ITEM.equals(BloqueAdapter.ITEM_PEDIDO)) {
				ll = (MTableRow) convertView.getTag();
			} else {
				tv = (TextView) convertView.getTag();
			}
		}

		if (this.GRID_ITEM.equals(BloqueAdapter.ITEM_MESA)) {
			Mesa mesa = (Mesa) getItem(position);
			if (mesa.getStatus() == 1) {
				((Mesa) tv).setStatus(1);
			}
			tv.setText(mesa.getName());
			tv.setVisibility(View.VISIBLE);
		} else if (this.GRID_ITEM.equals(BloqueAdapter.ITEM_PRODUCTO)) {
			Producto producto = (Producto) getItem(position);
			tv.setText(producto.getNombre());
			tv.setVisibility(View.VISIBLE);
		} else if (this.GRID_ITEM.equals(BloqueAdapter.ITEM_PEDIDO)) {
			Producto producto = (Producto) getItem(position);
			ll.setProducto(producto);
			ll.setVisibility(View.VISIBLE);
		} else {
			BaseClass baseClass = (BaseClass) getItem(position);
			tv.setText(baseClass.getName());
			tv.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	@Override
	public int getCount() {
		return baseClass.size();
	}

	@Override
	public Object getItem(int position) {
		return baseClass.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	} // return baseClass.get(position).getId();
}