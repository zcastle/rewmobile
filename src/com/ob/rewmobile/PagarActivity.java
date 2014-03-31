package com.ob.rewmobile;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ob.rewmobile.adapter.PedidoAdapterPagar;
import com.ob.rewmobile.listener.PagoListener;
import com.ob.rewmobile.model.PedidoController;
import com.ob.rewmobile.util.Globals;
import com.ob.rewmobile.util.Util;

public class PagarActivity extends Activity {

	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerTogglePedido;
	private ListView listViewPedido;
	
	public TextView tvRuc;
	public TextView tvRazonSocial;
	private PedidoController PEDIDO;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pagar);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		PEDIDO = Globals.PEDIDO_PAGAR;
		
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_pagar);
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		listViewPedido = (ListView) findViewById(R.id.listViewPedido);
		listViewPedido.setAdapter(new PedidoAdapterPagar(this, PEDIDO));
		
		drawerTogglePedido = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        drawerLayout.setDrawerListener(drawerTogglePedido);
        
        TextView txtMontoPagar = (TextView) findViewById(R.id.tvMontoPagar);
        TextView txtResta = (TextView) findViewById(R.id.tvResta);
        txtMontoPagar.setText(Globals.MONEDA_SIMBOLO+Util.format(PEDIDO.getTotal()));
        txtResta.setText(Globals.MONEDA_SIMBOLO+Util.format(PEDIDO.getTotal()));

        tvRuc = (TextView) findViewById(R.id.tvRuc);
        tvRazonSocial = (TextView) findViewById(R.id.tvRazonSocial);
        TextView btnRuc = (TextView) findViewById(R.id.btnRuc);
        TextView btnPagar = (TextView) findViewById(R.id.btnPagar);
        TextView btnPagoSoles = (TextView) findViewById(R.id.btnPagoSoles);
        TextView btnPagoDolares = (TextView) findViewById(R.id.btnPagoDolares);
        
        PagoListener pagoListener = new PagoListener(this, PEDIDO);
        btnRuc.setOnClickListener(pagoListener);
        btnPagar.setOnClickListener(pagoListener);
        btnPagoSoles.setOnClickListener(pagoListener);
        btnPagoDolares.setOnClickListener(pagoListener);
        
        final GestureDetector gestureDetector;
        gestureDetector = new GestureDetector(this, pagoListener);
        
        btnPagar.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		});
        
        if (PEDIDO.getCliente()!=null) {
        	tvRuc.setText(PEDIDO.getCliente().getRuc());
        	tvRazonSocial.setText(PEDIDO.getCliente().getRazonSocial());
        }
        
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerTogglePedido.syncState();
	}

}
