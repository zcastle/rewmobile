package com.ob.rewmobile.util;

import com.ob.rewmobile.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.ContextThemeWrapper;

public class DialogCarga extends ProgressDialog {

	public DialogCarga(Context context, String msg) {
		super(context);
		setTitle(R.string.procesando);
		setCancelable(false);
		setMessage(msg);
	}
	
	public DialogCarga(Context context) {
		super(context);
	}

	public DialogCarga(Context context, int theme) {
		super(context, theme);
	}

}
