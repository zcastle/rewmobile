package com.ob.rewmobile.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.ob.rewmobile.R;

public class DialogCarga extends ProgressDialog {

	private String mensaje;
	private TextView txtMensaje;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_dialog_carga);
		txtMensaje = (TextView) findViewById(R.id.txtMensaje);
		txtMensaje.setText(mensaje);
	}

	public DialogCarga(Context context, String mensaje) {
		super(context);
		setCancelable(false);
		this.mensaje = mensaje;
		/*view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_carga, null);
		TextView txtMensaje = (TextView) view.findViewById(R.id.txtMensaje);
		txtMensaje.setText(msg);*/
		
		/*setTitle(R.string.procesando);
		setMessage(msg);*/
	}

	@Override
	public void show() {
		super.show();
	}
	
}
