package com.gob.rewmobile.objects;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

import com.gob.rewmobile.util.AdminSQLiteOpenHelper;
import com.gob.rewmobile.util.JSONParser;

public class Data {

	private Context context;
	public static ArrayList<BaseClass> LST_MOZOS;
	public static ArrayList<Mesa> LST_MESAS;
	public static ArrayList<BaseClass> LST_PRODUCTOS;
	private String host01 = "10.10.10.20";
	private String host02 = "dogiacont.no-ip.org";
	private String host = host02;
	//dogiacont.no-ip.org
	
	public Data(Context context){
		this.context = context;
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.context);
		this.host = sp.getString("varHost", "");
		Log.i("Title", this.host);
	}
	
	public void loadMozos() throws IOException, Exception {
		LST_MOZOS = new ArrayList<BaseClass>();
		String url = "http://".concat(host).concat(":8080/rewmobile/readtblusuarios.php");
		try {
		    JSONArray obj = getJSONObject(url);
		    for(int i = 0; i < obj.length(); i++) {
		    	JSONObject element = obj.getJSONObject(i);
		    	LST_MOZOS.add(new BaseClass(element.getInt("id"), element.getString("no_usuario")));
		    }
		}catch(JSONException e){
		    e.printStackTrace();
		}
	}

	public void loadMesas() throws IOException, Exception {
		LST_MESAS = new ArrayList<Mesa>();
		String url = "http://".concat(host).concat(":8080/rewmobile/readatenciones.php");
		try {
		    JSONArray obj = getJSONObject(url);
		    for(int i = 0; i < obj.length(); i++) {
		    	JSONObject element = obj.getJSONObject(i);
		    	Mesa mesa = new Mesa(i, element.getString("n_ate"));
		    	mesa.setStatus(element.getInt("fl_sta"));
		    	LST_MESAS.add(mesa);
		    }
		}catch(JSONException e){
		    e.printStackTrace();
		}
	}
	
	public void loadProductos() throws Exception {
		LST_PRODUCTOS = new ArrayList<BaseClass>();
		AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this.context, "DBREWMobile", null, 1);
		SQLiteDatabase db = admin.getWritableDatabase();
		String url = "http://".concat(host).concat(":8080/rewmobile/readtblproductos.php");
		if(db != null){
			db.delete("productos", null, null);
			ContentValues registro;
			try {
			    JSONArray obj = getJSONObject(url);
			    for(int i = 0; i < obj.length(); i++) {
			    	JSONObject element = obj.getJSONObject(i);
			    	registro = new ContentValues();
					registro.put("co_producto", element.getString("co_producto"));
					registro.put("no_producto", element.getString("no_producto"));
					registro.put("va_producto", element.getString("precio0"));
					registro.put("co_categoria", element.getString("co_categoria"));
					registro.put("no_categoria", element.getString("no_categoria"));
					registro.put("nu_orden", element.getString("nu_orden"));
					db.insert("productos", null, registro);
					LST_PRODUCTOS.add(new BaseClass(element.getInt("co_producto"), element.getString("no_producto")));
			    }
			}catch(JSONException e){
			    e.printStackTrace();
			}
			db.close();
		}
	}
	
	private static JSONArray getJSONObject(String url) throws IOException, Exception {
		JSONObject json = new JSONParser().getJSONFromUrl(url);
		return json.getJSONArray("data");
	}
}










