package com.gob.rewmobile.objects;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.fortysevendeg.android.swipelistview.SwipeListView;
import com.gob.rewmobile.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ListAdapter extends BaseAdapter {

	private Context fContext;
	protected LayoutInflater fInflater;
	protected ArrayList<Producto> lstProducto;
	TextView txtMontoTotalMesa;

	public ListAdapter(Context aContext, ArrayList<Producto> producto, TextView txtMontoTotalMesa) {
		this.fContext = aContext;
		this.lstProducto = producto;
		this.fInflater = (LayoutInflater) fContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.txtMontoTotalMesa = txtMontoTotalMesa; 
	}

	public void addItem(Producto producto) {
		boolean isEdit = false;
		for (Producto p : lstProducto) {
			if (p.getId() == producto.getId()) {
				p.setCantidad(p.getCantidad() + 1);
				isEdit = true;
			}
		}
		if (!isEdit) {
			this.lstProducto.add(producto);
		}
		notifyDataSetChanged();
	}

	public void removeItem(Producto producto) {
		lstProducto.remove(producto);
		notifyDataSetChanged();
	}

	public int getCount() {
		return lstProducto.size();
	}

	public Object getItem(int i) {
		return lstProducto.get(i);
	}

	public long getItemId(int i) {
		return (long) i;
	}

	public View getView(int position, View convertView, final ViewGroup parent) {
		final Producto item = (Producto) getItem(position);
		View lView = convertView;
		TextView btnEliminar, btnEditar;
		if (lView == null) {
			lView = fInflater.inflate(R.layout.pedido_item, parent, false);
		}

		Producto producto = (Producto) getItem(position);
		TextView txtProducto = (TextView) lView.findViewById(R.id.txtProducto);
		TextView txtCantidad = (TextView) lView.findViewById(R.id.txtCantidad);
		TextView txtUnidad = (TextView) lView.findViewById(R.id.txtUnitario);
		TextView txtTotal = (TextView) lView.findViewById(R.id.txtTotal);

		btnEliminar = (TextView) lView.findViewById(R.id.btnEliminar);
		btnEditar = (TextView) lView.findViewById(R.id.btnEditar);

		txtProducto.setText(producto.getNombre());
		txtCantidad.setText(producto.getCantidad() + "");
		txtUnidad.setText(producto.getPrecio() + "");
		txtTotal.setText(producto.getTotal() + "");

		((SwipeListView) parent).recycle(lView, position);

		// btnEliminar.setTag(position);
		btnEliminar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(fContext);
				View viewDialog = fInflater.inflate(
						R.layout.layout_dialog_password, null);
				final EditText txtPassword = (EditText) viewDialog
						.findViewById(R.id.password);
				builder.setView(viewDialog)
						.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int id) {
										// Log.e("OnCLick",
										// txtPassword.getText().toString());
										if (txtPassword.getText().toString().equals("adm")) {
											final JSONObject obj = new JSONObject();
											try {
												Log.e("ID", String.valueOf(item.getIdAtencion()));
												obj.put("id", item.getIdAtencion());
												Data data = new Data(fContext);
												data.deletePedido(obj);
											} catch (JSONException e) {
												e.printStackTrace();
											} catch (IOException e) {
												e.printStackTrace();
											} catch (Exception e) {
												e.printStackTrace();
											}
											removeItem(item);
											notifyDataSetChanged();
											((SwipeListView) parent).closeOpenedItems();
											txtMontoTotalMesa.setText("TOTAL S/. " + Data.PEDIDO.getTotal());
											Toast.makeText(
													fContext,
													item.getNombre().concat(
															" Removido"),
													Toast.LENGTH_SHORT).show();
										} else {
											((SwipeListView) parent)
													.closeOpenedItems();
											Toast.makeText(fContext,
													"Contraseña Incorrecta",
													Toast.LENGTH_SHORT).show();
										}
									}
								})
						.setNegativeButton(R.string.cancelar,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// Log.e("OnCLick", "CANCELARCANCELAR");
										((SwipeListView) parent)
												.closeOpenedItems();
									}
								});
				builder.show();
			}
		});

		btnEditar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(fContext);
				View viewDialog = fInflater.inflate(
						R.layout.layout_dialog_editar, null);
				final EditText txtCantidad = (EditText) viewDialog
						.findViewById(R.id.txtCantidad);
				final EditText txtNombre = (EditText) viewDialog
						.findViewById(R.id.txtNombre);
				final EditText txtMensaje = (EditText) viewDialog
						.findViewById(R.id.txtMensaje);
				txtCantidad.setText(item.getCantidad().toString());
				txtNombre.setText(item.getNombre());
				txtMensaje.setText(item.getMensaje());
				builder.setView(viewDialog)
						.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int id) {
										final JSONObject obj = new JSONObject();
										try {
											obj.put("id", item.getIdAtencion());
											obj.put("cant", txtCantidad.getText());
											obj.put("prod", txtNombre.getText());
											obj.put("msg", txtMensaje.getText());
											obj.put("precio", item.getPrecio());
											Data data = new Data(fContext);
											data.updatePedido(obj);
										} catch (JSONException e) {
											e.printStackTrace();
										} catch (IOException e) {
											e.printStackTrace();
										} catch (Exception e) {
											e.printStackTrace();
										}
										item.setCantidad(Double.parseDouble(txtCantidad.getText().toString()));
										
										item.setNombre(txtNombre.getText().toString());
										
										item.setMensaje(txtMensaje.getText().toString());
										
										notifyDataSetChanged();
										
										((SwipeListView) parent).closeOpenedItems();
										txtMontoTotalMesa.setText("TOTAL S/. " + Data.PEDIDO.getTotal());
										Toast.makeText(
												fContext,
												item.getNombre().concat(
														" Actualizado"),
												Toast.LENGTH_SHORT).show();
									}
								})
						.setNegativeButton(R.string.cancelar,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										((SwipeListView) parent).closeOpenedItems();
									}
								});
				builder.show();
			}
		});

		return lView;
	}

}