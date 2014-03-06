package com.ob.rewmobile.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ob.rewmobile.R;
import com.ob.rewmobile.model.Categoria;

public class CategoriasAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater = null;
	private ArrayList<Categoria> categorias = null;

	public CategoriasAdapter(Context context, ArrayList<Categoria> categorias) {
		this.layoutInflater = LayoutInflater.from(context);
		this.categorias = categorias;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = this.layoutInflater.inflate(R.layout.layout_bloque, null);
		}
		
		TextView tv = (TextView) convertView.findViewById(R.id.textviewcategoria);
		tv.setText(getItem(position).getNombre());
		tv.setVisibility(View.VISIBLE);

		return convertView;
	}

	@Override
	public int getCount() {
		return categorias.size();
	}

	@Override
	public Categoria getItem(int position) {
		return categorias.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	} // return baseClass.get(position).getId();
}