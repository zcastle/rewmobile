package com.gob.rewmobile.objects;

import com.gob.rewmobile.R;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class Mesa extends TextView {

	private String name;
	private int status;

	public Mesa(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public Mesa(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Mesa(Context context) {
        super(context);
    }

    @Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.setText(name);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
		if (this.status == 0) {
			this.setBackgroundResource(R.drawable.background_mesa_libre);
		} else {
			this.setBackgroundResource(R.drawable.background_mesa_ocupada);
		}
	}

}
