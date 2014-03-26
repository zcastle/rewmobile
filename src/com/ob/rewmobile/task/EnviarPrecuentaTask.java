package com.ob.rewmobile.task;

import com.epson.eposprint.Print;
import com.ob.rewmobile.model.PedidoController;
import com.ob.rewmobile.util.DialogCarga;
import com.ob.rewmobile.util.PrintTicket;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class EnviarPrecuentaTask extends AsyncTask<Void, Void, Boolean> {

	private DialogCarga pd;
	private PedidoController pedido;
	private Context context;

	public EnviarPrecuentaTask(Context context, PedidoController pedido) {
		this.context = context;
		this.pedido = pedido;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pd = new DialogCarga(context, "Enviando Precuenta...");
		pd.show();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		int[] printerStatus = new int[1];
		PrintTicket printTicket = new PrintTicket(context, PrintTicket.TMU220, PrintTicket.PRECUENTA, this.pedido);
		printerStatus[0] = printTicket.printDoc();
		if ((printerStatus[0] & Print.ST_PRINT_SUCCESS) == Print.ST_PRINT_SUCCESS){
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onPostExecute(final Boolean success) {
		if (success) {
			Toast.makeText(context, "Precuenta Enviada", Toast.LENGTH_SHORT).show();
		}
		pd.dismiss();
	}

	@Override
	protected void onCancelled() {
		pd.dismiss();
	}

}
