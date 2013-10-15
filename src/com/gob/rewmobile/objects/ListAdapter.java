package com.gob.rewmobile.objects;

import java.util.ArrayList;

import com.fortysevendeg.android.swipelistview.SwipeListView;
import com.gob.rewmobile.PedidoActivity;
import com.gob.rewmobile.R;
import com.gob.rewmobile.PasswordDialogFragment.PasswordDialogListener;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ListAdapter extends BaseAdapter{

    private Context fContext;
    protected LayoutInflater fInflater;
    protected ArrayList<Producto> lstProducto;

    public ListAdapter(Context aContext, ArrayList<Producto> producto) {
    	this.fContext = aContext;
    	this.lstProducto = producto;
    	this.fInflater = (LayoutInflater) fContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
	public void addItem(Producto producto){
    	boolean isEdit = false;
    	for (Producto p : lstProducto) {
    		if(p.getId()==producto.getId()){
    			p.setCantidad(p.getCantidad()+1);
    			isEdit = true;
    		}
		}
    	if(!isEdit){
    		this.lstProducto.add(producto);
    	}
    	notifyDataSetChanged();
    }
    
    public void removeItem(Producto producto){
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
        Button btnEliminar, btnEditar;
        if (lView == null) {
            lView = fInflater.inflate(R.layout.pedido_item, parent, false);
        }

        Producto producto = (Producto) getItem(position);
        TextView txtProducto = (TextView) lView.findViewById(R.id.txtProducto);
        TextView txtCantidad = (TextView) lView.findViewById(R.id.txtCantidad);
        TextView txtUnidad = (TextView) lView.findViewById(R.id.txtUnitario);
        TextView txtTotal = (TextView) lView.findViewById(R.id.txtTotal);
        
        btnEliminar = (Button) lView.findViewById(R.id.example_row_b_action_1);
        btnEditar = (Button) lView.findViewById(R.id.example_row_b_action_2);

        txtProducto.setText(producto.getNombre());
        txtCantidad.setText(producto.getCantidad()+"");
        txtUnidad.setText(producto.getPrecio()+"");
        txtTotal.setText(producto.getTotal()+"");

        ((SwipeListView)parent).recycle(lView, position);
        
        //btnEliminar.setTag(position);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(fContext);
				View viewDialog = fInflater.inflate(R.layout.layout_dialog_password, null);
				final EditText txtPassword = (EditText) viewDialog.findViewById(R.id.password);
				builder.setView(viewDialog)
					.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							//Log.e("OnCLick", txtPassword.getText().toString());
							if (txtPassword.getText().toString().equals("adm")) {
								removeItem(item);
								notifyDataSetChanged();
								((SwipeListView)parent).closeOpenedItems();
								Toast.makeText(fContext, item.getNombre().concat(" Removido"), Toast.LENGTH_SHORT).show();
							} else {
								((SwipeListView)parent).closeOpenedItems();
								Toast.makeText(fContext, "Contraseņa Incorrecta", Toast.LENGTH_SHORT).show();
							}
						}
					})
					.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							//Log.e("OnCLick", "CANCELARCANCELAR");
						}
					});
				builder.show();
			}
		});
        
        return lView;
    }

}