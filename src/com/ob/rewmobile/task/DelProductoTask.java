package com.ob.rewmobile.task;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ob.rewmobile.PedidoActivity;
import com.ob.rewmobile.model.Producto;
import com.ob.rewmobile.util.App;
import com.ob.rewmobile.util.Data;
import com.ob.rewmobile.util.DialogCarga;
import com.ob.rewmobile.util.Globals;

public class DelProductoTask extends AsyncTask<Void, Void, Boolean> {

	private Context context;
	private DialogCarga pd;
	private Producto producto;
	private boolean sync = false;

	public DelProductoTask(Context context, Producto producto, boolean sync) {
		this.context = context;
		this.producto = producto;
		this.sync = sync;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pd = new DialogCarga(context, "Removiendo Producto...");
		if(App.isPedido()) pd.show();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			new Data(context).deleteProducto(producto, sync);
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
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean success) {
		if (success) {
			if(producto.isEnviado()) {
				
			}
			((PedidoActivity)context).pedidoListener.refresh();
			//Toast.makeText(context, producto.getNombre().concat(" Removido"), Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context, Globals.SERVER_NO_CONNECTION_MESSAGE, Toast.LENGTH_LONG).show();
		}
		if (pd.isShowing()) pd.dismiss();
	}

	@Override
	protected void onCancelled() {
		if (pd.isShowing()) pd.dismiss();
	}

}
