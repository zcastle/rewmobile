package com.ob.rewmobile.task;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ob.rewmobile.PedidoActivity;
import com.ob.rewmobile.adapter.PedidoAdapter;
import com.ob.rewmobile.model.PedidoController;
import com.ob.rewmobile.util.Data;
import com.ob.rewmobile.util.DialogCarga;
import com.ob.rewmobile.util.Globals;

public class LoadPedidoTask extends AsyncTask<Void, Void, Boolean> {

	private Context context;
	private DialogCarga pd;
	private PedidoController PEDIDO;
	
	public LoadPedidoTask(Context context, PedidoController PEDIDO) {
		this.context = context;
		this.PEDIDO = PEDIDO;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pd = new DialogCarga(context, "Cargando Pedido...");
		pd.show();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			new Data(context).loadPedido(PEDIDO);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return false;
		} catch (URISyntaxException e) {
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
	protected void onPostExecute(Boolean success) {
		if (success) {
			PedidoActivity activity = (PedidoActivity) context;
			activity.swipeListView.setAdapter(new PedidoAdapter(context, PEDIDO));
			activity.pedidoListener.refresh();
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
