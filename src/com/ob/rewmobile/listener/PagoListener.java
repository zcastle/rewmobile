package com.ob.rewmobile.listener;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
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
import android.widget.Toast;

import com.ob.rewmobile.PagarActivity;
import com.ob.rewmobile.R;
import com.ob.rewmobile.adapter.PagoAdapter;
import com.ob.rewmobile.adapter.PedidoAdapter;
import com.ob.rewmobile.model.Cliente;
import com.ob.rewmobile.model.Departamento;
import com.ob.rewmobile.model.Distrito;
import com.ob.rewmobile.model.Pago;
import com.ob.rewmobile.model.PedidoController;
import com.ob.rewmobile.model.Provincia;
import com.ob.rewmobile.model.Tarjeta;
import com.ob.rewmobile.task.EditPedidoTask;
import com.ob.rewmobile.util.Data;
import com.ob.rewmobile.util.DialogCarga;
import com.ob.rewmobile.util.Globals;
import com.ob.rewmobile.util.Util;

public class PagoListener extends GestureDetector.SimpleOnGestureListener implements View.OnClickListener, OnItemSelectedListener {

	private Context context;
	private PedidoController PEDIDO;
	
	private Spinner spnDepartamento;
	private Spinner spnProvincia;
	private Spinner spnDistrito;
	private EditText txtRuc;
	private EditText txtRazonNombre;
	private EditText txtDireccion;

	public PagoListener(Context context, PedidoController PEDIDO) {
		super();
		this.context = context;
		this.PEDIDO = PEDIDO;
	}

	@Override
    public boolean onDown(MotionEvent e) {return true;}
	
	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		new AlertDialog.Builder(context)
		.setMessage("Procesar Pago, ¿Continuar?")
		.setCancelable(false)
		.setPositiveButton(R.string.si, new DialogInterface.OnClickListener(){
			@Override
	        public void onClick(DialogInterface dialog, int which) {}
		})
		.setNegativeButton(R.string.no, null)
	    .show();
		return super.onSingleTapConfirmed(e);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {return super.onSingleTapUp(e);}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		new AlertDialog.Builder(context)
		.setMessage("Venta Rapida, ¿Continuar?")
		.setCancelable(false)
		.setPositiveButton(R.string.si, new DialogInterface.OnClickListener(){
			@Override
	        public void onClick(DialogInterface dialog, int which) {
				if(!new Data(context).pagarCuenta(PEDIDO, Globals.VENTA_RAPIDA, false)) {
					new AlertDialog.Builder(context)
					.setMessage("No se pudo realizar la venta")
					.setCancelable(false)
					.setPositiveButton(R.string.cerrar, null)
				    .show();
				} else {
					Toast.makeText(context, "Venta Procesada", Toast.LENGTH_SHORT).show();
				}
	        }
		})
		.setNegativeButton(R.string.no, null)
	    .show();
		return super.onDoubleTap(e);
	}

	@Override
	public void onClick(View v) { 
		switch (v.getId()) {
		case R.id.btnBuscarRuc:
			buscarRuc();
			break;
		case R.id.btnRuc:
			showAddRuc();
			break;
		case R.id.btnPagoSoles:
			showPago();
			break;
		case R.id.btnPagoDolares:
			Log.e("BTN", "btnPagoDolares");
			break;
		}
	}
	
	private void showAddRuc() {
		View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_ruc, null);
		txtRuc = (EditText) view.findViewById(R.id.txtRuc);
		txtRazonNombre = (EditText) view.findViewById(R.id.txtRazonNombre);
		txtDireccion = (EditText) view.findViewById(R.id.txtDireccion);
		spnDepartamento = (Spinner) view.findViewById(R.id.spnDepartamento);
		spnProvincia = (Spinner) view.findViewById(R.id.spnProvincia);
		spnDistrito = (Spinner) view.findViewById(R.id.spnDistrito);
		Button btnBuscarRuc = (Button) view.findViewById(R.id.btnBuscarRuc);
		
		spnDepartamento.setOnItemSelectedListener(this);
		spnProvincia.setOnItemSelectedListener(this);
		spnDistrito.setOnItemSelectedListener(this);
		btnBuscarRuc.setOnClickListener(this);
		
		ArrayAdapter<Departamento> spinner_adapter = new ArrayAdapter<Departamento>(context, android.R.layout.simple_spinner_item, Data.ubigeoController.getDepartamentos());
		spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnDepartamento.setAdapter(spinner_adapter);
		SelectSpinnerItemById(spnDepartamento, 1392);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(view);
		builder.setCancelable(false);
		builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener(){
			@Override
	        public void onClick(DialogInterface dialog, int which) {
				if (txtRuc.getText().toString().isEmpty()) {
					Util.showDialogAdvertencia(context, "Debe ingresar el numero de RUC");
					txtRuc.requestFocus();
				} else if (txtRuc.getText().toString().length()>11 || txtRuc.getText().toString().length()<8) {
					Util.showDialogAdvertencia(context, "Debe ingresar un RUC valido");
					txtRuc.requestFocus();
				} else if (txtRazonNombre.getText().toString().isEmpty()) {
					Util.showDialogAdvertencia(context, "Debe ingresar la Razon Social");
					txtRazonNombre.requestFocus();
				} else if (txtDireccion.getText().toString().isEmpty()) {
					Util.showDialogAdvertencia(context, "Debe ingresar la direccion");
					txtDireccion.requestFocus();
				} else {
					Cliente cliente = new Cliente();
					cliente.setRuc(txtRuc.getText().toString());
					cliente.setRazonSocial(txtRazonNombre.getText().toString());
					cliente.setDireccion(txtDireccion.getText().toString());
					cliente.setDistrito((Distrito) spnDistrito.getSelectedItem());
					new Data(context).addCliente(cliente);
					((PagarActivity)context).tvRuc.setText(txtRuc.getText().toString());
					((PagarActivity)context).tvRazonSocial.setText(txtRazonNombre.getText().toString());
					PEDIDO.setCliente(cliente);
					new EditPedidoTask(context, PEDIDO, false).execute();
				}
	        }
		});
		builder.setNegativeButton(R.string.cancelar, null);
		builder.show();
	}
	
	private void buscarRuc() {
		ProgressDialog pd = new DialogCarga(context, "Buscando ".concat(txtRuc.getText().toString()));
		if (!txtRuc.getText().toString().isEmpty()) {
			pd.show();
			Cliente cliente = new Data(context).getCienteByRuc(txtRuc.getText().toString());
			if (cliente!=null) {
				txtRazonNombre.setText(cliente.getRazonSocial());
				txtDireccion.setText(cliente.getDireccion());
				SelectSpinnerItemByDistrito(spnDistrito, cliente.getDistrito());
			}
			pd.dismiss();
		}
	}
	
	private void showPago() {
		View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_pago, null);
		final Spinner spnTarjetas = (Spinner) view.findViewById(R.id.spnTarjetas);
		final EditText txtValor = (EditText) view.findViewById(R.id.txtValor);
		final ListView lvPagos = (ListView) view.findViewById(R.id.lvPagos);
		Button btnAceptar = (Button) view.findViewById(R.id.btnAceptar);
		
		ArrayAdapter<Tarjeta> spinner_adapter = new ArrayAdapter<Tarjeta>(context, android.R.layout.simple_spinner_item, Data.tarjetaController);
		spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnTarjetas.setAdapter(spinner_adapter);
		
		lvPagos.setAdapter(new PagoAdapter(context, PEDIDO));
		
		btnAceptar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Tarjeta tarjeta = (Tarjeta) spnTarjetas.getSelectedItem();
				((PagoAdapter) lvPagos.getAdapter()).addItem(new Pago(tarjeta.getNombre(), Double.parseDouble(txtValor.getText().toString()), 0.0));
				txtValor.setText("0.0");
				txtValor.requestFocus();
			}
		});
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(view);
		builder.setCancelable(false);
		builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener(){
			@Override
	        public void onClick(DialogInterface dialog, int which) {
	        }
		});
		builder.setNegativeButton(R.string.cancelar, null);
		builder.show();
	}

	private void SelectSpinnerItemById(Spinner spinner, int id) {
	    SpinnerAdapter adapter = spinner.getAdapter();
	    for (int pos = 0; pos < adapter.getCount(); pos++) {
	    	Departamento depa = (Departamento) adapter.getItem(pos);
	        if(depa.getId() == id) {
	            spinner.setSelection(pos);
	            return;
	        }
	    }
	}
	
	private void SelectSpinnerItemByDistrito(Spinner spinner, Distrito distrito) {
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
			ArrayAdapter<Provincia> spinner_adapter = new ArrayAdapter<Provincia>(context, android.R.layout.simple_spinner_item, ((Departamento)obj).getProvincias());
			spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnProvincia.setAdapter(spinner_adapter);
		} if (obj instanceof Provincia) {
			ArrayAdapter<Distrito> spinner_adapter = new ArrayAdapter<Distrito>(context, android.R.layout.simple_spinner_item, ((Provincia)obj).getDistritos());
			spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spnDistrito.setAdapter(spinner_adapter);
		} if (obj instanceof Distrito) {
			
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {}

}