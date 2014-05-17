package com.ob.rewmobile;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.fortysevendeg.android.swipelistview.SwipeListView;
import com.ob.rewmobile.adapter.CategoriasAdapter;
import com.ob.rewmobile.adapter.PedidoAdapter;
import com.ob.rewmobile.adapter.ProductoAdapter;
import com.ob.rewmobile.listener.PedidoListener;
import com.ob.rewmobile.listener.SearchViewListener;
import com.ob.rewmobile.model.Categoria;
import com.ob.rewmobile.model.PedidoController;
import com.ob.rewmobile.model.Usuario;
import com.ob.rewmobile.util.App;
import com.ob.rewmobile.util.Data;
import com.ob.rewmobile.util.Globals;
import com.ob.rewmobile.util.Util;

public class PedidoActivity extends Activity {

	private PedidoController PEDIDO;
	private GridView gridCategorias;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	
	private ListView listViewProductos;

	public SwipeListView swipeListView;
	public TextView tvMontoTotalMesa;
	private SearchView searchView;

	private PedidoListener pedidoListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_pedido);

		setupActionBar();
		
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		gridCategorias = (GridView) findViewById(R.id.listViewCategorias);
		gridCategorias.setAdapter(new CategoriasAdapter(this, Data.categoriaController.getCategorias()));
		gridCategorias.setOnItemClickListener(new DrawerItemClickListener());
		
		listViewProductos = (ListView) findViewById(R.id.listViewProductos);
		listViewProductos.setAdapter(new ProductoAdapter(this, Data.productoController.getProductos()));
		listViewProductos.setOnItemClickListener(pedidoListener);

		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu();
			}
		};
		drawerToggle.setDrawerIndicatorEnabled(false);
		drawerLayout.setDrawerListener(drawerToggle);
		
		Bundle bundle = getIntent().getExtras();
		int acceso_id = bundle.getInt("mozo_id");
		//acceso_name = bundle.getString("mozo_name");
		Globals.USUARIO_LOGIN = Data.usuarioController.getUsuarioById(acceso_id);
		if (App.isCaja()) {
			PEDIDO.setCajero(Globals.USUARIO_LOGIN);
			PEDIDO.setMozo(new Usuario(0, "*"));
		} else if (App.isPedido()) {
			PEDIDO.setMozo(Globals.USUARIO_LOGIN);
		}
		pedidoListener.refresh();

		SearchViewListener searchViewProductos = new SearchViewListener(this, listViewProductos);
		searchView = (SearchView) findViewById(R.id.search_productos);
        searchView.setOnQueryTextListener(searchViewProductos);
        searchView.setOnCloseListener(searchViewProductos);

		if (savedInstanceState == null) {
			selectItem(0);
		}

		swipeListView = (SwipeListView) findViewById(R.id.grid_item_pedido);
		swipeListView.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
		
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		//Display display = wm.getDefaultDisplay();
		
		@SuppressWarnings("deprecation")
		int widthPanel = (Util.convertPixelsToDp(this, display.getWidth()) - 300) - 200;
		swipeListView.setOffsetLeft(Util.convertDpToPixel(this, widthPanel));
		
		
	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowCustomEnabled(true);
		LayoutInflater li = LayoutInflater.from(this); //(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewActionBar = li.inflate(R.layout.layout_action_bar, null);
		
		PEDIDO = new PedidoController("0");
		pedidoListener = new PedidoListener(this, PEDIDO, viewActionBar);
		
		getActionBar().setCustomView(viewActionBar);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.pedido, menu);
		if (App.isPedido()) menu.findItem(R.id.action_pagar).setVisible(false);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			finish();
			return true;
		case R.id.action_enviar:
			pedidoListener.enviar();
			break;
		case R.id.action_precuenta:
			pedidoListener.precuenta();
			break;
		case R.id.action_pagar:
			pedidoListener.pagar();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Categoria categoria = (Categoria) parent.getItemAtPosition(position);
			listViewProductos.setAdapter(new ProductoAdapter(getBaseContext(), Data.productoController.getProductoByCategoria(categoria)));
			selectItem(position);
		}
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
		drawerLayout.closeDrawer(gridCategorias);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}
	
	public PedidoAdapter getPedidoAdapter() {
		return (PedidoAdapter)swipeListView.getAdapter();
	}
	
	public PedidoListener getPedidoListener() {
		return pedidoListener;
	}
	
}