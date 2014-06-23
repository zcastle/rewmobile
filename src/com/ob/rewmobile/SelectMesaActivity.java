package com.ob.rewmobile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SelectMesaActivity extends DialogFragment {

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View viewSelectMesa = getActivity().getLayoutInflater().inflate(R.layout.layout_select_mesa, null);
		builder.setView(viewSelectMesa);
        return builder.create();
	}
	
	public void onButtonClicked(View v) {
	    switch (v.getId()) {
		    case R.id.button1:
		    	Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
		    break;
		    case R.id.button2:
		    	Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();
		    break;
		    case R.id.button3:
		    	Toast.makeText(getActivity(), "3", Toast.LENGTH_SHORT).show();
		    break;
	    }
	}

}
