package com.ob.rewmobile.task;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.ob.rewmobile.AccesoActivity;
import com.ob.rewmobile.MainActivity;
import com.ob.rewmobile.R;
import com.ob.rewmobile.model.Caja;
import com.ob.rewmobile.util.App;
import com.ob.rewmobile.util.Data;
import com.ob.rewmobile.util.DialogCarga;
import com.ob.rewmobile.util.Globals;

public class LoadDataTask extends AsyncTask<Void, Void, Boolean> {

	private Context context;
	private DialogCarga pd;
	private boolean force;
	private Caja equipo;

	public LoadDataTask(Context context, boolean force) {
		this.context = context;
		this.force = force;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pd = new DialogCarga(context, "Cargando Data...");
		pd.show();
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			Data data = new Data(context);
			data.loadEquipo(App.DEVICE_NAME, force);
			equipo = Data.cajaController.getCaja();
			Globals.MODULO = equipo.getTipo().equals(Globals.MODULO_CAJA) ? Globals.MODULO_CAJA : Globals.MODULO_PEDIDO;
			if(App.isPedido()) {
				Log.e("IP_HOST", Globals.IP_HOST);
				InetAddress in = InetAddress.getByName(Globals.IP_HOST);
				if (!in.isReachable(5000)) {
	                return false;
	            }
			}
			data.loadUsuarios(force);
			data.loadDestinos(force);
			data.loadCategorias(force);
			data.loadProductos(force);
			data.loadUbigeo(force);
			data.loadTarjetas();
			//data.loadClientes();
		} catch (UnknownHostException e) {
			return false;
		} catch (ClientProtocolException e) {
			return false;
		} catch (URISyntaxException e) {
			return false;
		} catch (IOException e) {
			return false;
		} catch (JSONException e) {
			return false;
		} catch (Exception e) {
			//Log.e("ERROR", e.toString());
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	protected void onPostExecute(Boolean success) {
		MainActivity activity = (MainActivity) context;
		if (success) {
			Globals.VA_IGV = equipo.getCentroCosto().getEmpresa().getIgv();
			Globals.VA_SERV = equipo.getServicio();
			Globals.VA_TC = equipo.getTc();
			
			activity.startActivity(new Intent(activity, AccesoActivity.class));
			//activity.overridePendingTransition(R.animator.animation_enter, R.animator.animation_leave);
			activity.finish();
		} else {
			Toast.makeText(context, Globals.SERVER_NO_CONNECTION_MESSAGE, Toast.LENGTH_LONG).show();
			//activity.finish();
		}
		if (pd.isShowing()) pd.dismiss();
	}

	@Override
	protected void onCancelled() {
		if (pd.isShowing()) pd.dismiss();
	}
}
