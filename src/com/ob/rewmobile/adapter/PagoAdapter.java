package com.ob.rewmobile.adapter;

import java.util.ArrayList;

import com.ob.rewmobile.R;
import com.ob.rewmobile.model.Pago;
import com.ob.rewmobile.model.PedidoController;
import com.ob.rewmobile.model.Producto;
import com.ob.rewmobile.util.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class PagoAdapter extends BaseAdapter {
	
	private Context context;
	private PedidoController pedido;
	private ArrayList<Pago> pagos;
	private LayoutInflater layoutInflater;
	
	public PagoAdapter(Context context, PedidoController pedido) {
		this.context = context;
		this.pedido = pedido;
		this.pagos = pedido.getPagos();
		this.layoutInflater = LayoutInflater.from(context);
	}

	public void addItem(Pago pago) {
		this.pagos.add(pago);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return pagos.size();
	}

	@Override
	public Pago getItem(int i) {
		return pagos.get(i);
	}

	@Override
	public long getItemId(int i) {
		return (long) i;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.pago_item, parent, false);
		}
		
		Pago pago = getItem(position);
		
		TextView txtTarjeta = (TextView) convertView.findViewById(R.id.txtTarjeta);
		TextView txtValor = (TextView) convertView.findViewById(R.id.txtValor);
		TextView txtTipoCambio = (TextView) convertView.findViewById(R.id.txtTipoCambio);
		
		txtTarjeta.setText(pago.getTarjeta());
		txtValor.setText(Util.format(pago.getValor()));
		txtTipoCambio.setText("0.00");
		
		return convertView;
	}

}
