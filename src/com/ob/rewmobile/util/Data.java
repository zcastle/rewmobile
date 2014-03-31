package com.ob.rewmobile.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.preference.PreferenceManager;
import android.util.Log;

import com.ob.rewmobile.model.Caja;
import com.ob.rewmobile.model.CajaController;
import com.ob.rewmobile.model.Categoria;
import com.ob.rewmobile.model.CategoriaController;
import com.ob.rewmobile.model.CentroCosto;
import com.ob.rewmobile.model.Cliente;
import com.ob.rewmobile.model.ClienteController;
import com.ob.rewmobile.model.Departamento;
import com.ob.rewmobile.model.Destino;
import com.ob.rewmobile.model.DestinoController;
import com.ob.rewmobile.model.Distrito;
import com.ob.rewmobile.model.Empresa;
import com.ob.rewmobile.model.Mesa;
import com.ob.rewmobile.model.PedidoController;
import com.ob.rewmobile.model.Producto;
import com.ob.rewmobile.model.ProductoController;
import com.ob.rewmobile.model.Provincia;
import com.ob.rewmobile.model.Tarjeta;
import com.ob.rewmobile.model.UbigeoController;
import com.ob.rewmobile.model.Usuario;
import com.ob.rewmobile.model.UsuarioController;
//import com.ob.rewmobile.util.AdminSQLiteOpenHelper.Tabla;

public class Data {

	private Context context;
	public static UsuarioController usuarioController;
	public static ArrayList<Mesa> LST_MESAS;
	public static CategoriaController categoriaController;
	public static ProductoController productoController;
	public static DestinoController destinoController;
	public static String URL_HOST;
	public static CajaController cajaController;
	public static UbigeoController ubigeoController;
	public static ClienteController clienteController;
	public static ArrayList<Tarjeta> tarjetaController;

	public Data(Context context) {
		this.context = context;
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.context);
		URL_HOST = sp.getString("keyHost", "");
		Globals.IP_HOST = URL_HOST;
		String PORT = sp.getString("keyPort", "");
		String PATH = sp.getString("keyPath", "");
		URL_HOST = "http://".concat(URL_HOST).concat(":").concat(PORT).concat("/").concat(PATH).concat("/");
	}
	
	public void loadTarjetas() {
		tarjetaController = new ArrayList<Tarjeta>();
		tarjetaController.add(new Tarjeta(1, "EFECTIVO"));
		tarjetaController.add(new Tarjeta(2, "VISA"));
		tarjetaController.add(new Tarjeta(3, "MASTERCARD"));
	}
	
	public void loadEquipo(String name, boolean force) throws ClientProtocolException, URISyntaxException, IOException, JSONException {
		cajaController = new CajaController();
		Conn conn = new Conn(context);
		Cursor e = conn.getDB().rawQuery("SELECT id, nombre, tipo, serie_b, numero_b, serie_f, numero_f, impresora_p, impresora_b, impresora_f, tc, servicio, dia FROM equipo", null);
		Cursor cc = conn.getDB().rawQuery("SELECT id, codigo, nombre, direccion, distrito FROM centrocosto", null);
		Cursor ee = conn.getDB().rawQuery("SELECT id, codigo, ruc, razon, nombre, direccion, distrito, igv FROM empresa", null);
		if (e.moveToFirst()) {
			if (force) {
				cajaController.setCaja(getEquipoFromUrl(name, conn));
			} else {
				//do {
					Caja equipo = new Caja();
					equipo.setId(e.getInt(0));
					equipo.setNombre(e.getString(1));
					equipo.setTipo(e.getString(2));
					//equipo.setCentroCosto(c.getInt(3));
					equipo.setSerieBoleta(e.getString(3));
					equipo.setNumeroBoleta(e.getInt(4));
					equipo.setSerieFactura(e.getString(5));
					equipo.setNumeroFactura(e.getInt(6));
					equipo.setImpresoraP(e.getString(7));
					equipo.setImpresoraB(e.getString(8));
					equipo.setImpresoraF(e.getString(9));
					equipo.setTc(e.getDouble(10));
					equipo.setServicio(e.getDouble(11));
					equipo.setDia(e.getInt(12));
					
					CentroCosto centroCosto = null;
					if (cc.moveToFirst()) {
						centroCosto = new CentroCosto();
						centroCosto.setId(cc.getInt(0));
						centroCosto.setCodigo(cc.getString(1));
						centroCosto.setNombre(cc.getString(2));
						centroCosto.setDireccion(cc.getString(3));
						centroCosto.setDistrito(cc.getString(4));
						equipo.setCentroCosto(centroCosto);
					}
					
					Empresa empresa = null;
					
					if (ee.moveToFirst()) {
						empresa = new Empresa();
						empresa.setId(ee.getInt(0));
						empresa.setCodigo(ee.getString(1));
						empresa.setRuc(ee.getString(2));
						empresa.setRazon(ee.getString(3));
						empresa.setNombre(ee.getString(4));
						empresa.setDireccion(ee.getString(5));
						empresa.setDistrito(ee.getString(6));
						empresa.setIgv(ee.getDouble(7));
						centroCosto.setEmpresa(empresa);
					}
					
					cajaController.setCaja(equipo);
				//} while (e.moveToNext());
			}
		} else {
			cajaController.setCaja(getEquipoFromUrl(name, conn));
		}
		e.close();
		cc.close();
		ee.close();
		conn.close();
	}
	
	private Caja getEquipoFromUrl(String name, Conn conn) throws ClientProtocolException, URISyntaxException, IOException, JSONException {
		Caja equipo = null;
		conn.getDB().delete("equipo", null, null);
		conn.getDB().delete("centrocosto", null, null);
		conn.getDB().delete("empresa", null, null);
		ContentValues r;
		JSONArray obj = getJSONObject(URL_HOST.concat("readEquipos.php?nombre=").concat(name));
		for (int i = 0; i < obj.length(); i++) {
			JSONObject e = obj.getJSONObject(i);
			r = new ContentValues();
			r.put("id", e.getInt("id"));
			r.put("nombre", e.getString("nombre"));
			r.put("tipo", e.getString("tipo"));
			r.put("serie_b", e.getString("serie_b"));
			r.put("numero_b", e.getInt("numero_b"));
			r.put("serie_f", e.getString("serie_f"));
			r.put("numero_f", e.getInt("numero_f"));
			r.put("impresora_p", e.getString("impresora_p"));
			r.put("impresora_b", e.getString("impresora_b"));
			r.put("impresora_f", e.getString("impresora_f"));
			r.put("tc", e.getDouble("tc"));
			r.put("servicio", e.getDouble("servicio"));
			r.put("dia", e.getInt("dia"));
			conn.getDB().insert("equipo", null, r);
			
			r = new ContentValues();
			r.put("id", e.getInt("centrocosto_id"));
			r.put("codigo", e.getString("cc_codigo"));
			r.put("nombre", e.getString("cc_nombre"));
			r.put("direccion", e.getString("cc_direccion"));
			r.put("distrito", e.getString("cc_distrito"));
			conn.getDB().insert("centrocosto", null, r);
			
			r = new ContentValues();
			r.put("id", e.getInt("cc_empresa_id"));
			r.put("codigo", e.getString("e_codigo"));
			r.put("ruc", e.getString("e_ruc"));
			r.put("razon", e.getString("e_razon"));
			r.put("nombre", e.getString("e_nombre"));
			r.put("direccion", e.getString("e_direccion"));
			r.put("distrito", e.getString("e_distrito"));
			r.put("igv", e.getDouble("e_igv"));
			conn.getDB().insert("empresa", null, r);
			
			equipo = new Caja();
			equipo.setId(e.getInt("id"));
			equipo.setNombre(e.getString("nombre"));
			equipo.setTipo(e.getString("tipo"));
			equipo.setSerieBoleta(e.getString("serie_b"));
			equipo.setNumeroBoleta(e.getInt("numero_b"));
			equipo.setSerieFactura(e.getString("serie_f"));
			equipo.setNumeroFactura(e.getInt("numero_f"));
			equipo.setImpresoraP(e.getString("impresora_p"));
			equipo.setImpresoraB(e.getString("impresora_b"));
			equipo.setImpresoraF(e.getString("impresora_f"));
			equipo.setTc(e.getDouble("tc"));
			equipo.setServicio(e.getDouble("servicio"));
			
			CentroCosto centroCosto = new CentroCosto();
			centroCosto.setId(e.getInt("centrocosto_id"));
			centroCosto.setCodigo(e.getString("cc_codigo"));
			centroCosto.setNombre(e.getString("cc_nombre"));
			centroCosto.setDireccion(e.getString("cc_direccion"));
			centroCosto.setDistrito(e.getString("cc_distrito"));
			equipo.setCentroCosto(centroCosto);
			
			Empresa empresa = new Empresa();
			empresa.setId(e.getInt("cc_empresa_id"));
			empresa.setCodigo(e.getString("e_codigo"));
			empresa.setRuc(e.getString("e_ruc"));
			empresa.setRazon(e.getString("e_razon"));
			empresa.setNombre(e.getString("e_nombre"));
			empresa.setDireccion(e.getString("e_direccion"));
			empresa.setDistrito(e.getString("e_distrito"));
			empresa.setIgv(e.getDouble("e_igv"));
			centroCosto.setEmpresa(empresa);
		}
		return equipo;
	}

	public void loadDestinos(boolean force) throws ClientProtocolException, URISyntaxException, IOException, JSONException {
		destinoController = new DestinoController();
		Conn conn = new Conn(context);
		Cursor c = conn.getDB().rawQuery("SELECT id, nombre, ip FROM destinos", null);
		if (c.moveToFirst()) {
			if (force) {
				destinoController.setDestinos(getDestinosFromUrl(conn));
			} else {
				do {
					destinoController.getDestinos().add(new Destino(c.getInt(0), c.getString(1), c.getString(2)));
				} while (c.moveToNext());
			}
		} else {
			destinoController.setDestinos(getDestinosFromUrl(conn));
		}
		c.close();
		conn.close();
	}
	
	private ArrayList<Destino> getDestinosFromUrl(Conn conn) throws ClientProtocolException, URISyntaxException, IOException, JSONException {
		ArrayList<Destino> destinos = new ArrayList<Destino>();
		conn.getDB().delete("destinos", null, null);
		ContentValues r;
		JSONArray obj = getJSONObject(URL_HOST.concat("readDestinos.php"));
		for (int i = 0; i < obj.length(); i++) {
			JSONObject e = obj.getJSONObject(i);
			r = new ContentValues();
			r.put("id", e.getInt("co_destino"));
			r.put("nombre", e.getString("no_imp"));
			r.put("ip", e.getString("no_destino"));
			conn.getDB().insert("destinos", null, r);
			destinos.add(new Destino(e.getInt("co_destino"), e.getString("no_imp"), e.getString("no_destino")));
		}
		return destinos;
	}
	
	public void loadCategorias(boolean force) throws ClientProtocolException, URISyntaxException, IOException, JSONException {
		categoriaController = new CategoriaController();
		Conn conn = new Conn(context);
		Cursor c = conn.getDB().rawQuery("SELECT id, codigo, nombre FROM categorias", null);
		if (c.moveToFirst()) {
			if (force) {
				categoriaController.setCategorias(getCategoriasFromUrl(conn));
			} else {
				do {
					categoriaController.getCategorias().add(new Categoria(c.getInt(0), c.getString(1), c.getString(2)));
				} while (c.moveToNext());
			}
		} else {
			categoriaController.setCategorias(getCategoriasFromUrl(conn));
		}
		c.close();
		conn.close();
	}
	
	private ArrayList<Categoria> getCategoriasFromUrl(Conn conn) throws ClientProtocolException, URISyntaxException, IOException, JSONException {
		ArrayList<Categoria> categorias = new ArrayList<Categoria>();
		conn.getDB().delete("categorias", null, null);
		ContentValues r;
		JSONArray obj = getJSONObject(URL_HOST.concat("readCategorias.php"));
		for (int i = 0; i < obj.length(); i++) {
			JSONObject e = obj.getJSONObject(i);
			r = new ContentValues();
			r.put("id", e.getInt("id"));
			r.put("codigo", e.getString("co_categoria"));
			r.put("nombre", e.getString("no_categoria"));
			conn.getDB().insert("categorias", null, r);
			categorias.add(new Categoria(e.getInt("id"), e.getString("co_categoria"), e.getString("no_categoria")));
		}
		return categorias;
	}

	public void loadProductos(boolean force) throws ClientProtocolException, URISyntaxException, IOException, JSONException {
		productoController = new ProductoController();
		Conn conn = new Conn(context);
		Cursor c = conn.getDB().rawQuery("SELECT id, codigo, nombre, precio, categoria_id, nu_orden, destino_id FROM productos", null);
		Producto producto;
		if (c.moveToFirst()) {
			if (force) {
				productoController.setProducto(getProductosFromUrl(conn));
			} else {
				do {
					producto = new Producto();
					producto.setId(c.getInt(0));
					producto.setCodigo(c.getString(1));
					producto.setNombre(c.getString(2));
					producto.setPrecio(c.getDouble(3));
					producto.setCategoria(categoriaController.getCategoriaById(c.getInt(4)));
					producto.setDestino(Data.destinoController.getDestinoById(c.getInt(6)));
					productoController.getProductos().add(producto);
				} while (c.moveToNext());
			}
		} else {
			productoController.setProducto(getProductosFromUrl(conn));
		}
		c.close();
		conn.close();
	}
	
	private ArrayList<Producto> getProductosFromUrl(Conn conn) throws ClientProtocolException, URISyntaxException, IOException, JSONException {
		ArrayList<Producto> productos = new ArrayList<Producto>();
		conn.getDB().delete("productos", null, null);
		ContentValues r;
		JSONArray obj = getJSONObject(URL_HOST.concat("readProductos.php"));
		Producto producto;
		for (int i = 0; i < obj.length(); i++) {
			JSONObject e = obj.getJSONObject(i);
			r = new ContentValues();
			r.put("id", e.getInt("id"));
			r.put("codigo", e.getString("co_producto"));
			r.put("nombre", e.getString("no_producto"));
			r.put("precio", e.getDouble("precio0"));
			r.put("categoria_id", e.getString("co_categoria"));
			r.put("nu_orden", e.getString("nu_orden"));
			r.put("destino_id", e.getString("co_destino"));
			conn.getDB().insert("productos", null, r);
			producto = new Producto();
			producto.setId(e.getInt("id"));
			producto.setCodigo(e.getString("co_producto"));
			producto.setNombre(e.getString("no_producto"));
			producto.setPrecio(e.getDouble("precio0"));
			producto.setCategoria(Data.categoriaController.getCategoriaByCodigo(e.getString("co_categoria")));
			producto.setDestino(Data.destinoController.getDestinoById(e.getInt("co_destino")));
			productos.add(producto);
		}
		return productos;
	}
	
	public void loadUsuarios(boolean force) throws ClientProtocolException, URISyntaxException, IOException, JSONException {
		usuarioController = new UsuarioController();
		Conn conn = new Conn(context);
		Cursor c = conn.getDB().rawQuery("SELECT id, nombre, clave, rol FROM usuarios", null);
		if (c.moveToFirst()) {
			if (force) {
				usuarioController.setUsuarios(getUsuariosFromUrl(conn));
			} else {
				do {
					usuarioController.getUsuarios().add(new Usuario(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3)));
				} while (c.moveToNext());
			}
		} else {
			usuarioController.setUsuarios(getUsuariosFromUrl(conn));
		}
		c.close();
		conn.close();
	}
	
	private ArrayList<Usuario> getUsuariosFromUrl(Conn conn) throws ClientProtocolException, URISyntaxException, IOException, JSONException {
		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
		conn.getDB().delete("usuarios", null, null);
		ContentValues r;
		JSONArray obj = getJSONObject(URL_HOST.concat("readUsuarios.php"));
		for (int i = 0; i < obj.length(); i++) {
			JSONObject e = obj.getJSONObject(i);
			r = new ContentValues();
			r.put("id", e.getInt("id"));
			r.put("nombre", e.getString("usuario"));
			r.put("clave", e.getString("clave"));
			r.put("rol", e.getInt("rol_id"));
			conn.getDB().insert("usuarios", null, r);
			usuarios.add(new Usuario(e.getInt("id"), e.getString("usuario"), e.getString("clave"), e.getInt("rol_id")));
		}
		return usuarios;
	}
	
	@SuppressWarnings("unchecked")
	public void loadUbigeo(boolean force) throws ClientProtocolException, JSONException, URISyntaxException, IOException {
		ubigeoController = new UbigeoController();
		Conn conn = new Conn(context);
		Cursor dep = conn.getDB().rawQuery("SELECT id, codigo, nombre FROM departamento", null);
		if (dep.moveToFirst()) {
			if (force) {
				for (Object obj: loadUbigeoFromUrl(conn)) {
					if (obj instanceof Departamento) {
						ubigeoController.setDepartamentos((ArrayList<Departamento>) obj);
					} else if (obj instanceof Provincia) {
						ubigeoController.setProvincias((ArrayList<Provincia>) obj);
					} else if (obj instanceof Distrito) {
						ubigeoController.setDistritos((ArrayList<Distrito>) obj);
					}
				}
			} else {
				Departamento departamento;
				do {
					departamento = new Departamento();
					departamento.setId(dep.getInt(0));
					departamento.setCodigo(dep.getString(1));
					departamento.setNombre(dep.getString(2));
					String[] campos = new String[] {"id","codigo","nombre"};
					String[] args = new String[] {departamento.getId()+""};
					Cursor pro = conn.getDB().query("provincia", campos, "departamento_id=?", args, null, null, null);
					ArrayList<Provincia> provincias = null;
					if (pro.moveToFirst()) {
						provincias = new ArrayList<Provincia>();
						Provincia provincia;
						do {
							provincia = new Provincia();
							provincia.setId(pro.getInt(0));
							provincia.setCodigo(pro.getString(1));
							provincia.setNombre(pro.getString(2));
							provincia.setDepartamento(departamento);
							String[] args2 = new String[] {provincia.getId()+""};
							Cursor dis = conn.getDB().query("distrito", campos, "provincia_id=?", args2, null, null, null);
							ArrayList<Distrito> distritos = null;
							if (dis.moveToFirst()) {
								distritos = new ArrayList<Distrito>();
								Distrito distrito;
								do {
									distrito = new Distrito(dis.getInt(0), dis.getString(1), dis.getString(2), provincia);
									distritos.add(distrito);
									ubigeoController.getDistritos().add(distrito);
								} while (dis.moveToNext());
							}
							dis.close();
							provincia.setDistritos(distritos);
							ubigeoController.getProvincias().add(provincia);
							provincias.add(provincia);
						} while(pro.moveToNext());
					}
					pro.close();
					departamento.setProvincias(provincias);
					ubigeoController.getDepartamentos().add(departamento);
				} while (dep.moveToNext());
			}
		} else {
			for (Object obj: loadUbigeoFromUrl(conn)) {
				if (obj instanceof Departamento) {
					ubigeoController.setDepartamentos((ArrayList<Departamento>) obj);
				} else if (obj instanceof Provincia) {
					ubigeoController.setProvincias((ArrayList<Provincia>) obj);
				} else if (obj instanceof Distrito) {
					ubigeoController.setDistritos((ArrayList<Distrito>) obj);
				}
			}
		}
		dep.close();
		conn.close();
	}
	
	private ArrayList<Object> loadUbigeoFromUrl(Conn conn) throws JSONException, ClientProtocolException, URISyntaxException, IOException {
		ArrayList<Object> objectReturn = new ArrayList<Object>();
		ArrayList<Departamento> departamentos = new ArrayList<Departamento>();
		ArrayList<Provincia> provincias = new ArrayList<Provincia>();
		ArrayList<Distrito> distritos = new ArrayList<Distrito>();
		Departamento departamento;
		conn.getDB().delete("departamento", null, null);
		conn.getDB().delete("provincia", null, null);
		conn.getDB().delete("distrito", null, null);
		ContentValues rde;
		ContentValues rpr;
		ContentValues rdi;
		JSONArray de = getJSONObject(URL_HOST.concat("readUbigeo.php?out=de"));
		for (int i = 0; i < de.length(); i++) {
			JSONObject dep = de.getJSONObject(i);
			departamento = new Departamento();
			rde = new ContentValues();
			rde.put("id", dep.getInt("id"));
			rde.put("codigo", dep.getString("co_departamento"));
			rde.put("nombre", dep.getString("no_ubigeo"));
			conn.getDB().insert("departamento", null, rde);
			departamento.setId(dep.getInt("id"));
			departamento.setCodigo(dep.getString("co_departamento"));
			departamento.setNombre(dep.getString("no_ubigeo"));
			JSONArray pr = getJSONObject(URL_HOST.concat("readUbigeo.php?out=pr&de=".concat(dep.getString("co_departamento"))));
			Provincia provincia;
			for (int j = 0; j < pr.length(); j++) {
				JSONObject pro = pr.getJSONObject(j);
				provincia = new Provincia();
				rpr = new ContentValues();
				rpr.put("id", pro.getInt("id"));
				rpr.put("codigo", pro.getString("co_provincia"));
				rpr.put("nombre", pro.getString("no_ubigeo"));
				rpr.put("departamento_id", dep.getInt("id"));
				conn.getDB().insert("provincia", null, rpr);
				provincia.setId(pro.getInt("id"));
				provincia.setCodigo(pro.getString("co_provincia"));
				provincia.setNombre(pro.getString("no_ubigeo"));
				provincia.setDepartamento(departamento);
				JSONArray di = getJSONObject(URL_HOST.concat("readUbigeo.php?out=di&de=".concat(dep.getString("co_departamento")).concat("&pr=").concat(pro.getString("co_provincia"))));
				Distrito distrito;
				for (int k = 0; k < di.length(); k++) {
					JSONObject dis = di.getJSONObject(k);
					distrito = new Distrito();
					rdi = new ContentValues();
					rdi.put("id", dis.getInt("id"));
					rdi.put("codigo", dis.getString("co_distrito"));
					rdi.put("nombre", dis.getString("no_ubigeo"));
					rdi.put("provincia_id", pro.getInt("id"));
					conn.getDB().insert("distrito", null, rdi);
					distrito.setId(dis.getInt("id"));
					distrito.setCodigo(dis.getString("co_distrito"));
					distrito.setNombre(dis.getString("no_ubigeo"));
					distrito.setProvincia(provincia);
					distritos.add(distrito);
					provincia.getDistritos().add(distrito);
					provincias.add(provincia);
				}
				departamento.getProvincias().add(provincia);
			}
			departamentos.add(departamento);
		}
		objectReturn.add(departamentos);
		objectReturn.add(provincias);
		objectReturn.add(distritos);
		return objectReturn;
	}
	
	/*public void loadClientes() throws ClientProtocolException, URISyntaxException, IOException, JSONException {
		clienteController = new ClienteController();
		Conn conn = new Conn(context);
		Cursor c = conn.getDB().rawQuery("SELECT ruc, razon, direccion, distrito_id FROM clientes", null);
		if (c.moveToFirst()) {
			do {
				clienteController.getClientes().add(new Cliente(c.getString(0), c.getString(1), c.getString(2), ubigeoController.getDistritoById(c.getInt(3))));
			} while (c.moveToNext());
		}
		c.close();
		conn.close();
	}*/
	
	private boolean existeRuc(Cliente cliente, Conn conn) {
		boolean existe = false;
		Cursor c = conn.query("clientes", new String[] {"*"}, "ruc=?", new String[] {cliente.getRuc()});
		if (c.moveToFirst()) {
			existe = true;
		}
		c.close();
		return existe;
	}
	
	public void addCliente(Cliente cliente) {
		Conn conn = new Conn(context);
		if (!existeRuc(cliente, conn)) {
			ContentValues r = new ContentValues();
	        r.put("ruc", cliente.getRuc()); 
	        r.put("razon", cliente.getRazonSocial());
	        r.put("direccion", cliente.getDireccion());
	        r.put("distrito_id", cliente.getDistrito().getId());
	        conn.insert("clientes", r);
		}
		conn.close();
	}
	
	public Cliente getCienteByRuc(String ruc) {
		Cliente cliente = null;
		Conn conn = new Conn(context);
		Cursor c = conn.query("clientes", new String[] {"ruc","razon","direccion","distrito_id"}, "ruc=?", new String[] {ruc});
		if (c.moveToFirst()) {
			cliente = new Cliente();
			cliente.setRuc(c.getString(0));
			cliente.setRazonSocial(c.getString(1));
			cliente.setDireccion(c.getString(2));
			cliente.setDistrito(ubigeoController.getDistritoById(c.getInt(3)));
		}
		c.close();
		conn.close();
		return cliente;
		
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

	public void loadPedido(PedidoController PEDIDO) throws ClientProtocolException, URISyntaxException, IOException, JSONException {
		Producto producto;
		if (App.isCaja()) {
			Conn conn = new Conn(context);
			String[] campos = new String[] {"id","mozo_id","cajero_id","pax","producto_id","producto","cantidad",
					"precio","mensaje","enviado","servicio","cliente_ruc"};
			Cursor f = conn.query("pedidos", campos, "mesa=?", new String[] {PEDIDO.getMesa()});
			if (f.moveToFirst()) {
				do {
					//mesa text, mozo_id integer, cajero_id integer, pax integer, atencion_id integer, producto_id integer, producto text, cantidad real, precio real, mensaje text, enviado text, sync text
					if(f.getInt(1)>0) {
						PEDIDO.setMozo(Data.usuarioController.getUsuarioById(f.getInt(1)));
					} else {
						PEDIDO.setMozo(new Usuario(0, "Sin Mozo"));
					}
					PEDIDO.setCajero(Data.usuarioController.getUsuarioById(f.getInt(2)));
					PEDIDO.setPax(f.getInt(3));
					producto = new Producto();
					producto.setIdAtencion(f.getInt(0));
					producto.setId(f.getInt(4));
					producto.setNombre(f.getString(5));
					producto.setCantidad(f.getDouble(6));
					producto.setPrecio(f.getDouble(7));
					producto.setMensaje(f.getString(8));
					producto.setEnviado(f.getString(9).equals("S") ? true : false);
					producto.setDestino(productoController.getDestinoByProducto(producto));
					PEDIDO.getProductos().add(producto);
					PEDIDO.setServicio(f.getString(10).equals("S") ? true : false);
					if (!f.getString(11).equals("0")) {
						PEDIDO.setCliente(getCienteByRuc(f.getString(13)));
					}
				} while(f.moveToNext());
			}
			conn.close();
		} else {
			String url = URL_HOST.concat("readMesaxNro.php?t=").concat(PEDIDO.getMesa());
			JSONArray obj = getJSONObject(url);
			for (int i = 0; i < obj.length(); i++) {
				JSONObject e = obj.getJSONObject(i);
				PEDIDO.setPax(e.getInt("pax"));
				if(e.getInt("mozo")>0) {
					PEDIDO.setMozo(Data.usuarioController.getUsuarioById(e.getInt("mozo")));
				}
				producto = new Producto();
				producto.setId(e.getInt("idproducto"));
				producto.setIdAtencion(e.getInt("idatencion"));
				producto.setNombre(e.getString("producto"));
				producto.setCantidad(e.getDouble("cantidad"));
				producto.setPrecio(e.getDouble("precio"));
				producto.setMensaje(e.getString("mensaje"));
				producto.setDestino(destinoController.getDestinoById(e.getInt("co_destino")));
				producto.setEnviado(e.getString("fl_envio").equals("S") ? true : false);
				PEDIDO.getProductos().add(producto);
			}
		}
	}

	public int insertPedido(PedidoController pedido, Producto producto, boolean sync) throws ClientProtocolException, IOException, JSONException {
		int id = 0;
		if (App.isCaja() && !sync) {
			Conn conn = new Conn(context);
			/*int atencion_id = 1;
			String[] campos = new String[] {"MAX(atencion_id)"};
			String[] args = new String[] {pedido.getMesa()};
			Cursor f = conn.getDB().query("pedidos", campos, "mesa=?", args, null, null, null);
			if (f.moveToFirst()) {
				atencion_id = f.getInt(0)+1;
			}
			f.close();*/
			ContentValues r = new ContentValues();
			r.put("mesa", pedido.getMesa());
			r.put("mozo_id", pedido.getMozo().getId());
			r.put("cajero_id", pedido.getCajero().getId());
			r.put("pax", pedido.getPax());
			//r.put("atencion_id", atencion_id);
			r.put("producto_id", producto.getId());
			r.put("producto", producto.getNombre());
			r.put("cantidad", producto.getCantidad());
			r.put("precio", producto.getPrecio());
			r.put("mensaje", producto.getMensaje());
			r.put("enviado", "N");
			r.put("destino_id", producto.getDestino().getId());
			r.put("servicio", pedido.isServicio() ? "S" : "N"); //AÑADIR A SYNC
			if (pedido.getCliente()!=null) { //AÑADIR A SYNC
				r.put("cliente_ruc", pedido.getCliente().getRuc());
			} else {
				r.put("cliente_ruc", "0");
			}
			r.put("sync", "");
			id = (int) conn.insert("pedidos", r);
			conn.close();
		}
		if (App.isPedido() || sync) {
			JSONObject row = new JSONObject();
			row.put("mesa", pedido.getMesa());
			row.put("mozoid", pedido.getMozo().getId());
			row.put("cajeroid", pedido.getCajero().getId());
			row.put("idprod", producto.getId());
			row.put("producto", producto.getNombre());
			row.put("cant", producto.getCantidad() + "");
			row.put("precio", producto.getPrecio());
			row.put("pax", pedido.getPax());
			row.put("co_destino", producto.getDestino().getId());
			row.put("pc", App.DEVICE_NAME);
			
			id = postData("insertPedidos.php", row);
		}
		return id;
	}

	public void deleteProducto(Producto producto, boolean sync) throws JSONException, ClientProtocolException, IOException{
		if (Globals.MODULO.equals(Globals.MODULO_CAJA) && !sync) {
			Conn conn = new Conn(context);
			conn.getDB().delete("pedidos", "id=?", new String[] {producto.getIdAtencion()+""});
			conn.close();
		} 
		if (Globals.MODULO.equals(Globals.MODULO_PEDIDO) || sync) {
			JSONObject obj = new JSONObject();
			obj.put("id", producto.getIdAtencion());
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(URL_HOST.concat("deletePedido.php"));
			httpPost.setHeader("Accept", "application/json");
	
			ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("data", obj.toString()));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	
			httpclient.execute(httpPost);
		}
	}

	public void updateProducto(Producto producto, boolean sync) throws JSONException, ClientProtocolException, IOException {
		if (Globals.MODULO.equals(Globals.MODULO_CAJA) && !sync) {
			ContentValues r = new ContentValues();
			r.put("cantidad", producto.getCantidad());
			r.put("producto", producto.getNombre());
			r.put("mensaje", producto.getMensaje());
			Conn conn = new Conn(context);
			conn.getDB().update("pedidos", r, "id=?", new String[] {producto.getIdAtencion()+""});
			conn.close();
		}
		if (Globals.MODULO.equals(Globals.MODULO_PEDIDO) || sync) {
			JSONObject obj = new JSONObject();
			obj.put("id", producto.getIdAtencion());
			obj.put("cant", producto.getCantidad());
			obj.put("prod", producto.getNombre());
			obj.put("msg", producto.getMensaje());
			//obj.put("precio", producto.getPrecio());
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(URL_HOST.concat("updatePedido.php"));
			httpPost.setHeader("Accept", "application/json");
	
			ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("data", obj.toString()));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	
			HttpResponse response = httpclient.execute(httpPost);
			//String temp = 
			EntityUtils.toString(response.getEntity());
			//Log.e("updatePedido.php", temp);
		}
	}
	
	public void updatePedido(PedidoController pedido, boolean sync) throws JSONException, ClientProtocolException, IOException {
		if (Globals.MODULO.equals(Globals.MODULO_CAJA) && !sync) {
			ContentValues r = new ContentValues();
			r.put("mozo_id", pedido.getMozo().getId());
			r.put("pax", pedido.getPax());
			r.put("servicio", pedido.isServicio() ? "S" : "N"); //AÑADIR A SYNC
			if (pedido.getCliente()!=null) { //AÑADIR A SYNC
				r.put("cliente_ruc", pedido.getCliente().getRuc());
			} else {
				r.put("cliente_ruc", "0");
			}
			Conn conn = new Conn(context);
			conn.getDB().update("pedidos", r, "mesa=?", new String[] {pedido.getMesa()});
			conn.close();
		}
		if (Globals.MODULO.equals(Globals.MODULO_PEDIDO) || sync) {
			JSONObject obj = new JSONObject();
			obj.put("nroatencion", pedido.getMesa());
			obj.put("pax", pedido.getPax());
			obj.put("mozo", pedido.getMozo().getId());
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(URL_HOST.concat("updatePaxMozo.php"));
			httpPost.setHeader("Accept", "application/json");
	
			ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("data", obj.toString()));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	
			//HttpResponse response = 
			httpclient.execute(httpPost);
			//String temp = EntityUtils.toString(response.getEntity());
			//Log.e("updatePaxMozo.php", temp);
		}
	}

	public void updateEnvio(PedidoController pedido, Destino destino, boolean sync) throws ClientProtocolException, IOException, JSONException {
		if (Globals.MODULO.equals(Globals.MODULO_CAJA) && !sync) {
			ContentValues r = new ContentValues();
			r.put("enviado", "S");
			Conn conn = new Conn(context);
			conn.getDB().update("pedidos", r, "mesa=? AND destino_id=?", new String[] {pedido.getMesa(), destino.getId()+""});
			conn.close();
		}
		if (Globals.MODULO.equals(Globals.MODULO_PEDIDO) || sync) {
			JSONObject obj = new JSONObject();
			obj.put("mesa", pedido.getMesa());
			obj.put("destino", destino.getId());
			
			String url = URL_HOST.concat("updateFlEnvio.php");
			HttpClient httpclient = new DefaultHttpClient(); // myParams
	
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Accept", "application/json");
	
			ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("data", obj.toString()));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	
			//HttpResponse response = 
			httpclient.execute(httpPost);
			//String temp = EntityUtils.toString(response.getEntity());
			//Log.i("updateFlEnvio.php", temp);
		}
	}
	
	public boolean pagarCuenta(PedidoController pedido, String tipoVenta, boolean sync) {
		boolean success = true;
		
		Conn conn = new Conn(context);
		conn.getDB().beginTransaction();
		try {
			ContentValues values;
			String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			String tipoDoc = "";
			String serie = "";
			int numero = 0;
			String ruc = "";
			String valor = pedido.getTotal().toString();
			int mozoId = pedido.getMozo().getId();
			int cajeroId = pedido.getCajero().getId();
			if (pedido.getCliente()==null) {
				tipoDoc = "BV";
				serie = cajaController.getCaja().getSerieBoleta();
				numero = cajaController.getCaja().getNumeroBoleta();
			} else {
				tipoDoc = "FV";
				serie = cajaController.getCaja().getSerieFactura();
				numero = cajaController.getCaja().getNumeroFactura();
				ruc = pedido.getCliente().getRuc();
			}
			
			//CABECERA DE VENTAS
			values = new ContentValues();
			values.put("fecha", fechaHora);
			values.put("tipo", tipoDoc);
			values.put("serie", serie);
			values.put("numero", numero);
			values.put("ruc", ruc);
			values.put("valor", valor);
			values.put("dscto", 0.0);
			values.put("mozo_id", mozoId);
			values.put("cajero_id", cajeroId);
			long ventaId = conn.insert("ventasc", values);
			
			//DETALLE DE VENTAS
			values = new ContentValues();
			values.put("venta_id", ventaId);
			for (Producto producto: pedido.getProductos()) {
				values.put("producto_id", producto.getId());
				values.put("producto", producto.getNombre());
				values.put("cantidad", producto.getCantidad());
				values.put("precio", producto.getPrecio());
				values.put("mensaje", producto.getMensaje());
				long ventaDId = conn.insert("ventasd", values);
			}
			
			//REGISTRO DE PAGO
			if (tipoVenta.equals(Globals.VENTA_RAPIDA)) {
				values = new ContentValues();
				values.put("venta_id", ventaId);
				values.put("moneda", "S");
				values.put("valor", valor);
				values.put("cambio", Globals.VA_TC);
				values.put("tarjeta", "EFECTIVO");
				conn.insert("ventasp", values);
			} else if (tipoVenta.equals(Globals.VENTA_DETALLADA)) {
				
			}
			
			conn.getDB().setTransactionSuccessful();
		} catch (SQLiteException e) {
			Log.e("SQLiteException", e.getMessage());
			success = false;
		} finally {
			conn.getDB().endTransaction();
			conn.close();
		}
		return success;
	}

	private static JSONArray getJSONObject(String url) throws ClientProtocolException, URISyntaxException, IOException, JSONException {
		JSONObject json = new JSONParser().getJSONFromUrl(url);
		return json.getJSONArray("data");
	}
	
	private int postData(String url, JSONObject row) throws ClientProtocolException, IOException, JSONException {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(URL_HOST.concat(url));
		//httpPost.setHeader("content-type", "application/json; charset=utf-8");
		
		List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("data", row.toString()));
		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));

		HttpResponse response = httpclient.execute(httpPost);
		String temp = EntityUtils.toString(response.getEntity());
		Log.i("OBJ JSON", row.toString());
		Log.i(url, temp);
		JSONObject json = new JSONObject(temp);
		return json.getInt("id");
	}
}
