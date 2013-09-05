package com.gob.rewmobile.objects;

import java.util.ArrayList;

import com.gob.rewmobile.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {

    private Context fContext;
    protected LayoutInflater fInflater;
    protected ArrayList<Producto> producto;

    public ListAdapter(Context aContext, ArrayList<Producto> producto) {
        fContext = aContext;
        this.producto = producto;
        fInflater = (LayoutInflater) fContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return producto.size();
    }

    public Object getItem(int i) {
        return producto.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View lView = convertView;
        if (lView == null) {
            lView = fInflater.inflate(R.layout.pedido_item, parent, false);
        }

        Producto producto = (Producto) getItem(position);
        TextView txtProducto = (TextView) lView.findViewById(R.id.txtProducto);
        TextView txtCantidad = (TextView) lView.findViewById(R.id.txtCantidad);
        TextView txtUnidad = (TextView) lView.findViewById(R.id.txtUnitario);
        TextView txtTotal = (TextView) lView.findViewById(R.id.txtTotal);

        txtProducto.setText(producto.getNombre());
        txtCantidad.setText(producto.getCantidad()+"");
        txtUnidad.setText(producto.getPrecio()+"");
        txtTotal.setText(producto.getTotal()+"");

        return lView;
    }

}