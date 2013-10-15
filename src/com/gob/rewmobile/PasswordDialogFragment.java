package com.gob.rewmobile;

import com.gob.rewmobile.interfaces.PasswordDialogListenerNo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

public class PasswordDialogFragment extends DialogFragment {

	public interface PasswordDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
	
	PasswordDialogListener mListener;
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (PasswordDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement PasswordDialogListener");
        }
    }
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.layout_dialog_password, null))
               .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						mListener.onDialogPositiveClick(PasswordDialogFragment.this);
					}
				})
				.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mListener.onDialogNegativeClick(PasswordDialogFragment.this);
					}
				});
        return builder.create();
    }
}
