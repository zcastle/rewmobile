package com.gob.rewmobile;

import java.io.IOException;

import com.gob.rewmobile.objects.Data;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnTouchListener {

	private LoadDataTask loadDataTask = null;
	private View LayoutStatusSync;
	private Button btnReintentar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_status_sync);

		LayoutStatusSync = findViewById(R.id.status_sync);
		btnReintentar = (Button) findViewById(R.id.btnReintentar);
		btnReintentar.setOnTouchListener(this);

		if (loadDataTask != null) {
			return;
		}

		showProgress(true);
		loadDataTask = new LoadDataTask(this);
		loadDataTask.execute((Void) null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent i = new Intent(this, ConfiguracionActivity.class);
			startActivity(i);
			break;
		}
		return true;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			LayoutStatusSync.setVisibility(View.VISIBLE);
			LayoutStatusSync.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							LayoutStatusSync.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});
		} else {
			LayoutStatusSync.setVisibility(show ? View.VISIBLE : View.GONE);
		}
	}

	public class LoadDataTask extends AsyncTask<Void, Void, Boolean> {

		private Context context;

		public LoadDataTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// Thread.sleep(2000);
			try {
				Data data = new Data(this.context);
				data.loadMozos();
				data.loadMesas();
				data.loadProductos();
				data.loadCategorias();
			} catch (IOException e) {
				/*
				 * AlertDialog.Builder builder = new
				 * AlertDialog.Builder(MainActivity.this);
				 * builder.setMessage("No se puede conectar al servidor")
				 * .setTitle("IOException"); AlertDialog dialog =
				 * builder.create(); dialog.show();
				 */
				// Toast.makeText(getApplicationContext(), "nada 1",
				// Toast.LENGTH_SHORT).show();
				Log.e("ERRORUNO", e.toString());
				return false;
			} catch (Exception e) {
				/*
				 * AlertDialog.Builder builder = new
				 * AlertDialog.Builder(MainActivity.this);
				 * builder.setMessage("No se puede conectar al servidor")
				 * .setTitle("Exception"); AlertDialog dialog =
				 * builder.create(); dialog.show();
				 */
				// Toast.makeText(getApplicationContext(), "data 2",
				// Toast.LENGTH_SHORT).show();
				Log.e("ERRORDOS", e.toString());
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			loadDataTask = null;
			showProgress(false);

			if (success) {
				Intent intent = new Intent(MainActivity.this,
						AccesoActivity.class);
				startActivity(intent);
				overridePendingTransition(R.animator.slide_in,
						R.animator.slide_out);
				finish();
			} else {
				btnReintentar.setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected void onCancelled() {
			loadDataTask = null;
			showProgress(false);
		}
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		btnReintentar.setVisibility(View.INVISIBLE);
		showProgress(true);
		loadDataTask = new LoadDataTask(this);
		loadDataTask.execute((Void) null);
		return true;
	}

}
