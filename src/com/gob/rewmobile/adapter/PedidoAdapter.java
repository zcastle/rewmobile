package com.gob.rewmobile.adapter;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.fortysevendeg.android.swipelistview.SwipeListView;
import com.gob.rewmobile.R;
import com.gob.rewmobile.model.Producto;
import com.gob.rewmobile.objects.Pedido;
import com.gob.rewmobile.util.Data;

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

public class PedidoAdapter extends BaseAdapter {

	private Context context;
	protected LayoutInflater layoutInflater;
	private Pedido pedido;
	protected ArrayList<Producto> productos;
	private TextView txtMontoTotalMesa;

	public PedidoAdapter(Context context, Pedido pedido, TextView txtMontoTotalMesa) {
		this.context = context;
		this.pedido = pedido;
		this.productos = pedido.getProducto();
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

	public int getCount() {
		return productos.size();
	}

	public Producto getItem(int i) {
		return productos.get(i);
	}

	public long getItemId(int i) {
		return (long) i;
	}

	public View getView(int position, View convertView, final ViewGroup parent) {
		
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.pedido_item, parent, false);
		}

		final Producto producto = getItem(position);
		
		TextView txtEstado, txtProducto, txtCantidad, txtUnidad, txtTotal, btnEliminar, btnEditar;
		
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
		txtCantidad.setText(producto.getCantidad().toString());
		txtUnidad.setText(producto.getPrecio().toString());
		txtTotal.setText(producto.getTotal().toString());

		((SwipeListView) parent).recycle(convertView, position);

		btnEliminar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				View viewDialogClave = layoutInflater.inflate(R.layout.layout_dialog_password, null);
				final EditText txtPassword = (EditText) viewDialogClave.findViewById(R.id.password);
				
				builder.setView(viewDialogClave);
				builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						if (txtPassword.getText().toString().equals("adm")) {
							final JSONObject obj = new JSONObject();
							try {
								obj.put("id", producto.getIdAtencion());
								Data data = new Data(context);
								data.deletePedido(obj);
							} catch (JSONException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							}
							removeItem(producto);
							notifyDataSetChanged();
							((SwipeListView) parent).closeOpenedItems();
							txtMontoTotalMesa.setText("TOTAL S/. " + pedido.getTotal());
							Toast.makeText(context, producto.getNombre().concat(" Removido"), Toast.LENGTH_SHORT).show();
						} else {
							((SwipeListView) parent).closeOpenedItems();
							Toast.makeText(context, "Contraseña Incorrecta", Toast.LENGTH_SHORT).show();
						}
					}
				});
				builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						((SwipeListView) parent).closeOpenedItems();
					}
				});
				builder.show();
			}
		});

		btnEditar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				View viewDialogEditar = layoutInflater.inflate(R.layout.layout_dialog_editar, null);
				final EditText txtCantidad = (EditText) viewDialogEditar.findViewById(R.id.txtCantidad);
				final EditText txtNombre = (EditText) viewDialogEditar.findViewById(R.id.txtNombre);
				final EditText txtMensaje = (EditText) viewDialogEditar.findViewById(R.id.txtMensaje);
				
				txtCantidad.setText(producto.getCantidad().toString());
				txtNombre.setText(producto.getNombre());
				txtMensaje.setText(producto.getMensaje());
				builder.setView(viewDialogEditar);
				builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						final JSONObject obj = new JSONObject();
						try {
							obj.put("id", producto.getIdAtencion());
							obj.put("cant", txtCantidad.getText());
							obj.put("prod", txtNombre.getText());
							obj.put("msg", txtMensaje.getText());
							obj.put("precio", producto.getPrecio());
							Data data = new Data(context);
							data.updatePedido(obj);
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						producto.setCantidad(Double.parseDouble(txtCantidad.getText().toString()));
						producto.setNombre(txtNombre.getText().toString());
						producto.setMensaje(txtMensaje.getText().toString());
						notifyDataSetChanged();
						
						((SwipeListView) parent).closeOpenedItems();
						txtMontoTotalMesa.setText("TOTAL S/. " + pedido.getTotal());
						Toast.makeText(context, producto.getNombre().concat(" Actualizado"), Toast.LENGTH_SHORT).show();
					}
				});
				builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						((SwipeListView) parent).closeOpenedItems();
					}
				});
				builder.show();
			}
		});

		return convertView;
	}

}