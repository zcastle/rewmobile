package com.gob.rewmobile;

import com.gob.rewmobile.objects.BaseClass;
import com.gob.rewmobile.objects.Data;
import com.gob.rewmobile.util.BloqueAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class AccesoActivity extends Activity implements OnItemClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}*/
		setContentView(R.layout.activity_acceso);
		
		GridView gridView = (GridView) findViewById(R.id.gridviewacceso);
		gridView.setAdapter(new BloqueAdapter(this, Data.LST_MOZOS, BloqueAdapter.ITEM_MOZO));
		gridView.setOnItemClickListener(this);
	}

	/*private void showMessage(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
	       .setTitle("Message");
		AlertDialog dialog = builder.create();
		dialog.show();
	}*/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.acceso, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.action_salir:
	        	//Intent intent = new Intent(Intent.ACTION_MAIN);
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
	public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {
		//int p = position + 1;
		BaseClass mozo = (BaseClass) parent.getItemAtPosition(position);
		//Toast.makeText(AccesoActivity.this, mozo.getName(), Toast.LENGTH_SHORT).show();
		
		Intent intent = new Intent(this, FragmentMesasActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("mozo_id", mozo.getId());
		bundle.putString("mozo_name", mozo.getName());
		intent.putExtras(bundle);
		//intent.putExtra("mozo_id", mozo.getId());
        startActivity(intent);
        overridePendingTransition(R.animator.slide_in, R.animator.slide_out);
	}

}
