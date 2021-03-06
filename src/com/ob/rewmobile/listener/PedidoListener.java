package com.ob.rewmobile.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ob.rewmobile.PagarActivity;
import com.ob.rewmobile.PedidoActivity;
import com.ob.rewmobile.R;
import com.ob.rewmobile.model.PedidoController;
import com.ob.rewmobile.model.Producto;
import com.ob.rewmobile.model.Usuario;
import com.ob.rewmobile.task.AddProductoTask;
import com.ob.rewmobile.task.EditPedidoTask;
import com.ob.rewmobile.task.EnviarPedidoTask;
import com.ob.rewmobile.task.EnviarPrecuentaTask;
import com.ob.rewmobile.task.LoadPedidoTask;
import com.ob.rewmobile.util.App;
import com.ob.rewmobile.util.Data;
import com.ob.rewmobile.util.Globals;
import com.ob.rewmobile.util.Util;

public class PedidoListener implements OnClickListener, OnItemClickListener {
	
	private TextView tvMesa;
	private TextView tvCajero;
	private TextView tvMozo;
	private TextView tvPax;
	private TextView tvTotal;
	private Context context;
	private PedidoController PEDIDO;
	
	public PedidoListener(Context context, PedidoController PEDIDO, View viewActionBar) {
		super();
		this.context = context;
		this.PEDIDO = PEDIDO;
		this.tvMesa = (TextView) viewActionBar.findViewById(R.id.tvMesa);
		this.tvCajero = (TextView) viewActionBar.findViewById(R.id.tvCajero);
		TextView tvCajeroGuion = (TextView) viewActionBar.findViewById(R.id.tvCajeroGuion);
		this.tvMozo = (TextView) viewActionBar.findViewById(R.id.tvMozo);
		this.tvPax = (TextView) viewActionBar.findViewById(R.id.tvPax);
		this.tvTotal = (TextView) viewActionBar.findViewById(R.id.tvMontoTotalMesa);
		if(App.isCaja()) {
			tvCajero.setVisibility(TextView.VISIBLE);
			tvCajeroGuion.setVisibility(TextView.VISIBLE);
		}
		this.tvMesa.setOnClickListener(this);
		this.tvCajero.setOnClickListener(this);
		this.tvMozo.setOnClickListener(this);
		this.tvPax.setOnClickListener(this);
		this.tvTotal.setOnClickListener(this);
		//refresh();
	}
	
	public void refresh() {
		tvMesa.setText("Mesa: ".concat(PEDIDO.getMesa()));
		if (PEDIDO.getMozo().getId()>0) {
			tvMozo.setText("Mozo: ".concat(PEDIDO.getMozo().getNombre()));
		}
		//tvMozo.setText("Mozo: ");
		if (App.isCaja()) {
			setCajero(PEDIDO.getCajero().getNombre());
		} else if (App.isPedido()) {
			setCajero("*");
		}
		if (PEDIDO.getProductos().size()==0) {
			setPax(1);
		} else {
			setPax(PEDIDO.getPax());
		}
		tvTotal.setText("Total S/. " + Util.format(PEDIDO.getTotal()));
	}

	public void setCajero(String cajero) {
		tvCajero.setText("Cajero: ".concat(cajero));
	}

	public void setPax(int pax) {
		tvPax.setText("Pax: ".concat(pax+""));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tvMesa:
				LoadPedido();
				break;
			case R.id.tvCajero:
				//
				break;
			case R.id.tvMozo:
				changeMozo();
				break;
			case R.id.tvPax:
				changePax();
				break;
			case R.id.tvMontoTotalMesa:
				verTotales();
				break;
		}
	}

	private void LoadPedido() {
		View viewSelectMesa = LayoutInflater.from(context).inflate(R.layout.layout_select_mesa, null);
		final EditText txtMesa = (EditText) viewSelectMesa.findViewById(R.id.txtMesa);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(viewSelectMesa);
		builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				PEDIDO = new PedidoController(txtMesa.getText().toString());
				if (App.isCaja()) {
					PEDIDO.setCajero(Globals.USUARIO_LOGIN);
					PEDIDO.setMozo(new Usuario(0, "*"));
				} else {
					PEDIDO.setMozo(Globals.USUARIO_LOGIN);
					PEDIDO.setCajero(new Usuario(0, "*"));
				}
				new LoadPedidoTask(context, PEDIDO).execute();
			}
		});
		builder.setNegativeButton(R.string.cancelar, null);
		builder.show();
	}
	
	private void changePax() {
		if (!PEDIDO.getMesa().equals("0")) {
			View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_pax, null);
			final NumberPicker npPax = (NumberPicker) view.findViewById(R.id.npPax);
			String[] nums = new String[100];
		    for(int i=0; i<nums.length; i++)
		    	nums[i] = Integer.toString(i+1);

		    npPax.setMinValue(1);
		    npPax.setMaxValue(nums.length);
		    npPax.setWrapSelectorWheel(false);
		    npPax.setDisplayedValues(nums);
		    //Log.e("E", PEDIDO.getPax()+"");
		    npPax.setValue(PEDIDO.getPax());
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setView(view);
			builder.setTitle("PAX -> MESA: ".concat(PEDIDO.getMesa()));
			builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					PEDIDO.setPax(npPax.getValue());
					new EditPedidoTask(context, PEDIDO, false).execute();
					setPax(PEDIDO.getPax());
				}
			});
			builder.setNegativeButton(R.string.cancelar, null);
			builder.show();
			
		}
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
			View viewTotales = LayoutInflater.from(context).inflate(R.layout.layout_totales, null);
			((TextView)viewTotales.findViewById(R.id.tvNeto)).setText(Util.format(sTotal));
			((TextView)viewTotales.findViewById(R.id.tvIgv)).setText(Util.format(igv));
			((TextView)viewTotales.findViewById(R.id.tvServ)).setText(Util.format(serv));
			((TextView)viewTotales.findViewById(R.id.tvServData)).setText("SERV. ("+Globals.VA_SERV+"%)");
			((TextView)viewTotales.findViewById(R.id.tvTotal)).setText(Util.format(total));
			new AlertDialog.Builder(context)
			.setTitle("TOTALES -> MESA: ".concat(PEDIDO.getMesa()))
			.setIcon(R.drawable.ic_launcher)
			.setView(viewTotales)
			.setPositiveButton(R.string.cerrar, null)
			.show();
		}		
	}
	
	public void enviar() {
		if(!PEDIDO.getMesa().equals("0")){
			new AlertDialog.Builder(context)
			.setMessage("¿Esta seguro de querer enviar el pedido?")
			.setCancelable(false)
			.setPositiveButton(R.string.si, new DialogInterface.OnClickListener(){
				@Override
		        public void onClick(DialogInterface dialog, int which) {
					PedidoActivity activity = (PedidoActivity) context;
					new EnviarPedidoTask(context, PEDIDO, activity.swipeListView, activity.tvMontoTotalMesa).execute();
		        }
			})
			.setNegativeButton(R.string.no, null)
		    .show();
		} else {
			Toast.makeText(context, "No se puede ENVIAR en la mesa 0", Toast.LENGTH_SHORT).show();
		}
	}

	private void changeMozo() {
		if (App.isPedido()) return;
		if (PEDIDO.getMesa().equals("0")) {
			Toast.makeText(context, "Debe ingresar a una mesa", Toast.LENGTH_SHORT).show();
			return;
		}
		
		View viewEditMesa = LayoutInflater.from(context).inflate(R.layout.layout_edit_mesa, null);
		
		if (App.isCaja()) {
			LinearLayout llMozo = (LinearLayout) viewEditMesa.findViewById(R.id.llMozo);
			llMozo.setVisibility(LinearLayout.VISIBLE);
		}
		
		final EditText txtPax = (EditText) viewEditMesa.findViewById(R.id.txtPax);
		txtPax.setText(PEDIDO.getPax()+"");
		final Spinner spinner = (Spinner) viewEditMesa.findViewById(R.id.spnMozos);
		ArrayAdapter<Usuario> spinner_adapter = new ArrayAdapter<Usuario>(context, android.R.layout.simple_spinner_item, Data.usuarioController.getUsuariosByRol(Globals.ROL_MOZO));
		spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(spinner_adapter);
		
		if(PEDIDO.getMozo().getId()>0) {
			SelectSpinnerItemByObject(spinner, PEDIDO.getMozo());
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
				new EditPedidoTask(context, PEDIDO, false).execute();
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
	
	public void precuenta() {
		if(!PEDIDO.getMesa().equals("0")){
			new EnviarPrecuentaTask(context, PEDIDO).execute();
		} else {
			Toast.makeText(context, "No se puede enviar PRECUENTA en la mesa 0", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void pagar() {
		if (PEDIDO.getMesa().equals("0")) {
			Util.showDialogAdvertencia(context, "No hay un pedido para procesar");
			return;
		} else if (PEDIDO.getMozo().getId()==0) {
			Util.showDialogAdvertencia(context, "Debe especificar un mozo");
			return;
		}
		
		Globals.PEDIDO_PAGAR = PEDIDO;
		context.startActivity(new Intent(context, PagarActivity.class));
	}
	
	private void addProducto(Producto producto) {
		if (PEDIDO.getMesa().equals("0")) {
			Toast.makeText(context, "Debe ingresar el numero de MESA", Toast.LENGTH_LONG).show();
		} else {
			new AddProductoTask(context, PEDIDO, producto, false).execute();
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Producto producto = (Producto) parent.getItemAtPosition(position);
		Producto newProducto = null;
		try {
			newProducto = (Producto) producto.clone();
			addProducto(newProducto);
		} catch (CloneNotSupportedException e1) {
			e1.printStackTrace();
		}
	}
}
