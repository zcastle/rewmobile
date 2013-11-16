package com.gob.rewmobile;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.epson.eposprint.Print;
import com.fortysevendeg.android.swipelistview.SwipeListView;
import com.gob.rewmobile.adapter.CategoriasAdapter;
import com.gob.rewmobile.adapter.PedidoAdapter;
import com.gob.rewmobile.adapter.ProductoAdapter;
import com.gob.rewmobile.model.Categoria;
import com.gob.rewmobile.model.Destino;
import com.gob.rewmobile.model.Producto;
import com.gob.rewmobile.model.ProductoControlller;
import com.gob.rewmobile.objects.Pedido;
import com.gob.rewmobile.util.AdminSQLiteOpenHelper;
import com.gob.rewmobile.util.Data;
import com.gob.rewmobile.util.PrintTicket;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class PedidoActivity extends FragmentActivity implements OnItemClickListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener {

	private ListView listViewCategorias;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	
	private ListView listViewProductos;

	private AdminSQLiteOpenHelper dbAdmin;

	private LoadPedidoTask loadPedidoTask = null;
	private EnviarPedidoTask enviarPedidoTask = null;
	private EnviarPrecuentaTask enviarPrecuentaTask = null;
	
	private ProgressDialog mProgress;
	private ProgressDialog mProgressEnvio;
	private ProgressDialog mProgressEnvioPrecuenta;

	private SwipeListView listViewPedido;
	private PedidoAdapter pedidoAdapter;

	private TextView txtMontoTotalMesa;
	
	private SearchView searchView;
	
	private Pedido PEDIDO;
	private ProductoControlller productoControlller;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedido);
		
		productoControlller = new ProductoControlller();
		
		dbAdmin = new AdminSQLiteOpenHelper(this, "DBREWMobile", null, 1);		

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		listViewCategorias = (ListView) findViewById(R.id.listViewCategorias);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		listViewCategorias.setAdapter(new CategoriasAdapter(this, Data.LST_CATEGORIAS.getCategorias()));
		listViewCategorias.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		setupActionBar();

		Bundle bundle = getIntent().getExtras();
		PEDIDO = new Pedido();
		PEDIDO.setMesa(bundle.getString("mesa_name"));
		PEDIDO.setMozo(Data.LST_MOZOS.getUsuarioById(bundle.getInt("mozo_id")));


		ActionBar ab = getActionBar();
		ab.setTitle(PEDIDO.getMozo().getNombre());
		ab.setSubtitle("Mesa ".concat(PEDIDO.getMesa()));

		listViewProductos = (ListView) findViewById(R.id.gridviewproductos);
		loadProductos();
		listViewProductos.setOnItemClickListener(this);

		if (savedInstanceState == null) {
			selectItem(0);
		}

		txtMontoTotalMesa = (TextView) findViewById(R.id.txtMontoTotalMesa);
		pedidoAdapter = new PedidoAdapter(this, PEDIDO, txtMontoTotalMesa);
		listViewPedido = (SwipeListView) findViewById(R.id.grid_item_pedido);

		mProgressEnvio = new ProgressDialog(this);
		mProgressEnvio.setMessage("Enviando Pedido...");
		
		mProgressEnvioPrecuenta = new ProgressDialog(this);
		mProgressEnvioPrecuenta.setMessage("Enviando Precuenta...");
		
		mProgress = new ProgressDialog(this);
		mProgress.setMessage("Cargando Pedido...");
		mProgress.show();
		if (loadPedidoTask != null)
			return;
		loadPedidoTask = new LoadPedidoTask(this, PEDIDO);
		loadPedidoTask.execute((Void) null);

	}

	@Override
    protected void onDestroy() {
        super.onDestroy();
 
        if (dbAdmin != null) {
        	dbAdmin.close();
        }
    }

	public int convertDpToPixel(float dp) {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return (int) px;
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowCustomEnabled(true);
		// getActionBar().setDisplayShowTitleEnabled(false);
		LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.layout_totales, null);
		getActionBar().setCustomView(v);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pedido, menu);
		
		
		
		MenuItem searchItem = menu.findItem(R.id.search_productos);
		searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			backToMesas();
			return true;
		case R.id.action_enviar:
			enviar();
			return true;
		case R.id.action_precuenta:
			precuenta();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		backToMesas();
	}

	private void backToMesas() {
		Intent intent = new Intent(getBaseContext(), FragmentMesasActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("mozo_id", PEDIDO.getMozo().getId());
		bundle.putString("mozo_name", PEDIDO.getMozo().toString());
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	
	private void enviar() {
		mProgressEnvio.show();
		if (enviarPedidoTask != null)
			return;
		enviarPedidoTask = new EnviarPedidoTask(this, PEDIDO);
		enviarPedidoTask.execute((Void) null);
	}
	
	private void precuenta() {
		mProgressEnvioPrecuenta.show();
		if (enviarPrecuentaTask != null)
			return;
		enviarPrecuentaTask = new EnviarPrecuentaTask(this, PEDIDO);
		enviarPrecuentaTask.execute((Void) null);
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Categoria categoria = (Categoria) parent.getItemAtPosition(position);
			listViewProductos.setAdapter(new ProductoAdapter(getBaseContext(), productoControlller.getProductoByCategoria(categoria)));
			selectItem(position);
		}
	}
	
	private void loadProductos() {
		SQLiteDatabase db = dbAdmin.getWritableDatabase();
		if (db != null) {
			Cursor fila = db.rawQuery("SELECT id, no_producto, va_producto, co_destino, co_categoria FROM productos", null);
			Producto producto;
			if (fila.moveToFirst()) {
				do {
					producto = new Producto();
					producto.setId(fila.getInt(0));
					producto.setNombre(fila.getString(1));
					producto.setPrecio(fila.getDouble(2));
					producto.setDestino(Data.DESTINOS.getDestinoById(fila.getInt(3)));
					producto.setCantidad(1.0);
					producto.setEnviado(false);
					producto.setCategoria(Data.LST_CATEGORIAS.getCategoriaByCodigo(fila.getString(4)));
					productoControlller.getProductos().add(producto);
				} while (fila.moveToNext());
			}
			db.close();
			listViewProductos.setAdapter(new ProductoAdapter(this, productoControlller.getProductos()));
		}
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
		// mDrawerList.setItemChecked(position, true);
		mDrawerLayout.closeDrawer(listViewCategorias);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {
		Producto producto = (Producto) parent.getItemAtPosition(position);
		insertPedido(producto, true);
		((PedidoAdapter) listViewPedido.getAdapter()).addItem(producto);
		txtMontoTotalMesa.setText("TOTAL S/. " + PEDIDO.getTotal());
	}

	private void insertPedido(Producto producto, boolean insertDB) {
		SQLiteDatabase db = dbAdmin.getWritableDatabase();
		if (db != null) {
			final JSONObject m_fuente = new JSONObject();
			String[] campos = new String[] { "va_producto" };
			String[] args = new String[] { String.valueOf(producto.getId()) };
			Cursor fila = db.query("productos", campos, "id=?", args, null,
					null, null);
			if (fila.moveToFirst()) {
				do {
					try {
						m_fuente.put("nmesa", PEDIDO.getMesa());
						m_fuente.put("nmozo", PEDIDO.getMozo());
						m_fuente.put("idprod", producto.getId());
						m_fuente.put("cant", producto.getCantidad() + "");
						m_fuente.put("precio", fila.getString(0));
						m_fuente.put("pax", "1");
						//m_fuente.put("cod_dest", fila.getString(1));
						m_fuente.put("mnsg", "");
						m_fuente.put("co_destino", producto.getDestino().getId());
						//m_fuente.put("fl_envio", producto.getDestino());
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} while (fila.moveToNext());
			}
			db.close();
			
			if (insertDB) {
				Data data = new Data(this);
				data.insertPedido(m_fuente, producto);
			}
		}
	}

	public class LoadPedidoTask extends AsyncTask<Void, Void, Boolean> {

		private Context context;
		private Pedido pedido;

		public LoadPedidoTask(Context context, Pedido pedido) {
			this.context = context;
			this.pedido = pedido;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				// Looper.prepare();
				Data data = new Data(this.context);
				data.loadPedido(pedido);
				// Looper.loop();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (success) {
				pedidoAdapter = new PedidoAdapter(this.context, pedido, txtMontoTotalMesa);
				listViewPedido.setAdapter(pedidoAdapter);
				txtMontoTotalMesa.setText("TOTAL S/. " + pedido.getTotal());
				//Data.PEDIDO.setMesa(this.mesa_name);
				//Data.PEDIDO.setMozo(mozo);
			} else {
				Log.i("onPostExecute", "False");
			}
			loadPedidoTask = null;
			mProgress.dismiss();
		}

		@Override
		protected void onCancelled() {
			loadPedidoTask = null;
			mProgress.dismiss();
		}
	}
	
	public class EnviarPedidoTask extends AsyncTask<Void, Void, Boolean> {

		private Context context;
		private Pedido pedido;

		public EnviarPedidoTask(Context context, Pedido pedido) {
			this.context = context;
			this.pedido = pedido;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			Pedido pedido;
			boolean rpta = true;
			for (Destino destino: Data.DESTINOS.getDestinos()) {
				pedido = this.pedido;
				ArrayList<Producto> productos = this.pedido.getProductosByDestino(destino);
				if(productos.size()>0) {
					pedido.setProducto(productos);
					int[] printerStatus = new int[1];
					PrintTicket printTicket = new PrintTicket(PedidoActivity.this, PrintTicket.TMU220, PrintTicket.ENVIO, destino, pedido);
					printerStatus[0] = printTicket.printDoc();
		    		if ((printerStatus[0] & Print.ST_PRINT_SUCCESS) == Print.ST_PRINT_SUCCESS){
		    			final JSONObject obj = new JSONObject();
						try {
							obj.put("mesa", this.pedido.getMesa());
							obj.put("destino", destino.getId());
							Data data = new Data(this.context);
							data.updateEnvio(obj);
							data.loadPedido(pedido);
						} catch (JSONException e) {
							rpta = false;
							e.printStackTrace();
						} catch (IOException e) {
							rpta = false;
							e.printStackTrace();
						} catch (Exception e) {
							rpta = false;
							e.printStackTrace();
						}
		    			rpta = true;
		    		} else {
		    			rpta = false;
		    		}
				}
			}
			return rpta;
			
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (success) {
				pedidoAdapter = new PedidoAdapter(this.context, pedido, txtMontoTotalMesa);
				listViewPedido.setAdapter(pedidoAdapter);
				pedidoAdapter.notifyDataSetChanged();
				txtMontoTotalMesa.setText("TOTAL S/. " + pedido.getTotal());
				Toast.makeText(
						this.context,
						"Pedido Enviado",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(
						this.context,
						"Pedido No Enviado",
						Toast.LENGTH_SHORT).show();
			}
			enviarPedidoTask = null;
			mProgressEnvio.dismiss();
		}

		@Override
		protected void onCancelled() {
			enviarPedidoTask = null;
			mProgressEnvio.dismiss();
		}
	}
	
	public class EnviarPrecuentaTask extends AsyncTask<Void, Void, Boolean> {

		private Context context;
		private Pedido pedido;

		public EnviarPrecuentaTask(Context context, Pedido pedido) {
			this.context = context;
			this.pedido = pedido;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			int[] printerStatus = new int[1];
			PrintTicket printTicket = new PrintTicket(PedidoActivity.this, PrintTicket.TMU220, PrintTicket.PRECUENTA, this.pedido);
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
				Toast.makeText(this.context, "Precuenta Enviada", Toast.LENGTH_SHORT).show();
			} else {
				Log.i("onPostExecute", "False");
			}
			enviarPrecuentaTask = null;
			mProgressEnvioPrecuenta.dismiss();
		}

		@Override
		protected void onCancelled() {
			enviarPrecuentaTask = null;
			mProgressEnvioPrecuenta.dismiss();
		}
	}

	@Override
	public boolean onClose() {
		listViewProductos.setAdapter(new ProductoAdapter(this, productoControlller.getProductos()));
        return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if (!newText.isEmpty()){
            displayResults(newText.toUpperCase());
        } else {
        	listViewProductos.setAdapter(new ProductoAdapter(this, productoControlller.getProductos()));
        }
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}
	
	private void displayResults(String nombre) {
		listViewProductos.setAdapter(new ProductoAdapter(this, productoControlller.getProductoContainName(nombre)));
    }
}