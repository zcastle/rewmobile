package com.gob.rewmobile.util;

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

import com.gob.rewmobile.model.Categoria;
import com.gob.rewmobile.model.CategoriaController;
import com.gob.rewmobile.model.Destino;
import com.gob.rewmobile.model.DestinoController;
import com.gob.rewmobile.model.Producto;
import com.gob.rewmobile.model.Usuario;
import com.gob.rewmobile.model.UsuarioControlller;
import com.gob.rewmobile.objects.Mesa;
import com.gob.rewmobile.objects.Pedido;

public class Data {

	private Context context;
	public static UsuarioControlller LST_MOZOS;
	public static ArrayList<Mesa> LST_MESAS;
	//public static ArrayList<Producto> LST_PRODUCTOS;
	public static CategoriaController LST_CATEGORIAS;
	//public static Pedido PEDIDO;
	public static DestinoController DESTINOS;
	// private String host01 = "10.10.10.20";
	// private String host02 = "dogiacont.no-ip.org";
	public static String URL_HOST;

	// dogiacont.no-ip.org

	public Data(Context context) {
		this.context = context;
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.context);
		URL_HOST = sp.getString("varHost", "");
		String PORT = sp.getString("varPort", "");
		String PATH = sp.getString("varPath", "");
		URL_HOST = "http://".concat(URL_HOST).concat(":").concat(PORT).concat("/").concat(PATH).concat("/");
	}

	public void loadDestinos() throws IOException, Exception {
		DESTINOS = new DestinoController();
		String url = URL_HOST.concat("readDestinos.php");
		try {
			JSONArray obj = getJSONObject(url);
			for (int i = 0; i < obj.length(); i++) {
				JSONObject e = obj.getJSONObject(i);
				DESTINOS.getDestinos().add(new Destino(e.getInt("co_destino"), e.getString("no_imp"), e.getString("no_destino")));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void loadCategorias() throws IOException, Exception {
		LST_CATEGORIAS = new CategoriaController();
		String url = URL_HOST.concat("readCategorias.php");
		try {
			JSONArray obj = getJSONObject(url);
			for (int i = 0; i < obj.length(); i++) {
				JSONObject element = obj.getJSONObject(i);
				LST_CATEGORIAS.getCategorias().add(new Categoria(element.getString("co_categoria"), element.getString("no_categoria")));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void loadProductos() throws Exception {
		SQLiteDatabase db = null;
		try {
			AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this.context, "DBREWMobile", null, 1);
			db = admin.getWritableDatabase();
			String url = URL_HOST.concat("readProductos.php");
			if (db != null) {
				db.delete("productos", null, null);
				ContentValues registro;
				try {
					JSONArray obj = getJSONObject(url);
					for (int i = 0; i < obj.length(); i++) {
						JSONObject e = obj.getJSONObject(i);
						registro = new ContentValues();
						registro.put("id", e.getInt("id"));
						registro.put("no_producto", e.getString("no_producto"));
						registro.put("va_producto", e.getString("precio0"));
						registro.put("co_categoria", e.getString("co_categoria"));
		 				registro.put("no_categoria", e.getString("no_categoria"));
						registro.put("nu_orden", e.getString("nu_orden"));
						registro.put("co_destino", e.getString("co_destino"));
						db.insert("productos", null, registro);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}			
			}
		} catch (Exception e) {
			new Exception("Error al cargar productos");
		} finally {
			db.close();
		}
		
	}
	
	public void loadMozos() throws IOException, Exception {
		LST_MOZOS = new UsuarioControlller();
		String url = URL_HOST.concat("readUsuarios.php");
		try {
			JSONArray obj = getJSONObject(url);
			for (int i = 0; i < obj.length(); i++) {
				JSONObject e = obj.getJSONObject(i);
				LST_MOZOS.getUsuarios().add(new Usuario(e.getInt("id"), e.getString("no_usuario")));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void loadMesas() throws IOException, Exception {
		LST_MESAS = new ArrayList<Mesa>();
		String url = URL_HOST.concat("readMesas.php");
		try {
			JSONArray obj = getJSONObject(url);
			for (int i = 0; i < obj.length(); i++) {
				JSONObject e = obj.getJSONObject(i);
				Mesa mesa = new Mesa(this.context);
				mesa.setNombre(e.getString("n_ate"));
				mesa.setStatus(e.getInt("fl_sta"));
				LST_MESAS.add(mesa);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void loadPedido(Pedido pedido) throws IOException, Exception {
		String url = URL_HOST.concat("readMesaxNro.php?t=").concat(pedido.getMesa());
		try {
			JSONArray obj = getJSONObject(url);
			Producto producto;
			for (int i = 0; i < obj.length(); i++) {
				JSONObject e = obj.getJSONObject(i);
				producto = new Producto();
				pedido.setCajero(e.getString("usuario"));
				pedido.setPax(e.getInt("pax"));

				producto.setId(e.getInt("idproducto"));
				producto.setIdAtencion(e.getInt("idatencion"));
				producto.setNombre(e.getString("producto"));
				producto.setCantidad(e.getDouble("cantidad"));
				producto.setPrecio(e.getDouble("precio"));
				producto.setMensaje(e.getString("mensaje"));
				producto.setDestino(DESTINOS.getDestinoById(e.getInt("co_destino")));
				producto.setEnviado(e.getString("fl_envio").equals("S") ? true : false);
				pedido.getProducto().add(producto);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void insertPedido(final JSONObject obj, final Producto producto) {
		final String url = URL_HOST.concat("insertPedidos.php");
		new Thread() {
			public void run() {
				HttpClient httpclient = new DefaultHttpClient(); // myParams

				try {
					HttpPost httpPost = new HttpPost(url);
					httpPost.setHeader("Accept", "application/json");
					// httpPost.setHeader("Content-type", "application/json");

					// StringEntity se = new StringEntity(obj.toString());
					// se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					// "application/json"));
					ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
					Log.e("OBJSON", obj.toString());
					nameValuePairs.add(new BasicNameValuePair("data", obj.toString()));
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					HttpResponse response = httpclient.execute(httpPost);
					// EntityUtils.toString(response.getEntity());
					String temp = EntityUtils.toString(response.getEntity());
					Log.i("insertPedidos.php", temp);
					JSONObject json = new JSONObject(temp);
					if(json.getInt("idatencion")>0) {
						producto.setIdAtencion(json.getInt("idatencion"));
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void deletePedido(final JSONObject obj) throws IOException,
			Exception {
		final String url = URL_HOST.concat("deletePedido.php");
		new Thread() {
			public void run() {
				HttpClient httpclient = new DefaultHttpClient(); // myParams

				try {
					HttpPost httpPost = new HttpPost(url);
					httpPost.setHeader("Accept", "application/json");

					ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("data", obj
							.toString()));
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					HttpResponse response = httpclient.execute(httpPost);
					// EntityUtils.toString(response.getEntity());
					String temp = EntityUtils.toString(response.getEntity());
					Log.i("deletePedido.php", temp);
				} catch (ClientProtocolException e) {
				} catch (IOException e) {
				}
			}
		}.start();
	}

	public void updatePedido(final JSONObject obj) throws IOException,
			Exception {
		final String url = URL_HOST.concat("updatePedido.php");
		new Thread() {
			public void run() {
				HttpClient httpclient = new DefaultHttpClient();
				try {
					HttpPost httpPost = new HttpPost(url);
					httpPost.setHeader("Accept", "application/json");

					ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("data", obj.toString()));
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					HttpResponse response = httpclient.execute(httpPost);
					String temp = EntityUtils.toString(response.getEntity());
					Log.i("updatePedido.php", temp);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void updateEnvio(final JSONObject obj) throws IOException, Exception {
		String url = URL_HOST.concat("updateFlEnvio.php");
		HttpClient httpclient = new DefaultHttpClient(); // myParams

		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Accept", "application/json");

			ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("data", obj.toString()));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httpPost);
			String temp = EntityUtils.toString(response.getEntity());
			Log.i("updateFlEnvio.php", temp);
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
	}

	private static JSONArray getJSONObject(String url) throws IOException,
			Exception {
		JSONObject json = new JSONParser().getJSONFromUrl(url);
		return json.getJSONArray("data");
	}
}
