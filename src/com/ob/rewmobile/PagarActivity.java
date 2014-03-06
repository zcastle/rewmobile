package com.ob.rewmobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.ob.rewmobile.adapter.PedidoAdapterPagar;
import com.ob.rewmobile.listener.GestureListener;
import com.ob.rewmobile.model.Cliente;
import com.ob.rewmobile.model.Departamento;
import com.ob.rewmobile.model.Distrito;
import com.ob.rewmobile.model.PedidoController;
import com.ob.rewmobile.model.Provincia;
import com.ob.rewmobile.task.EditPedidoTask;
import com.ob.rewmobile.util.Data;
import com.ob.rewmobile.util.DialogCarga;
import com.ob.rewmobile.util.Globals;
import com.ob.rewmobile.util.Util;

public class PagarActivity extends Activity implements OnItemSelectedListener {

	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerTogglePedido;
	private ListView listViewPedido;
	private Spinner spnDepartamento;
	private Spinner spnProvincia;
	private Spinner spnDistrito;
	private TextView tvRuc;
	private TextView tvRazonSocial;
	private PedidoController PEDIDO;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pagar);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		PEDIDO = Globals.PEDIDO_PAGAR;
		
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_pagar);
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		listViewPedido = (ListView) findViewById(R.id.listViewPedido);
		listViewPedido.setAdapter(new PedidoAdapterPagar(this, PEDIDO));
		
		drawerTogglePedido = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        drawerLayout.setDrawerListener(drawerTogglePedido);
        
        TextView txtMontoPagar = (TextView) findViewById(R.id.tvMontoPagar);
        TextView txtResta = (TextView) findViewById(R.id.tvResta);
        txtMontoPagar.setText(Globals.MONEDA+Util.format(PEDIDO.getTotal()));
        txtResta.setText(Globals.MONEDA+Util.format(PEDIDO.getTotal()));
        
        TextView tvRucAction = (TextView) findViewById(R.id.tvRucAction);
        tvRucAction.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				addRuc();
			}
		});
        
        tvRuc = (TextView) findViewById(R.id.tvRuc);
        tvRazonSocial = (TextView) findViewById(R.id.tvRazonSocial);
        TextView btnPagar = (TextView) findViewById(R.id.btnPagar);
        
        final GestureDetector gestureDetector;
        gestureDetector = new GestureDetector(this, new GestureListener());
        btnPagar.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		});
        
        if (PEDIDO.getCliente()!=null) {
        	tvRuc.setText(PEDIDO.getCliente().getRuc());
        	tvRazonSocial.setText(PEDIDO.getCliente().getRazonSocial());
        }
        
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = NavUtils.getParentActivityIntent(this);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
			NavUtils.navigateUpTo(this, intent); //navigateUpFromSameTask
			finish();
			//overridePendingTransition(R.animator.slide_in, R.animator.slide_out);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerTogglePedido.syncState();
	}
	
	private void addRuc() {
		View viewCliente = LayoutInflater.from(this).inflate(R.layout.layout_dialog_ruc, null);
		final EditText txtRuc = (EditText) viewCliente.findViewById(R.id.txtRuc);
		final EditText txtRazonNombre = (EditText) viewCliente.findViewById(R.id.txtRazonNombre);
		final EditText txtDireccion = (EditText) viewCliente.findViewById(R.id.txtDireccion);
		Button btnBuscarRuc = (Button) viewCliente.findViewById(R.id.btnBuscarRuc);
		
		spnDepartamento = (Spinner) viewCliente.findViewById(R.id.spnDepartamento);
		spnProvincia = (Spinner) viewCliente.findViewById(R.id.spnProvincia);
		spnDistrito = (Spinner) viewCliente.findViewById(R.id.spnDistrito);
		spnDepartamento.setOnItemSelectedListener(this);
		spnProvincia.setOnItemSelectedListener(this);
		spnDistrito.setOnItemSelectedListener(this);
		
		ArrayAdapter<Departamento> spinner_adapter = new ArrayAdapter<Departamento>(this, android.R.layout.simple_spinner_item, Data.ubigeoController.getDepartamentos());
		spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnDepartamento.setAdapter(spinner_adapter);
		SelectSpinnerItemById(spnDepartamento, 1392);
		
		btnBuscarRuc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ProgressDialog pd = new DialogCarga(PagarActivity.this, "Buscando ".concat(txtRuc.getText().toString()));
				if (!txtRuc.getText().toString().isEmpty()) {
					pd.show();
					Cliente cliente = new Data(PagarActivity.this).getCienteByRuc(txtRuc.getText().toString());
					if (cliente!=null) {
						txtRazonNombre.setText(cliente.getRazonSocial());
						txtDireccion.setText(cliente.getDireccion());
						SelectSpinnerItemByDistrito(spnDistrito, cliente.getDistrito());
					}
					pd.dismiss();
				}
			}
		});
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(viewCliente);
		builder.setCancelable(false);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){
			@Override
	        public void onClick(DialogInterface dialog, int which) {
				if (txtRuc.getText().toString().isEmpty()) {
					Util.showDialogAdvertencia(PagarActivity.this, "Debe ingresar el numero de RUC");
					txtRuc.requestFocus();
				} else if (txtRuc.getText().toString().length()>11 || txtRuc.getText().toString().length()<8) {
					Util.showDialogAdvertencia(PagarActivity.this, "Debe ingresar un RUC valido");
					txtRuc.requestFocus();
				} else if (txtRazonNombre.getText().toString().isEmpty()) {
					Util.showDialogAdvertencia(PagarActivity.this, "Debe ingresar la Razon Social");
					txtRazonNombre.requestFocus();
				} else if (txtDireccion.getText().toString().isEmpty()) {
					Util.showDialogAdvertencia(PagarActivity.this, "Debe ingresar la direccion");
					txtDireccion.requestFocus();
				} else {
					Cliente cliente = new Cliente();
					cliente.setRuc(txtRuc.getText().toString());
					cliente.setRazonSocial(txtRazonNombre.getText().toString());
					cliente.setDireccion(txtDireccion.getText().toString());
					cliente.setDistrito((Distrito) spnDistrito.getSelectedItem());
					new Data(PagarActivity.this).addCliente(cliente);
					tvRuc.setText(txtRuc.getText().toString());
					tvRazonSocial.setText(txtRazonNombre.getText().toString());
					PEDIDO.setCliente(cliente);
					new EditPedidoTask(PagarActivity.this, PEDIDO, false).execute();
				}
	        }
		});
		builder.setNegativeButton(R.string.cancelar, null);
		builder.show();
	}
	
	public static void SelectSpinnerItemById(Spinner spinner, int id) {
	    SpinnerAdapter adapter = spinner.getAdapter();
	    for (int pos = 0; pos < adapter.getCount(); pos++) {
	    	Departamento depa = (Departamento) adapter.getItem(pos);
	        if(depa.getId() == id) {
	            spinner.setSelection(pos);
	            return;
	        }
	    }
	}
	
	public static void SelectSpinnerItemByDistrito(Spinner spinner, Distrito distrito) {
	    SpinnerAdapter adapter = spinner.getAdapter();
	    for (int pos = 0; pos < adapter.getCount(); pos++) {
	    	Distrito dis = (Distrito) adapter.getItem(pos);
	        if(dis==distrito) {
	            spinner.setSelection(pos);
	            return;
	        }
	    }
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		Object obj = parent.getItemAtPosition(pos);
		
		if (obj instanceof Departamento) {
			//Log.e("CONTAR", ((Departamento)obj).getProvincias().toString());
			ArrayAdapter<Provincia> spinner_adapter = new ArrayAdapter<Provincia>(this, android.R.layout.simple_spinner_item, ((Departamento)obj).getProvincias());
			spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnProvincia.setAdapter(spinner_adapter);
		} if (obj instanceof Provincia) {
			ArrayAdapter<Distrito> spinner_adapter = new ArrayAdapter<Distrito>(this, android.R.layout.simple_spinner_item, ((Provincia)obj).getDistritos());
			spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnDistrito.setAdapter(spinner_adapter);
		} if (obj instanceof Distrito) {
			
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}
