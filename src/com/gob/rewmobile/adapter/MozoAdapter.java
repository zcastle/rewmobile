package com.gob.rewmobile.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gob.rewmobile.R;
import com.gob.rewmobile.model.Usuario;

public class MozoAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater = null;
	private ArrayList<Usuario> usuarios = null;

	public MozoAdapter(Context context, ArrayList<Usuario> usuarios) {
		this.layoutInflater = LayoutInflater.from(context);
		this.usuarios = usuarios;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = this.layoutInflater.inflate(R.layout.layout_bloque, null);
		}
		
		TextView tv = (TextView) convertView.findViewById(R.id.textviewmozo);
		tv.setText(getItem(position).getNombre());
		tv.setVisibility(View.VISIBLE);

		return convertView;
	}

	@Override
	public int getCount() {
		return usuarios.size();
	}

	@Override
	public Usuario getItem(int position) {
		return usuarios.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	} // usuarios.get(position).getId()
}