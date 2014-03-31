package com.ob.rewmobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.fortysevendeg.android.swipelistview.SwipeListView;
import com.ob.rewmobile.adapter.CategoriasAdapter;
import com.ob.rewmobile.adapter.PedidoAdapter;
import com.ob.rewmobile.adapter.ProductoAdapter;
import com.ob.rewmobile.listener.PedidoListener;
import com.ob.rewmobile.listener.SearchViewListener;
import com.ob.rewmobile.model.Categoria;
import com.ob.rewmobile.model.PedidoController;
import com.ob.rewmobile.model.Usuario;
import com.ob.rewmobile.task.EditPedidoTask;
import com.ob.rewmobile.task.EnviarPedidoTask;
import com.ob.rewmobile.task.EnviarPrecuentaTask;
import com.ob.rewmobile.util.App;
import com.ob.rewmobile.util.Data;
import com.ob.rewmobile.util.Globals;
import com.ob.rewmobile.util.Util;

public class PedidoActivity extends Activity {

	//private String acceso_name;
	private PedidoController PEDIDO;
	private GridView listViewCategorias;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	
	private ListView lvProductos;

	public SwipeListView swipeListView;
	public TextView tvMontoTotalMesa;
	private SearchView searchView;
	private static final int MNU_PAGAR = 0;

	public PedidoListener pedidoListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_pedido);

		setupActionBar();
		
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		listViewCategorias = (GridView) findViewById(R.id.listViewCategorias);
		listViewCategorias.setAdapter(new CategoriasAdapter(this, Data.categoriaController.getCategorias()));
		listViewCategorias.setOnItemClickListener(new DrawerItemClickListener());
		
		lvProductos = (ListView) findViewById(R.id.gridviewproductos);
		lvProductos.setAdapter(new ProductoAdapter(this, Data.productoController.getProductos()));
		lvProductos.setOnItemClickListener(pedidoListener);

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

		SearchViewListener searchViewProductos = new SearchViewListener(this, lvProductos);
		searchView = (SearchView) findViewById(R.id.search_productos);
        searchView.setOnQueryTextListener(searchViewProductos);
        searchView.setOnCloseListener(searchViewProductos);

		if (savedInstanceState == null) {
			selectItem(0);
		}

		swipeListView = (SwipeListView) findViewById(R.id.grid_item_pedido);
		swipeListView.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
		
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		
		//Log.e("convertPixelsToDp", Util.convertPixelsToDp(this, display.getWidth())+"");
		
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
		if (Globals.MODULO.equals(Globals.MODULO_CAJA)) menu.add(0, MNU_PAGAR, 2, R.string.mnu_pagar);
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
			precuenta();
			break;
		case MNU_PAGAR:
			pagar();
			break;
		case R.id.action_mozo_pax:
			editPedido();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void editPedido() {
		if (PEDIDO.getMesa().equals("0")) {
			Toast.makeText(this, "Debe ingresar a una mesa", Toast.LENGTH_SHORT).show();
			return;
		}
		
		View viewEditMesa = LayoutInflater.from(this).inflate(R.layout.layout_edit_mesa, null);
		
		if (Globals.MODULO.equals(Globals.MODULO_CAJA)) {
			LinearLayout llMozo = (LinearLayout) viewEditMesa.findViewById(R.id.llMozo);
			llMozo.setVisibility(LinearLayout.VISIBLE);
		}
		
		final EditText txtPax = (EditText) viewEditMesa.findViewById(R.id.txtPax);
		txtPax.setText(PEDIDO.getPax()+"");
		final Spinner spinner = (Spinner) viewEditMesa.findViewById(R.id.spnMozos);
		ArrayAdapter<Usuario> spinner_adapter = new ArrayAdapter<Usuario>(this, android.R.layout.simple_spinner_item, Data.usuarioController.getUsuariosByRol(Globals.ROL_MOZO));
		spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinner_adapter);
		
		if(PEDIDO.getMozo().getId()>0) {
			SelectSpinnerItemByObject(spinner, PEDIDO.getMozo());
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(viewEditMesa);
		builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				int pax = Integer.parseInt(txtPax.getText().toString());
				if (App.isCaja()) {
					Usuario mozo = (Usuario)spinner.getSelectedItem();
					PEDIDO.setMozo(mozo);
				}
				PEDIDO.setPax(pax);
				new EditPedidoTask(PedidoActivity.this, PEDIDO, false).execute();
			}
		});
		builder.setNegativeButton(R.string.cancelar, null);
		builder.show();
	}
	
	public static void SelectSpinnerItemByObject(Spinner spinner, Usuario mozoSelected) {
	    SpinnerAdapter adapter = spinner.getAdapter();
	    for (int pos = 0; pos < adapter.getCount(); pos++) {
	    	Usuario mozo = (Usuario) adapter.getItem(pos);
	        if(mozo == mozoSelected) {
	            spinner.setSelection(pos);
	            return;
	        }
	    }
	}
	
	private void precuenta() {
		if(!PEDIDO.getMesa().equals("0")){
			new EnviarPrecuentaTask(this, PEDIDO).execute();
		} else {
			Toast.makeText(this, "No se puede enviar PRECUENTA en la mesa 0", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void pagar() {
		if (PEDIDO.getMesa().equals("0")) {
			Util.showDialogAdvertencia(this, "No hay un pedido para procesar");
			return;
		} else if (PEDIDO.getMozo().getId()==0) {
			Util.showDialogAdvertencia(this, "Debe especificar un mozo");
			return;
		}
		
		Globals.PEDIDO_PAGAR = PEDIDO;
		startActivity(new Intent(this, PagarActivity.class));
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Categoria categoria = (Categoria) parent.getItemAtPosition(position);
			lvProductos.setAdapter(new ProductoAdapter(getBaseContext(), Data.productoController.getProductoByCategoria(categoria)));
			selectItem(position);
		}
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
		drawerLayout.closeDrawer(listViewCategorias);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}
	
	public PedidoAdapter getPedidoAdapter() {
		return (PedidoAdapter)swipeListView.getAdapter();
	}
	
}