package com.ob.rewmobile.task;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.MenuItem;
import android.widget.TextView;

import com.fortysevendeg.android.swipelistview.SwipeListView;
import com.ob.rewmobile.R;
import com.ob.rewmobile.adapter.PedidoAdapter;
import com.ob.rewmobile.model.PedidoController;
import com.ob.rewmobile.model.Producto;
import com.ob.rewmobile.util.Data;

public class LoadPedidoTask extends AsyncTask<Void, Void, Boolean> {

	private Context context;
	private ProgressDialog pd;
	private PedidoController pedido;
	/*private SwipeListView listViewPedido;
	private TextView txtMontoTotalMesa;
	private MenuItem mnuItemMozoPax;*/

	public LoadPedidoTask(Context context, PedidoController pedido, SwipeListView listViewPedido, TextView txtMontoTotalMesa, MenuItem mnuItemMozoPax) {
		this.context = context;
		this.pedido = pedido;
		/*this.listViewPedido = listViewPedido;
		this.txtMontoTotalMesa = txtMontoTotalMesa;
		this.mnuItemMozoPax = mnuItemMozoPax;*/
	}
	
	public LoadPedidoTask(Context context, PedidoController pedido) {
		this.context = context;
		this.pedido = pedido;
		/*this.listViewPedido = listViewPedido;
		this.txtMontoTotalMesa = txtMontoTotalMesa;
		this.mnuItemMozoPax = mnuItemMozoPax;*/
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pd = new ProgressDialog(context);
		pd.setTitle(R.string.procesando);
		pd.setCancelable(false);
		pd.setMessage("Cargando Pedido...");
		pd.show();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			pedido.setProductos(new ArrayList<Producto>());
			new Data(context).loadPedido(pedido);
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
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean success) {
		if (success) {
			/*listViewPedido.setAdapter(new PedidoAdapter(context, pedido, txtMontoTotalMesa));
			txtMontoTotalMesa.setText("TOTAL S/. " + pedido.getTotal());
			((Activity) context).getActionBar().setSubtitle("Mesa ".concat(pedido.getMesa()));
			mnuItemMozoPax.setTitle(pedido.getMozo().getNombre().concat(" - ").concat(pedido.getPax()+" PAX"));*/
		}
		pd.dismiss();
	}

	@Override
	protected void onCancelled() {
		pd.dismiss();
	}

}
