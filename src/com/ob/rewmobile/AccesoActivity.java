package com.ob.rewmobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.ob.rewmobile.R;
import com.ob.rewmobile.adapter.MozoAdapter;
import com.ob.rewmobile.model.Usuario;
import com.ob.rewmobile.service.SyncService;
import com.ob.rewmobile.task.LoadDataTask;
import com.ob.rewmobile.util.Data;
import com.ob.rewmobile.util.Globals;

public class AccesoActivity extends Activity implements OnItemClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_acceso);

		GridView gridView = (GridView) findViewById(R.id.gridviewacceso);
		if (Globals.MODULO.equals(Globals.MODULO_CAJA)) {
			gridView.setAdapter(new MozoAdapter(this, Data.usuarioController.getUsuariosByRol(Globals.ROL_CAJA)));
		} else {
			gridView.setAdapter(new MozoAdapter(this, Data.usuarioController.getUsuariosByRol(Globals.ROL_MOZO)));
		}
		gridView.setOnItemClickListener(this);
		startService(new Intent(getBaseContext(), SyncService.class));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.acceso, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_salir:
			finish();
			break;
		case R.id.action_settings:
			Intent i = new Intent(this, ConfiguracionActivity.class);
			startActivity(i);
			finish();
			break;
		case R.id.action_recargar_productos:
			//new LoadDataTask(this, true).execute();
			break;
		}
		return true;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		final Usuario mozo = (Usuario) parent.getItemAtPosition(position);
		final Intent intent = new Intent(this, PedidoActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("mozo_id", mozo.getId());
		bundle.putString("mozo_name", mozo.getNombre());
		bundle.putString("mesa_name", "0");
		intent.putExtras(bundle);
		
		if (Globals.MODULO.equals(Globals.MODULO_CAJA)) {
			View viewDialogPassword = LayoutInflater.from(this).inflate(R.layout.layout_dialog_password, null);
			final EditText txtPassword = (EditText) viewDialogPassword.findViewById(R.id.password);
			TextView txtUsuarioAcceso = (TextView) viewDialogPassword.findViewById(R.id.txtUsuarioAcceso);
			txtUsuarioAcceso.setText(mozo.getNombre());
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setView(viewDialogPassword);
			builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					if (mozo.isPasswordOk(txtPassword.getText().toString())) {
						startActivity(intent);
						overridePendingTransition(R.animator.slide_in, R.animator.slide_out);
						finish();
					} else {
						Toast.makeText(getBaseContext(), "Contraseña Incorrecta", Toast.LENGTH_SHORT).show();
					}
				}
			});
			builder.setNegativeButton(R.string.cancelar, null);
			builder.show();
		} else if (Globals.MODULO.equals(Globals.MODULO_PEDIDO)) {
			startActivity(intent);
			overridePendingTransition(R.animator.slide_in, R.animator.slide_out);
			finish();
		}
	}

}
