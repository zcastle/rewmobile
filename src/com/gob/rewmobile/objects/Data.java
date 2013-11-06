package com.gob.rewmobile.objects;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
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
	public static ArrayList<Producto> LST_PRODUCTOS;
	public static ArrayList<BaseClass> LST_CATEGORIAS;
	public static Pedido PEDIDO;
	//private String host01 = "10.10.10.20";
	//private String host02 = "dogiacont.no-ip.org";
	public static String URL_HOST;
	//dogiacont.no-ip.org
	
	public Data(Context context){
		this.context = context;
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.context);
		URL_HOST = sp.getString("varHost", "");
		String PORT = sp.getString("varPort", "");
		String PATH =  sp.getString("varPath", "");
		URL_HOST = "http://".concat(URL_HOST).concat(":").concat(PORT).concat("/").concat(PATH).concat("/");
		//Log.i("Title", URL_HOST);
	}
	
	public void loadMozos() throws IOException, Exception {
		LST_MOZOS = new ArrayList<BaseClass>();
		String url = URL_HOST.concat("readUsuarios.php");
		try {
		    JSONArray obj = getJSONObject(url);
		    for(int i = 0; i < obj.length(); i++) {
		    	JSONObject element = obj.getJSONObject(i);
		    	LST_MOZOS.add(new BaseClass(element.getString("id"), element.getString("no_usuario")));
		    }
		}catch(JSONException e){
		    e.printStackTrace();
		}
	}

	public void loadMesas() throws IOException, Exception {
		LST_MESAS = new ArrayList<Mesa>();
		String url = URL_HOST.concat("readMesas.php");
		try {
		    JSONArray obj = getJSONObject(url);
		    for(int i = 0; i < obj.length(); i++) {
		    	JSONObject element = obj.getJSONObject(i);
		    	Mesa mesa = new Mesa(this.context);
		    	mesa.setName(element.getString("n_ate"));
		    	mesa.setStatus(element.getInt("fl_sta"));
		    	LST_MESAS.add(mesa);
		    }
		}catch(JSONException e){
		    e.printStackTrace();
		}
	}
	
	public void loadProductos() throws Exception {
		LST_PRODUCTOS = new ArrayList<Producto>();
		AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this.context, "DBREWMobile", null, 1);
		SQLiteDatabase db = admin.getWritableDatabase();
		String url = URL_HOST.concat("readProductos.php");
		if(db != null){
			db.delete("productos", null, null);
			ContentValues registro;
			try {
			    JSONArray obj = getJSONObject(url);
			    for(int i = 0; i < obj.length(); i++) {
			    	JSONObject element = obj.getJSONObject(i);
			    	registro = new ContentValues();
			    	registro.put("id", element.getString("id"));
					//registro.put("co_producto", element.getString("co_producto"));
					registro.put("no_producto", element.getString("no_producto"));
					registro.put("va_producto", element.getString("precio0"));
					registro.put("co_categoria", element.getString("co_categoria"));
					registro.put("no_categoria", element.getString("no_categoria"));
					registro.put("nu_orden", element.getString("nu_orden"));
					registro.put("co_destino", element.getString("co_destino"));
					db.insert("productos", null, registro);
					//LST_PRODUCTOS.add(new BaseClass(element.getInt("co_producto"), element.getString("no_producto")));
			    }
			}catch(JSONException e){
			    e.printStackTrace();
			}
			db.close();
		}
	}
	
	public void loadCategorias() throws IOException, Exception {
		LST_CATEGORIAS = new ArrayList<BaseClass>();
		String url = URL_HOST.concat("readCategorias.php");
		try {
		    JSONArray obj = getJSONObject(url);
		    for(int i = 0; i < obj.length(); i++) {
		    	JSONObject element = obj.getJSONObject(i);
		    	LST_CATEGORIAS.add(new BaseClass(element.getString("co_categoria"), element.getString("no_categoria")));
		    }
		}catch(JSONException e){
		    e.printStackTrace();
		}
	}
	
	public void loadPedido(String mesa_name) throws IOException, Exception {
		PEDIDO = new Pedido();
		String url = URL_HOST.concat("readMesaxNro.php?t=").concat(mesa_name);
		Log.i("mesa_name", mesa_name);
		try {
		    JSONArray obj = getJSONObject(url);
		    Producto producto;
		    for(int i = 0; i < obj.length(); i++) {
		    	JSONObject element = obj.getJSONObject(i);
		    	producto = new Producto();
		    	//Log.i("USUARIO", element.getString("usuario"));
		    	//PEDIDO.setId(element.getInt("idatencion"));
		    	PEDIDO.setCajero(element.getString("usuario"));
		    	PEDIDO.setMozo(element.getString("mozo"));
		    	PEDIDO.setPax(element.getInt("pax"));
		    	
		    	producto.setId(element.getInt("idproducto"));
		    	producto.setIdAtencion(element.getInt("idatencion"));
		    	producto.setNombre(element.getString("producto"));
		    	producto.setCantidad(element.getDouble("cantidad"));
		    	producto.setPrecio(element.getDouble("precio"));
		    	producto.setMensaje(element.getString("mensaje"));
		    	PEDIDO.getProducto().add(producto);
		    }
		}catch(JSONException e){
		    e.printStackTrace();
		}
	}
	
	public void insertPedido(final JSONObject obj) {
		final String url = URL_HOST.concat("insertPedidos.php");
		new Thread() {
			public void run() {
				HttpClient httpclient = new DefaultHttpClient(); //myParams

			    try {
			        HttpPost httpPost = new HttpPost(url);
			        httpPost.setHeader("Accept", "application/json");
			        //httpPost.setHeader("Content-type", "application/json");

			        //StringEntity se = new StringEntity(obj.toString()); 
			        //se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			        ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
			        nameValuePairs.add(new BasicNameValuePair("data", obj.toString()));
			        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			        
			        HttpResponse response = httpclient.execute(httpPost);
			        String temp = EntityUtils.toString(response.getEntity());
			        //Log.i("tag", temp);
			    } catch (ClientProtocolException e) {
			    } catch (IOException e) {
			    }
			}
		}.start();
	}
	
	public void deletePedido(final JSONObject obj) throws IOException, Exception {
		final String url = URL_HOST.concat("deletePedido.php");
		new Thread() {
			public void run() {
				HttpClient httpclient = new DefaultHttpClient(); //myParams

			    try {
			        HttpPost httpPost = new HttpPost(url);
			        httpPost.setHeader("Accept", "application/json");
			        
			        ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
			        nameValuePairs.add(new BasicNameValuePair("data", obj.toString()));
			        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			        
			        HttpResponse response = httpclient.execute(httpPost);
			        String temp = EntityUtils.toString(response.getEntity());
			        //Log.i("tag", temp);
			    } catch (ClientProtocolException e) {
			    } catch (IOException e) {
			    }
			}
		}.start();
	}

	public void updatePedido(final JSONObject obj) throws IOException, Exception {
		final String url = URL_HOST.concat("updatePedido.php");
		new Thread() {
			public void run() {
				HttpClient httpclient = new DefaultHttpClient(); //myParams

			    try {
			        HttpPost httpPost = new HttpPost(url);
			        httpPost.setHeader("Accept", "application/json");
			        
			        ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
			        nameValuePairs.add(new BasicNameValuePair("data", obj.toString()));
			        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			        
			        HttpResponse response = httpclient.execute(httpPost);
			        String temp = EntityUtils.toString(response.getEntity());
			        Log.i("respuesta", temp);
			    } catch (ClientProtocolException e) {
			    } catch (IOException e) {
			    }
			}
		}.start();
	}
	
	private static JSONArray getJSONObject(String url) throws IOException, Exception {
		JSONObject json = new JSONParser().getJSONFromUrl(url);
		return json.getJSONArray("data");
	}
}










