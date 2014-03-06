package com.ob.rewmobile.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import android.app.AlertDialog;
import android.content.Context;
import android.util.DisplayMetrics;

public class Util {

	public static int convertDpToPixel(Context context, float dp) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return (int) px;
	}
	
	public static String getMD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
	
	public static void showDialogAdvertencia(Context context, String msg) {
		new AlertDialog.Builder(context)
		.setIcon(android.R.drawable.stat_sys_warning)
		.setTitle("Advertencia")
		.setMessage(msg)
		.setCancelable(false)
		.setPositiveButton("Aceptar", null)
	    .show();
	}

	public static String format(Double val) {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		dfs.setGroupingSeparator(',');
		
		NumberFormat nf = new DecimalFormat("#,##0.00", dfs);
		return nf.format(val);
	}

}
