package com.ob.rewmobile.listener;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class GestureListener extends GestureDetector.SimpleOnGestureListener {

	public GestureListener() {
		
	}

	@Override
    public boolean onDown(MotionEvent e) {
        return true;
    }
	
	@Override
	public boolean onDoubleTap(MotionEvent e) {
		float x = e.getX();
        float y = e.getY();

        Log.e("Double Tap", "Tapped at: (" + x + "," + y + ")");
		return super.onDoubleTap(e);
	}
	

}
