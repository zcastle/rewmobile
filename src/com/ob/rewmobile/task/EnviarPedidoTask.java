package com.ob.rewmobile.task;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.epson.eposprint.Print;
import com.fortysevendeg.android.swipelistview.SwipeListView;
import com.ob.rewmobile.adapter.PedidoAdapter;
import com.ob.rewmobile.model.Destino;
import com.ob.rewmobile.model.PedidoController;
import com.ob.rewmobile.model.Producto;
import com.ob.rewmobile.util.Data;
import com.ob.rewmobile.util.DialogCarga;
import com.ob.rewmobile.util.PrintTicket;

public class EnviarPedidoTask extends AsyncTask<Void, Void, Boolean> {

	private Context context;
	private DialogCarga pd;
	private PedidoController PEDIDO;
	private SwipeListView listViewPedido;
	private TextView txtMontoTotalMesa;

	public EnviarPedidoTask(Context context, PedidoController PEDIDO, SwipeListView listViewPedido, TextView txtMontoTotalMesa) {
		this.context = context;
		this.PEDIDO = PEDIDO;
		this.listViewPedido = listViewPedido;
		this.txtMontoTotalMesa = txtMontoTotalMesa;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pd = new DialogCarga(context, "Enviando Pedido...");
		pd.show();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		PedidoController pedido = new PedidoController(this.PEDIDO.getMesa());
		boolean rpta = true;
		Data data = null;
		for (Destino destino: Data.destinoController.getDestinos()) {
			pedido.setMozo(this.PEDIDO.getMozo());
			ArrayList<Producto> productos = pedido.getProductosByDestino(destino);
			if(productos.size()>0) {
				pedido.setProductos(productos);
				int[] printerStatus = new int[1];
				PrintTicket printTicket = new PrintTicket(context, PrintTicket.TMU220, PrintTicket.ENVIO, destino, pedido);
				printerStatus[0] = printTicket.printDoc();
	    		if ((printerStatus[0] & Print.ST_PRINT_SUCCESS) == Print.ST_PRINT_SUCCESS){
	    			final JSONObject obj = new JSONObject();
					try {
						obj.put("mesa", pedido.getMesa());
						obj.put("destino", destino.getId());
						data = new Data(context);
						data.updateEnvio(pedido, destino, false);
						pedido.updateEnviado();
					} catch (JSONException e) {
						rpta = false;
					} catch (IOException e) {
						rpta = false;
					} catch (Exception e) {
						rpta = false;
					}
	    			rpta = true;
	    		} else {
	    			rpta = false;
	    		}
			}
		}
		try {
			data.loadPedido(pedido);
		} catch (IOException e) {
			rpta = false;
		} catch (Exception e) {
			rpta = false;
		}
		return rpta;
	}

	@Override
	protected void onPostExecute(Boolean success) {
		if (success) {
			listViewPedido.setAdapter(new PedidoAdapter(context, PEDIDO));
			((PedidoAdapter)listViewPedido.getAdapter()).notifyDataSetChanged();
			txtMontoTotalMesa.setText("TOTAL S/. " + PEDIDO.getTotal());
			Toast.makeText(context, "Pedido Enviado", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context, "Pedido No Enviado", Toast.LENGTH_SHORT).show();
		}
		pd.dismiss();
	}

	@Override
	protected void onCancelled() {
		pd.dismiss();
	}
}
