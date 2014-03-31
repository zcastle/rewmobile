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

public class DelProductoTask extends AsyncTask<Void, Void, Boolean> {

	private Context context;
	private DialogCarga pd;
	private PedidoController PEDIDO;
	private boolean sync = false;

	public DelProductoTask(Context context, PedidoController PEDIDO, boolean sync) {
		this.context = context;
		this.PEDIDO = PEDIDO;
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
			for (Producto producto: PEDIDO.getProductos()) {
				new Data(context).deleteProducto(producto, sync);
			}
			
			/*int[] printerStatus = new int[1];
			PrintTicket printTicket = new PrintTicket(context, PrintTicket.TMU220, PrintTicket.ELIMINAR_ENVIO, PEDIDO);
			printerStatus[0] = printTicket.printDoc();
			if ((printerStatus[0] & Print.ST_PRINT_SUCCESS) == Print.ST_PRINT_SUCCESS){
				
    		}*/
			
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
			PedidoActivity activity = (PedidoActivity)context;
			activity.getPedidoAdapter().removeItem(PEDIDO);
			activity.getPedidoListener().refresh();
			Toast.makeText(context, "Producto Removido", Toast.LENGTH_SHORT).show();
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
