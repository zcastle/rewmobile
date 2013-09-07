package com.gob.rewmobile.objects;

import java.util.ArrayList;

import com.fortysevendeg.android.swipelistview.SwipeListView;
import com.gob.rewmobile.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {

    private Context fContext;
    protected LayoutInflater fInflater;
    protected ArrayList<Producto> lstProducto;

    public ListAdapter(Context aContext, ArrayList<Producto> producto) {
        fContext = aContext;
        lstProducto = producto;
        fInflater = (LayoutInflater) fContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

    public View getView(int position, View convertView, ViewGroup parent) {
        View lView = convertView;
        Button bAction1, bAction2, bAction3;
        if (lView == null) {
            lView = fInflater.inflate(R.layout.pedido_item, parent, false);
        }

        Producto producto = (Producto) getItem(position);
        TextView txtProducto = (TextView) lView.findViewById(R.id.txtProducto);
        TextView txtCantidad = (TextView) lView.findViewById(R.id.txtCantidad);
        TextView txtUnidad = (TextView) lView.findViewById(R.id.txtUnitario);
        TextView txtTotal = (TextView) lView.findViewById(R.id.txtTotal);
        
        bAction1 = (Button) lView.findViewById(R.id.example_row_b_action_1);
        bAction2 = (Button) lView.findViewById(R.id.example_row_b_action_2);
        //bAction3 = (Button) lView.findViewById(R.id.example_row_b_action_3);

        txtProducto.setText(producto.getNombre());
        txtCantidad.setText(producto.getCantidad()+"");
        txtUnidad.setText(producto.getPrecio()+"");
        txtTotal.setText(producto.getTotal()+"");

        ((SwipeListView)parent).recycle(lView, position);
        
        return lView;
    }

}