package com.ob.rewmobile.task;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ob.rewmobile.PedidoActivity;
import com.ob.rewmobile.model.Producto;
import com.ob.rewmobile.util.Data;
import com.ob.rewmobile.util.DialogCarga;

public class EditProductoTask extends AsyncTask<Void, Void, Boolean> {

	private DialogCarga pd;
	private Context context;
	private Producto producto;
	private boolean sync = false;

	public EditProductoTask(Context context, Producto producto, boolean sync) {
		this.context = context;
		this.producto = producto;
		this.sync = sync;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pd = new DialogCarga(context, "Editando Producto...");
		if (!sync) pd.show();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			return new Data(context).updateProducto(producto, sync);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	protected void onPostExecute(final Boolean success) {
		if (success) {
			PedidoActivity activity = (PedidoActivity) context;
			activity.getPedidoAdapter().notifyDataSetChanged();
			activity.getPedidoListener().refresh();
			Toast.makeText(context, "Producto Editado", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context, "No se a podido editar el producto", Toast.LENGTH_SHORT).show();
		}
		if (pd.isShowing()) pd.dismiss();
	}

	@Override
	protected void onCancelled() {
		if (pd.isShowing()) pd.dismiss();
	}

}
