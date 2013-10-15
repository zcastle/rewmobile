package com.gob.rewmobile;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.fortysevendeg.android.swipelistview.SwipeListView;
import com.gob.rewmobile.objects.BaseClass;
import com.gob.rewmobile.objects.Data;
import com.gob.rewmobile.objects.ListAdapter;
import com.gob.rewmobile.objects.Producto;
import com.gob.rewmobile.objects.SettingsManager;
import com.gob.rewmobile.util.AdminSQLiteOpenHelper;
import com.gob.rewmobile.util.BloqueAdapter;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class PedidoActivity extends FragmentActivity implements OnItemClickListener {

	//private String[] lstCategorias;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private GridView gridView;
    
    private CharSequence mDrawerTitle;
    //private CharSequence mTitle;
    
	private String mozo_name = null;
	private String mesa_name = null;
	//private Mesa mesa = null;
	//private ListView tb;
	private AdminSQLiteOpenHelper dbAdmin;
	
	private LoadPedidoTask loadPedidoTask = null;
	private ProgressDialog mProgress;
	
	private SwipeListView swipeListView;
	private ListAdapter listAdpter;
	
	TextView txtMontoTotalMesa;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedido);
		dbAdmin = new AdminSQLiteOpenHelper(this, "DBREWMobile", null, 1);
		////
			
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // Set the adapter for the list view
        //mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, lstCategorias));
        mDrawerList.setAdapter(new BloqueAdapter(this, Data.LST_CATEGORIAS, BloqueAdapter.ITEM_CATEGORIA));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		
		////
		// Show the Up button in the action bar.
		setupActionBar();
		
		//Bundle bundle = this.getIntent().getExtras();
		//mozo_name = bundle.getString("mozo_name");
		//this.mesa = (Mesa) bundle.getSerializable("mesa");
		
		Intent i = getIntent();
		
		mozo_name = i.getExtras().getString("mozo_name");
		mesa_name = i.getExtras().getString("mesa_name");
		
		ActionBar ab = getActionBar();
		ab.setTitle(mozo_name);
		ab.setSubtitle("Mesas".concat(" ").concat(this.mesa_name));
		
		//LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lista_productos);
		gridView = (GridView) findViewById(R.id.gridviewproductos);
		updateLstProductos("");
		//gridView.setAdapter(new BloqueAdapter(this, Data.LST_PRODUCTOS, BloqueAdapter.ITEM_PRODUCTO));
		gridView.setOnItemClickListener(this);
		
		/////
		mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
		/////
        txtMontoTotalMesa = (TextView) findViewById(R.id.txtMontoTotalMesa);
        listAdpter = new ListAdapter(this, new ArrayList<Producto>());
        swipeListView = (SwipeListView) findViewById(R.id.grid_item_pedido);
        
        //reload();
        
        mProgress = new ProgressDialog(this);
	    mProgress.setMessage("Cargando Pedido...");
	    mProgress.show();
	    if (loadPedidoTask != null) return;
		loadPedidoTask = new LoadPedidoTask(this, mesa_name);
		loadPedidoTask.execute((Void) null);
        
	}

	private void reload() {
        SettingsManager settings = SettingsManager.getInstance();
        swipeListView.setSwipeMode(settings.getSwipeMode());
        swipeListView.setSwipeActionLeft(settings.getSwipeActionLeft());
        swipeListView.setSwipeActionRight(settings.getSwipeActionRight());
        swipeListView.setOffsetLeft(convertDpToPixel(settings.getSwipeOffsetLeft()));
        swipeListView.setOffsetRight(convertDpToPixel(settings.getSwipeOffsetRight()));
        swipeListView.setAnimationTime(settings.getSwipeAnimationTime());
        swipeListView.setSwipeOpenOnLongPress(settings.isSwipeOpenOnLongPress());
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pedido, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			backToMesas();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		backToMesas();
	}
	
	private void backToMesas(){
		Intent intent = new Intent(getBaseContext(), FragmentMesasActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("mozo_name", mozo_name);
		intent.putExtras(bundle);
        startActivity(intent);
        finish();
	}
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    	BaseClass cate = (BaseClass) parent.getItemAtPosition(position);
	    	updateLstProductos(cate.getId());
	        selectItem(position);
	    }
	}

	private void updateLstProductos(final String co_categoria){
		SQLiteDatabase db = dbAdmin.getWritableDatabase();
		if(db != null){
			Cursor fila;
			if (co_categoria.isEmpty()) {
				fila = db.rawQuery("SELECT id, no_producto, va_producto FROM productos", null);
			}else{
				String[] campos = new String[] {"id", "no_producto", "va_producto"};
				String[] args = new String[] {co_categoria};
				//fila = db.rawQuery("SELECT co_producto, no_producto FROM productos WHERE co_categoria = ?", args);
				fila = db.query("productos", campos, "co_categoria=?", args, null, null, null);
			}
			Data.LST_PRODUCTOS.clear();
			Producto producto;
			if (fila.moveToFirst()) {
				do {
					producto = new Producto();
					producto.setId(fila.getInt(0));
					producto.setNombre(fila.getString(1));
					producto.setPrecio(fila.getDouble(2));
					producto.setCantidad(1.0);
					Data.LST_PRODUCTOS.add(producto);
				} while(fila.moveToNext());
			}
			db.close();
			gridView.setAdapter(new BloqueAdapter(getBaseContext(), Data.LST_PRODUCTOS, BloqueAdapter.ITEM_PRODUCTO));
		}
	}
	
	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
	    //mDrawerList.setItemChecked(position, true);
	    mDrawerLayout.closeDrawer(mDrawerList);
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {
		Producto producto = (Producto) parent.getItemAtPosition(position);
		//insertPedido(producto, true);
		insertPedido(producto, true);
		((ListAdapter)swipeListView.getAdapter()).addItem(producto);
		txtMontoTotalMesa.setText("TOTAL S/. "+Data.PEDIDO.getTotal());
	}
	
	private void insertPedido(Producto producto, boolean insertDB){
		SQLiteDatabase db = dbAdmin.getWritableDatabase();
		if(db != null){
			final JSONObject m_fuente = new JSONObject();
			String[] campos = new String[] {"va_producto","co_destino"};
			String[] args = new String[] {String.valueOf(producto.getId())};
			Cursor fila = db.query("productos", campos, "id=?", args, null, null, null);
			if (fila.moveToFirst()) {
				do {
					try {
						m_fuente.put("nmesa", mesa_name);
						m_fuente.put("nmozo", mozo_name);
						m_fuente.put("idprod", producto.getId());
						m_fuente.put("prod", producto.getNombre());
						m_fuente.put("cant", producto.getCantidad()+"");
						m_fuente.put("precio", fila.getString(0));
						m_fuente.put("pax", "1");
						m_fuente.put("cod_dest", fila.getString(1));
						m_fuente.put("mnsg", "");
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} while(fila.moveToNext());
			}
			db.close();
			
			if(insertDB){
				Data data = new Data(getBaseContext());
				data.insertPedido(m_fuente);
			}
		}
	}

public class LoadPedidoTask extends AsyncTask<Void, Void, Boolean> {
		
		private Context context;
		private String mesa_name;

		public LoadPedidoTask(Context context, String mesa_name){
		    this.context = context;
		    this.mesa_name = mesa_name;
		}
		
		@Override
		protected void onPreExecute() {
		    super.onPreExecute();
		} 
		
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				//Looper.prepare();
				Data data = new Data(this.context);
				data.loadPedido(this.mesa_name);
				//Looper.loop();
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
				//tb.removeAllViews();
				//for (Producto producto: Data.PEDIDO.getProducto()) {
					//insertPedido(producto, false);
				//}
				//tb.setAdapter(new BloqueAdapter(this.context, Data.PEDIDO.getProducto(), BloqueAdapter.ITEM_PEDIDO));
				listAdpter = new ListAdapter(this.context, Data.PEDIDO.getProducto());
				swipeListView.setAdapter(listAdpter);
				//((ListAdapter)swipeListView.getAdapter()).notifyDataSetChanged();
				TextView txtMontoTotalMesa = (TextView) findViewById(R.id.txtMontoTotalMesa);
				txtMontoTotalMesa.setText("TOTAL S/. "+Data.PEDIDO.getTotal());
				Log.i("onPostExecute", "True");
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
}