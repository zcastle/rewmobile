package com.ob.rewmobile.adapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fortysevendeg.android.swipelistview.SwipeListView;
import com.ob.rewmobile.R;
import com.ob.rewmobile.model.PedidoController;
import com.ob.rewmobile.model.Producto;
import com.ob.rewmobile.task.DelProductoTask;
import com.ob.rewmobile.task.EditProductoTask;
import com.ob.rewmobile.util.Util;

public class PedidoAdapter extends BaseAdapter {

	private Context context;
	protected LayoutInflater layoutInflater;
	private PedidoController pedido;
	protected ArrayList<Producto> productos;
	private TextView txtMontoTotalMesa;

	public PedidoAdapter(Context context, PedidoController pedido, TextView txtMontoTotalMesa) {
		this.context = context;
		this.pedido = pedido;
		this.productos = pedido.getProductos();
		this.layoutInflater = LayoutInflater.from(context);
		this.txtMontoTotalMesa = txtMontoTotalMesa; 
	}

	public void addItem(Producto producto) {
		boolean isEdit = false;
		for (Producto p : productos) {
			if (p.getId() == producto.getId() && !p.isEnviado()) { 
				p.setCantidad(p.getCantidad() + 1);
				isEdit = true;
			}
		}
		if (!isEdit) {
			this.productos.add(producto);
		}
		notifyDataSetChanged();
	}

	public void removeItem(Producto producto) {
		productos.remove(producto);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return productos.size();
	}

	@Override
	public Producto getItem(int i) {
		return productos.get(i);
	}

	@Override
	public long getItemId(int i) {
		return (long) i;
	}

	public View getView(final int position, View convertView, final ViewGroup parent) {
		
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.pedido_item, parent, false);
		}

		final Producto producto = getItem(position);
		
		TextView txtEstado;
		final TextView txtProducto;
		final TextView txtCantidad;
		TextView txtUnidad;
		final TextView txtTotal;
		TextView btnEliminar, btnEditar;
		
		txtEstado = (TextView) convertView.findViewById(R.id.txtEstado);
		txtProducto = (TextView) convertView.findViewById(R.id.txtProducto);
		txtCantidad = (TextView) convertView.findViewById(R.id.txtCantidad);
		txtUnidad = (TextView) convertView.findViewById(R.id.txtUnitario);
		txtTotal = (TextView) convertView.findViewById(R.id.txtTotal);

		btnEliminar = (TextView) convertView.findViewById(R.id.btnEliminar);
		btnEditar = (TextView) convertView.findViewById(R.id.btnEditar);

		if (producto.isEnviado()) {
			txtEstado.setBackgroundResource(android.R.color.holo_orange_light);
		} else {
			txtEstado.setBackgroundResource(android.R.color.holo_green_light);
		}

		txtProducto.setText(producto.getNombre());
		txtCantidad.setText(Util.format(producto.getCantidad()));
		txtUnidad.setText(Util.format(producto.getPrecio()));
		txtTotal.setText(Util.format(producto.getTotal()));

		((SwipeListView) parent).recycle(convertView, position);

		btnEliminar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (producto.isEnviado()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					View viewDialogClave = layoutInflater.inflate(R.layout.layout_dialog_password, null);
					final EditText txtPassword = (EditText) viewDialogClave.findViewById(R.id.password);
					builder.setView(viewDialogClave);
					builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							if (txtPassword.getText().toString().equals("adm")) {
								removerItem(producto, parent, position);
							} else {
								closeAnimation(parent, position);
								Toast.makeText(context, "Contraseña Incorrecta", Toast.LENGTH_SHORT).show();
							}
						}
					});
					builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							closeAnimation(parent, position);
						}
					});
					builder.show();
				} else {
					removerItem(producto, parent, position);
				}
			}
		});

		btnEditar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				View viewDialogEditar = layoutInflater.inflate(R.layout.layout_dialog_editar, null);
				final EditText txtCant = (EditText) viewDialogEditar.findViewById(R.id.txtCantidad);
				final EditText txtNombre = (EditText) viewDialogEditar.findViewById(R.id.txtNombre);
				final EditText txtMensaje = (EditText) viewDialogEditar.findViewById(R.id.txtMensaje);
				
				txtCant.setText(producto.getCantidad().toString());
				txtNombre.setText(producto.getNombre());
				txtMensaje.setText(producto.getMensaje());
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setView(viewDialogEditar);
				builder.setCancelable(false);
				builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						closeAnimation(parent, position);
						producto.setCantidad(Double.parseDouble(txtCant.getText().toString()));
						producto.setNombre(txtNombre.getText().toString());
						producto.setMensaje(txtMensaje.getText().toString());
						txtProducto.setText(producto.getNombre());
						txtCantidad.setText(Util.format(producto.getCantidad()));
						txtTotal.setText(Util.format(producto.getTotal()));
						//producto.setPrecio(Double.parseDouble(txtCantidad.getText().toString()));
						new EditProductoTask(context, producto, false).execute();
						//((SwipeListView) parent).closeOpenedItems();
						txtMontoTotalMesa.setText("TOTAL S/. " + Util.format(pedido.getTotal()));
						
						//Toast.makeText(context, producto.getNombre().concat(" Actualizado"), Toast.LENGTH_SHORT).show();
					}
				});
				builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						((SwipeListView) parent).closeOpenedItems();
					}
				});
				builder.show();
			}
		});

		return convertView;
	}
	
	private void removerItem(Producto producto, ViewGroup parent, int position){
		try {
			if(new DelProductoTask(context, producto, false).execute().get()) {
				removeItem(producto);
				closeAnimation(parent, position);
				txtMontoTotalMesa.setText("TOTAL S/. " + Util.format(pedido.getTotal()));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	private void closeAnimation(ViewGroup parent, int position) {
		((SwipeListView) parent).closeAnimate(position);
		((SwipeListView) parent).closeOpenedItems();
	}

}