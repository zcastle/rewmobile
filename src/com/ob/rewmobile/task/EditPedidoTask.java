package com.ob.rewmobile.task;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ob.rewmobile.model.PedidoController;
import com.ob.rewmobile.util.Data;
import com.ob.rewmobile.util.DialogCarga;

public class EditPedidoTask extends AsyncTask<Void, Void, Boolean> {

	private Context context;
	private ProgressDialog pd;
	private PedidoController pedido;
	private boolean sync = false;

	public EditPedidoTask(Context context, PedidoController pedido, boolean sync) {
		this.context = context;
		this.pedido = pedido;
		this.sync = sync;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pd = new DialogCarga(context, "Editando Pedido...");
		if (!sync) pd.show();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			new Data(context).updatePedido(pedido, sync);
		} catch (ClientProtocolException e) {
			Toast.makeText(context, "Error en la Conexion...", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			return false;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean success) {
		if (success) {
			//Toast.makeText(context, "Pedido Editado", Toast.LENGTH_SHORT).show();
		}
		if (pd.isShowing()) pd.dismiss();
	}

	@Override
	protected void onCancelled() {
		if (pd.isShowing()) pd.dismiss();
	}

}
