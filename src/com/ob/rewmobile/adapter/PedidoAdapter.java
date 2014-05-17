package com.ob.rewmobile.adapter;

import java.util.ArrayList;

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
	protected ArrayList<Producto> productos;
	PedidoController PEDIDO;

	public PedidoAdapter(Context context, PedidoController PEDIDO) {
		this.context = context;
		this.productos = PEDIDO.getProductos();
		this.PEDIDO = PEDIDO;
		this.layoutInflater = LayoutInflater.from(context);
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

	public void removeItem(PedidoController pedido) {
		for (Producto producto: pedido.getProductos()) {
			productos.remove(producto);
		}
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
					builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
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
					new AlertDialog.Builder(context)
					.setMessage("¿Esta seguro de querer eliminar el producto?")
					.setCancelable(false)
					.setPositiveButton(R.string.si, new DialogInterface.OnClickListener(){
						@Override
				        public void onClick(DialogInterface dialog, int which) {
							removerItem(producto, parent, position);
				        }
					})
					.setNegativeButton(R.string.no, new DialogInterface.OnClickListener(){
						@Override
				        public void onClick(DialogInterface dialog, int which) {
							closeAnimation(parent, position);
				        }
					})
				    .show();
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
				builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						
						if(txtCantidad.getText().toString().isEmpty()) {
							Toast.makeText(context, "Ingrese Cantidad", Toast.LENGTH_SHORT).show();
							txtCantidad.requestFocus();
							return;
						}
						if(txtCantidad.getText().toString().equals("0")) {
							Toast.makeText(context, "No se puede ingresar cantidad en cero", Toast.LENGTH_SHORT).show();
							txtCantidad.requestFocus();
							return;
						}
						
						if(txtNombre.getText().toString().isEmpty()) {
							Toast.makeText(context, "Nombre de producto no puede ser estar blanco", Toast.LENGTH_SHORT).show();
							txtCantidad.requestFocus();
							return;
						}
						
						closeAnimation(parent, position);
						producto.setCantidad(Double.parseDouble(txtCant.getText().toString()));
						producto.setNombre(txtNombre.getText().toString());
						producto.setMensaje(txtMensaje.getText().toString());
						txtProducto.setText(producto.getNombre());
						txtCantidad.setText(Util.format(producto.getCantidad()));
						txtTotal.setText(Util.format(producto.getTotal()));
						//producto.setPrecio(Double.parseDouble(txtCantidad.getText().toString()));
						new EditProductoTask(context, producto, false).execute();
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
		PedidoController pedido = new PedidoController(this.PEDIDO.getMesa());
		pedido.setMozo(this.PEDIDO.getMozo());
		pedido.getProductos().add(producto);
		new DelProductoTask(context, pedido, false).execute();
		//removeItem(producto);
		closeAnimation(parent, position);
	}
	
	private void closeAnimation(ViewGroup parent, int position) {
		try {
			//((SwipeListView) parent).closeAnimate(position-1);
			((SwipeListView) parent).closeOpenedItems();
			notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}