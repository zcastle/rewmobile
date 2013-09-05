package com.gob.rewmobile.objects;

import com.gob.rewmobile.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MTableRow extends LinearLayout {

	private Producto producto;
	
	public MTableRow(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MTableRow(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public MTableRow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
		setBackgroundResource(R.drawable.background_pedido_registro);
		setLayoutParams(new MTableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		//setOrientation(MTableRow.VERTICAL);
		
		TextView tv0 = new TextView(getContext());
		TextView tv1 = new TextView(getContext());
		TextView tv2 = new TextView(getContext());
		TextView tv3 = new TextView(getContext());

		MTableRow.LayoutParams lp0 = new MTableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
		MTableRow.LayoutParams lp1 = new MTableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp1.gravity = Gravity.CENTER_VERTICAL;
		
		tv0.setTextColor(Color.WHITE);
		tv0.setPadding(3, 0, 0, 0);
		tv0.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
		tv0.setText(producto.getNombre());
		tv0.setLayoutParams(lp0);
		
		tv1.setTextColor(Color.WHITE);
		tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
		tv1.setText(producto.getCantidad()+"");
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
		tv1.setMinimumWidth(px);
		tv1.setGravity(Gravity.RIGHT);
		tv1.setLayoutParams(lp1);
		
		tv2.setTextColor(Color.WHITE);
		tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
		tv2.setText(producto.getPrecio()+"");
		px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 110, getResources().getDisplayMetrics());
		tv2.setMinimumWidth(px);
		tv2.setGravity(Gravity.RIGHT);
		tv2.setLayoutParams(lp1);
		
		tv3.setTextColor(Color.WHITE);
		tv3.setPadding(0, 0, 3, 0);
		tv3.setGravity(Gravity.RIGHT);
		tv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
		px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, getResources().getDisplayMetrics());
		tv3.setMinimumWidth(px);
		tv3.setText(producto.getTotal()+"");
		tv3.setLayoutParams(lp1);
		
		addView(tv0);
		addView(tv1);
		addView(tv2);
		addView(tv3);
	}
	
	

}
