package com.gob.rewmobile;

import com.gob.rewmobile.adapter.MozoAdapter;
import com.gob.rewmobile.model.Usuario;
import com.gob.rewmobile.util.Data;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class AccesoActivity extends Activity implements OnItemClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_acceso);

		GridView gridView = (GridView) findViewById(R.id.gridviewacceso);
		gridView.setAdapter(new MozoAdapter(this, Data.LST_MOZOS.getUsuarios()));
		gridView.setOnItemClickListener(this);
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
			break;
		}
		return true;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Usuario mozo = (Usuario) parent.getItemAtPosition(position);
		Intent intent = new Intent(this, FragmentMesasActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("mozo_id", mozo.getId());
		bundle.putString("mozo_name", mozo.getNombre());
		intent.putExtras(bundle);
		startActivity(intent);
		overridePendingTransition(R.animator.slide_in, R.animator.slide_out);
	}

}
