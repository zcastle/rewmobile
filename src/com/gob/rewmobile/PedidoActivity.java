package com.gob.rewmobile;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.gob.rewmobile.objects.BaseClass;
import com.gob.rewmobile.objects.Data;
import com.gob.rewmobile.util.AdminSQLiteOpenHelper;
import com.gob.rewmobile.util.BloqueAdapter;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class PedidoActivity extends Activity implements OnItemClickListener {

	//private String[] lstCategorias;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private GridView gridView;
    
    private CharSequence mDrawerTitle;
    //private CharSequence mTitle;
    
	private String mozo_name = null;
	private String mesa_name = null;
	private TableLayout tb;
	private AdminSQLiteOpenHelper dbAdmin;
	
	
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
		
		Bundle bundle = this.getIntent().getExtras();
		mozo_name = bundle.getString("mozo_name");
		mesa_name = bundle.getString("mesa_name");
		ActionBar ab = getActionBar();
		ab.setTitle(mozo_name);
		ab.setSubtitle("Mesas".concat(" ").concat(mesa_name));
		
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
        tb = (TableLayout) findViewById(R.id.grid_item_pedido);
        
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
		/*new Thread() {
			public void run() {
				try {
					Data data = new Data(getBaseContext());
					data.loadMesas();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();*/
		
		Intent intent = new Intent(getBaseContext(), FragmentMesasActivity.class);
		Bundle bundle = new Bundle();
		//bundle.putInt("mozo_id", mozo.getId());
		bundle.putString("mozo_name", mozo_name);
		intent.putExtras(bundle);
		//intent.putExtra("mozo_id", mozo.getId());
		//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
		//AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getBaseContext(), "DBREWMobile", null, 1);
		//new Thread() {
		//	public void run() {
		//		
		//	}
		//}.start();
		SQLiteDatabase db = dbAdmin.getWritableDatabase();
		if(db != null){
			Cursor fila;
			if (co_categoria.isEmpty()) {
				fila = db.rawQuery("SELECT co_producto, no_producto FROM productos", null);
			}else{
				String[] campos = new String[] {"co_producto", "no_producto"};
				String[] args = new String[] {co_categoria};
				//fila = db.rawQuery("SELECT co_producto, no_producto FROM productos WHERE co_categoria = ?", args);
				fila = db.query("productos", campos, "co_categoria=?", args, null, null, null);
			}
			Data.LST_PRODUCTOS.clear();
			if (fila.moveToFirst()) {
				do {
					Data.LST_PRODUCTOS.add(new BaseClass(fila.getString(0), fila.getString(1)));
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
		BaseClass producto = (BaseClass) parent.getItemAtPosition(position);
		insertPedido(producto, position, true);
	}
	
	private void insertPedido(BaseClass producto, int position, boolean insertDB){
		//BaseClass producto = (BaseClass) parent.getItemAtPosition(position);
		//TableLayout tb = (TableLayout) findViewById(R.id.grid_item_pedido);
		TableRow tr = new TableRow(this);
		tr.setBackgroundResource(R.drawable.background_pedido_registro);
		tr.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		TextView tv0 = new TextView(this);
		TextView tv1 = new TextView(this);
		TextView tv2 = new TextView(this);
		TextView tv3 = new TextView(this);

		TableRow.LayoutParams lp0 = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
		TableRow.LayoutParams lp1 = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp1.gravity = Gravity.CENTER_VERTICAL;
		
		tv0.setTextColor(Color.WHITE);
		tv0.setPadding(3, 0, 0, 0);
		tv0.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
		tv0.setText(producto.getName());
		tv0.setLayoutParams(lp0);
		
		tv1.setTextColor(Color.WHITE);
		tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
		tv1.setText("1");
		//int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
		//tv1.setMinimumWidth(px);
		tv1.setGravity(Gravity.RIGHT);
		tv1.setLayoutParams(lp1);
		
		tv2.setTextColor(Color.WHITE);
		tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
		tv2.setText("2.00");
		tv2.setGravity(Gravity.RIGHT);
		tv2.setLayoutParams(lp1);
		
		tv3.setTextColor(Color.WHITE);
		tv3.setPadding(0, 0, 3, 0);
		tv3.setGravity(Gravity.RIGHT);
		tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
		tv3.setText("3.00");
		tv3.setLayoutParams(lp1);
		
		tr.addView(tv0);
		tr.addView(tv1);
		tr.addView(tv2);
		tr.addView(tv3);
		//tb.addView(tr);
		
		SQLiteDatabase db = dbAdmin.getWritableDatabase();
		if(db != null){
			final JSONObject m_fuente = new JSONObject();
			String[] campos = new String[] {"va_producto","co_destino"};
			String[] args = new String[] {String.valueOf(producto.getId())};
			Log.i("co_producto", producto.getId());
			Cursor fila = db.query("productos", campos, "co_producto=?", args, null, null, null);
			if (fila.moveToFirst()) {
				do {
					try {
						m_fuente.put("nmesa", mesa_name);
						m_fuente.put("nmozo", mozo_name);
						m_fuente.put("idprod", producto.getId());
						m_fuente.put("prod", producto.getName());
						m_fuente.put("cant", "1");
						m_fuente.put("precio", fila.getString(0));
						m_fuente.put("pax", "1");
						m_fuente.put("cod_dest", fila.getString(1));
						m_fuente.put("mnsg", "");
						tv0.setText(producto.getName());
						tv2.setText(fila.getString(0));
						tb.addView(tr);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} while(fila.moveToNext());
			}
			db.close();
			
			if(insertDB){
				String URL ="http://".concat(Data.URL_HOST).concat(":8080/rewmobile/insertPedidos.php");
				Data data = new Data(getBaseContext());
				data.insertPedido(URL, m_fuente);
			}
		}
	}


}
