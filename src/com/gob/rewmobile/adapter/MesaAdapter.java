package com.gob.rewmobile.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gob.rewmobile.R;
import com.gob.rewmobile.objects.Mesa;

public class MesaAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater = null;
	private ArrayList<Mesa> mesas = null;

	public MesaAdapter(Context context, ArrayList<Mesa> mesas) {
		this.layoutInflater = LayoutInflater.from(context);
		this.mesas = mesas;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = this.layoutInflater.inflate(R.layout.layout_bloque, null);
		}
		
		TextView tv = (TextView) convertView.findViewById(R.id.textviewmea);

		Mesa mesa = getItem(position);
		//if (mesa.getStatus() == 1) {
			((Mesa) tv).setStatus(mesa.getStatus());
		//}
		tv.setText(mesa.getNombre());
		tv.setVisibility(View.VISIBLE);
		
		return convertView;
	}

	@Override
	public int getCount() {
		return mesas.size();
	}

	@Override
	public Mesa getItem(int position) {
		return mesas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	} // usuarios.get(position).getId()
}