package com.ob.rewmobile;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fortysevendeg.android.swipelistview.SwipeListView;
import com.ob.rewmobile.adapter.CategoriasAdapter;
import com.ob.rewmobile.adapter.PedidoAdapter;
import com.ob.rewmobile.adapter.ProductoAdapter;
import com.ob.rewmobile.listener.SearchViewListener;
import com.ob.rewmobile.model.Categoria;
import com.ob.rewmobile.model.PedidoController;
import com.ob.rewmobile.model.Producto;
import com.ob.rewmobile.model.Usuario;
import com.ob.rewmobile.task.AddProductoTask;
import com.ob.rewmobile.task.EditPedidoTask;
import com.ob.rewmobile.task.EnviarPedidoTask;
import com.ob.rewmobile.task.EnviarPrecuentaTask;
import com.ob.rewmobile.task.LoadPedidoTask;
import com.ob.rewmobile.util.Data;
import com.ob.rewmobile.util.Globals;
import com.ob.rewmobile.util.Util;

public class PedidoActivity extends Activity implements OnItemClickListener {

	private int acceso_id;
	private GridView listViewCategorias;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	
	private ListView listViewProductos;

	private SwipeListView listViewPedido;
	private TextView txtMontoTotalMesa;
	
	private SearchView searchView;
	
	private PedidoController PEDIDO;
	
	private static final int MNU_PAGAR = 0;
	
	private MenuItem mnuItemMozoPax;
	
	private final String MOZO_PAX_MESA_NUEVA = "Sin Mozo - 1 PAX";
	
	private Switch swServicio;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_pedido);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		listViewCategorias = (GridView) findViewById(R.id.listViewCategorias);
		listViewCategorias.setAdapter(new CategoriasAdapter(this, Data.categoriaController.getCategorias()));
		listViewCategorias.setOnItemClickListener(new DrawerItemClickListener());
		
		listViewProductos = (ListView) findViewById(R.id.gridviewproductos);
		listViewProductos.setAdapter(new ProductoAdapter(this, Data.productoController.getProductos()));
		listViewProductos.setOnItemClickListener(this);

		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu();
			}
		};
		drawerLayout.setDrawerListener(drawerToggle);
		
		setupActionBar();

		Bundle bundle = getIntent().getExtras();
		acceso_id = bundle.getInt("mozo_id");
		
		
		getActionBar().setTitle(bundle.getString("mozo_name"));
		getActionBar().setSubtitle("Mesa ".concat(bundle.getString("mesa_name")));

		SearchViewListener searchViewProductos = new SearchViewListener(this, listViewProductos);
		searchView = (SearchView) findViewById(R.id.search_productos);
        searchView.setOnQueryTextListener(searchViewProductos);
        searchView.setOnCloseListener(searchViewProductos);

		if (savedInstanceState == null) {
			selectItem(0);
		}

		txtMontoTotalMesa = (TextView) findViewById(R.id.txtMontoTotalMesa);
		txtMontoTotalMesa.setText("TOTAL S/. 0.00");
		//PEDIDO.setProductos(new ArrayList<Producto>());
		//pedidoAdapter = new PedidoAdapter(this, PEDIDO, txtMontoTotalMesa);
		listViewPedido = (SwipeListView) findViewById(R.id.grid_item_pedido);
		//listViewPedido.setAdapter(pedidoAdapter);
	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowCustomEnabled(true);
		LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.layout_total, null);
		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				verTotales();	
			}
		});
		getActionBar().setCustomView(v);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.pedido, menu);
		if (Globals.MODULO.equals(Globals.MODULO_CAJA)) menu.add(0, MNU_PAGAR, 2, R.string.mnu_pagar);
		mnuItemMozoPax = (MenuItem) menu.findItem(R.id.action_mozo_pax);
		swServicio = (Switch) menu.findItem(R.id.action_servicio).getActionView();
		swServicio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        try {
					PEDIDO.setServicio(PedidoActivity.this, isChecked);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		});
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (!Globals.DIRECTO) {
				backToMesas();
			} else {
				LoadPedido();
			}
			break;
		case R.id.action_enviar:
			enviar();
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

	@Override
	public void onBackPressed() {
		if (!Globals.DIRECTO) {
			backToMesas();
		} else {
			backToMozos();
		}
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
	
	private void backToMozos() {
		startActivity(new Intent(getBaseContext(), AccesoActivity.class));
		finish();
		overridePendingTransition(R.animator.slide_in, R.animator.slide_out);
	}
	
	private void LoadPedido() {
		View viewSelectMesa = LayoutInflater.from(this).inflate(R.layout.layout_select_mesa, null);
		final EditText txtMesa = (EditText) viewSelectMesa.findViewById(R.id.txtMesa);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(viewSelectMesa);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				PEDIDO = new PedidoController();
				PEDIDO.setMesa(txtMesa.getText().toString());
				if (Globals.MODULO.equals(Globals.MODULO_CAJA)) {
					PEDIDO.setCajero(Data.usuarioController.getUsuarioById(acceso_id));
					PEDIDO.setMozo(new Usuario(0, "Sin Mozo"));
				} else {
					PEDIDO.setMozo(Data.usuarioController.getUsuarioById(acceso_id));
					PEDIDO.setCajero(new Usuario(0, "Cajero"));
				}
				//new LoadPedidoTask(PedidoActivity.this, PEDIDO, listViewPedido, txtMontoTotalMesa, mnuItemMozoPax).execute();
				try {
					if(new LoadPedidoTask(PedidoActivity.this, PEDIDO).execute().get()) {
						listViewPedido.setAdapter(new PedidoAdapter(PedidoActivity.this, PEDIDO, txtMontoTotalMesa));
						((Activity) PedidoActivity.this).getActionBar().setSubtitle("Mesa ".concat(PEDIDO.getMesa()));
						txtMontoTotalMesa.setText("TOTAL S/. " + Util.format(PEDIDO.getTotal()));
						if (PEDIDO.getProductos().size()>0) {
							swServicio.setChecked(PEDIDO.isServicio());
							mnuItemMozoPax.setTitle(PEDIDO.getMozo().getNombre().concat(" - ").concat(PEDIDO.getPax()+" PAX"));
							PEDIDO.setPax(PEDIDO.getPax());
						} else {
							swServicio.setChecked(true);
							mnuItemMozoPax.setTitle(MOZO_PAX_MESA_NUEVA);
							PEDIDO.setPax(1);
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		});
		builder.setNegativeButton(R.string.cancelar, null);
		builder.show();
	}
	
	private void verTotales() {
		if (!PEDIDO.getMesa().equals("0")) {
			Double sTotal, igv, serv = 0.0, total;
			total = PEDIDO.getTotal();
			if (PEDIDO.isServicio()) {
				sTotal = total / (((Globals.VA_IGV+Globals.VA_SERV)/100)+1);
				igv = sTotal * (Globals.VA_IGV / 100);
				serv = sTotal * (Globals.VA_SERV / 100);
			} else {
				sTotal = total / ((Globals.VA_IGV / 100)+1);
				igv = sTotal * (Globals.VA_IGV / 100);
			}
			total = sTotal+igv+serv;
			View viewTotales = LayoutInflater.from(this).inflate(R.layout.layout_totales, null);
			((TextView)viewTotales.findViewById(R.id.tvNeto)).setText(Util.format(sTotal));
			((TextView)viewTotales.findViewById(R.id.tvIgv)).setText(Util.format(igv));
			((TextView)viewTotales.findViewById(R.id.tvServ)).setText(Util.format(serv));
			((TextView)viewTotales.findViewById(R.id.tvServData)).setText("SERV. ("+Globals.VA_SERV+"%)");
			((TextView)viewTotales.findViewById(R.id.tvTotal)).setText(Util.format(total));
			new AlertDialog.Builder(this)
			.setTitle("TOTALES -> MESA: ".concat(PEDIDO.getMesa()))
			.setIcon(R.drawable.ic_launcher)
			.setView(viewTotales)
			.setPositiveButton(R.string.cerrar, null)
			.show();
		}
		
	}
	
	private void editPedido() {
		
		if (PEDIDO.getMesa().equals("0")) {
			Toast.makeText(this, "Debe ingresar a una mesa", Toast.LENGTH_SHORT).show();
			return;
		}
		
		View viewEditMesa = LayoutInflater.from(this).inflate(R.layout.layout_edit_mesa, null);
		
		final EditText txtPax = (EditText) viewEditMesa.findViewById(R.id.txtPax);
		txtPax.setText(PEDIDO.getPax()+"");
		final Spinner spinner = (Spinner) viewEditMesa.findViewById(R.id.spnMozos);
		//spinner.setOnItemSelectedListener(this);
		ArrayAdapter<Usuario> spinner_adapter = new ArrayAdapter<Usuario>(this, android.R.layout.simple_spinner_item, Data.usuarioController.getUsuariosByRol(Globals.ROL_MOZO));
		spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinner_adapter);
		
		if(PEDIDO.getMozo().getId()>0) {
			SelectSpinnerItemByObject(spinner, PEDIDO.getMozo());
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(viewEditMesa);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				PEDIDO.setMozo((Usuario)spinner.getSelectedItem());
				PEDIDO.setPax(Integer.parseInt(txtPax.getText().toString()));
				mnuItemMozoPax.setTitle(PEDIDO.getMozo().getNombre().concat(" - ").concat(PEDIDO.getPax()+" PAX"));
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
	
	private void enviar() {
		if(!this.PEDIDO.getMesa().equals("0")){
			new AlertDialog.Builder(this)
			.setMessage("¿Esta seguro de querer enviar el pedido?")
			.setCancelable(false)
			.setPositiveButton("Si", new DialogInterface.OnClickListener(){
				@Override
		        public void onClick(DialogInterface dialog, int which) {
					new EnviarPedidoTask(PedidoActivity.this, PEDIDO, listViewPedido, txtMontoTotalMesa).execute();
		        }
			})
			.setNegativeButton("No", null)
		    .show();
		} else {
			Toast.makeText(this, "No se puede ENVIAR en la mesa 0", Toast.LENGTH_SHORT).show();
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
		overridePendingTransition(R.animator.slide_in, R.animator.slide_out);
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
		drawerLayout.closeDrawer(listViewCategorias);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {
		if (PEDIDO.getMesa().equals("0")) {
			Toast.makeText(this, "Debe ingresar el numero de MESA", Toast.LENGTH_LONG).show();
		} else {
			Producto producto = (Producto) parent.getItemAtPosition(position);
			Producto newProducto = null;
			try {
				newProducto = (Producto) producto.clone();
			} catch (CloneNotSupportedException e1) {
				e1.printStackTrace();
			}
			new AddProductoTask(this, PEDIDO, newProducto, false).execute();
			((PedidoAdapter) listViewPedido.getAdapter()).addItem(newProducto);
			txtMontoTotalMesa.setText("TOTAL S/. " + Util.format(PEDIDO.getTotal()));
		}
	}
	
}