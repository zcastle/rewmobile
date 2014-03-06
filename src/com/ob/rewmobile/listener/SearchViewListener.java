package com.ob.rewmobile.listener;

import com.ob.rewmobile.adapter.ProductoAdapter;
import com.ob.rewmobile.util.Data;

import android.app.Activity;
import android.widget.ListView;
import android.widget.SearchView;

public class SearchViewListener implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

	private Activity activity;
	private ListView listViewProductos;
	
	public SearchViewListener(Activity activity, ListView listViewProductos) {
		this.activity = activity;
		this.listViewProductos = listViewProductos;
	}

	@Override
	public boolean onClose() {
		listViewProductos.setAdapter(new ProductoAdapter(activity, Data.productoController.getProductos()));
        return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if (!newText.isEmpty()){
            displayResults(newText);
        } else {
        	listViewProductos.setAdapter(new ProductoAdapter(activity, Data.productoController.getProductos()));
        }
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		return false;
	}
	
	private void displayResults(String nombre) {
		listViewProductos.setAdapter(new ProductoAdapter(activity, Data.productoController.getProductoContainName(nombre)));
    }

}
