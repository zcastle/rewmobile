package com.ob.rewmobile.task;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import com.ob.rewmobile.R;
import com.ob.rewmobile.AccesoActivity;
import com.ob.rewmobile.model.Caja;
import com.ob.rewmobile.util.Data;
import com.ob.rewmobile.util.DialogCarga;
import com.ob.rewmobile.util.Globals;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class LoadDataTask extends AsyncTask<Void, Void, Boolean> {

	private Context context;
	private ProgressDialog pd;
	private boolean force;

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
			data.loadEquipo("TABLET01", force);
			data.loadUsuarios(force);
			data.loadDestinos(force);
			data.loadCategorias(force);
			data.loadProductos(force);
			data.loadUbigeo(force);
			data.loadTarjetas();
			//data.loadClientes();
			Caja equipo = Data.cajaController.getCaja();
			Globals.VA_IGV = equipo.getCentroCosto().getEmpresa().getIgv();
			Globals.VA_SERV = equipo.getServicio();
			Globals.VA_TC = equipo.getTc();
			Globals.MODULO = equipo.getTipo().equals(Globals.MODULO_CAJA) ? Globals.MODULO_CAJA : Globals.MODULO_PEDIDO;
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
		if (success) {
			context.startActivity(new Intent(context, AccesoActivity.class));
			((Activity) context).overridePendingTransition(R.animator.slide_in, R.animator.slide_out);
			((Activity) context).finish();
		}
		if (pd.isShowing()) pd.dismiss();
	}

	@Override
	protected void onCancelled() {
		if (pd.isShowing()) pd.dismiss();
	}
}
