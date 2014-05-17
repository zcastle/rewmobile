package com.ob.rewmobile.task;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ob.rewmobile.PedidoActivity;
import com.ob.rewmobile.model.PedidoController;
import com.ob.rewmobile.model.Producto;
import com.ob.rewmobile.util.App;
import com.ob.rewmobile.util.Data;
import com.ob.rewmobile.util.DialogCarga;
import com.ob.rewmobile.util.Globals;

public class AddProductoTask extends AsyncTask<Void, Void, Boolean> {

	private DialogCarga pd;
	private PedidoController pedido;
	private Context context;
	private Producto producto;
	private boolean sync = false;

	public AddProductoTask(Context context, PedidoController pedido, Producto producto, boolean sync) {
		this.context = context;
		this.pedido = pedido;
		this.producto = producto;
		this.sync = sync;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pd = new DialogCarga(context, "Añadiendo Producto...");
		if(App.isPedido()) pd.show();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			producto.setIdAtencion(new Data(context).insertPedido(pedido, producto, sync));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	protected void onPostExecute(final Boolean success) {
		if (success) {
			PedidoActivity activity = (PedidoActivity) context;
			if(producto.getIdAtencion()>0) {
				activity.getPedidoAdapter().addItem(producto);
				activity.getPedidoListener().refresh();
				Toast.makeText(context, "Producto Añadido", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(context, "No se a podido añadir el producto", Toast.LENGTH_SHORT).show();
			}
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
