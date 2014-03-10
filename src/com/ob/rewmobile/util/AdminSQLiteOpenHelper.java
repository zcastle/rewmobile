package com.ob.rewmobile.util;

import java.util.ArrayList;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
	
	private static String DB_NAME = "REWMobile.db";
	private ArrayList<Tabla> tablas = new ArrayList<Tabla>();
	
	public AdminSQLiteOpenHelper(Context context) {
		super(context, DB_NAME, null, 1);
		init();
	}

	
	private void init() {
		tablas.add(new Tabla("empresa", "create table empresa(id integer PRIMARY KEY, codigo text, ruc text, razon text, nombre text, direccion text, distrito text, igv real)"));
		tablas.add(new Tabla("centrocosto", "create table centrocosto(id integer PRIMARY KEY, codigo text, nombre text, direccion text, distrito text)"));
		tablas.add(new Tabla("equipo", "create table equipo(id integer PRIMARY KEY, nombre text, tipo text, serie_b text, numero_b text, serie_f text, numero_f text, impresora_p text, impresora_b text, impresora_f text, tc real, servicio real, dia integer)"));
		tablas.add(new Tabla("destinos", "create table destinos(id integer PRIMARY KEY, nombre text, ip text)"));
		tablas.add(new Tabla("productos", "create table productos(id integer PRIMARY KEY, codigo text, nombre text, precio real, categoria_id text, nu_orden integer, destino_id integer)"));
		tablas.add(new Tabla("categorias", "create table categorias(id integer PRIMARY KEY, codigo text, nombre text)"));
		tablas.add(new Tabla("usuarios", "create table usuarios(id integer PRIMARY KEY, nombre text, clave text, rol integer)"));
		tablas.add(new Tabla("pedidos", "create table pedidos(id integer PRIMARY KEY AUTOINCREMENT, mesa text, mozo_id integer, cajero_id integer, pax integer, producto_id integer, producto text, cantidad real, precio real, mensaje text, enviado text, destino_id text, servicio text, cliente_ruc text, sync text)"));
		tablas.add(new Tabla("ventasc", "create table ventasc(id integer PRIMARY KEY AUTOINCREMENT, fecha text, tipo text, serie text, numero text, ruc text, valor real, dscto real, mozo_id integer, cajero_id integer)"));
		tablas.add(new Tabla("ventasd", "create table ventasd(id integer PRIMARY KEY AUTOINCREMENT, venta_id integer, producto_id integer, producto text, cantidad real, precio real, mensaje text)"));
		tablas.add(new Tabla("ventasp", "create table ventasp(id integer PRIMARY KEY AUTOINCREMENT, venta_id integer, moneda text, valor real, cambio real, tarjeta text)"));
		tablas.add(new Tabla("departamento", "create table departamento(id integer PRIMARY KEY, codigo text, nombre text)"));
		tablas.add(new Tabla("provincia", "create table provincia(id integer PRIMARY KEY, codigo text, nombre text, departamento_id integer)"));
		tablas.add(new Tabla("distrito", "create table distrito(id integer PRIMARY KEY, codigo text, nombre text, provincia_id integer)"));
		tablas.add(new Tabla("clientes", "create table clientes(ruc text PRIMARY KEY, razon text, direccion text, distrito_id integer)"));
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (Tabla tabla: tablas) {
			//Log.e("QUERY", tabla.getQuery());
			db.execSQL(tabla.getQuery());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versionAnte, int versionNue) {
		for (Tabla tabla: tablas) {
			db.execSQL("drop table if exists ".concat(tabla.getNombre()));
		}
		onCreate(db);
	}
	
	class Tabla {
		private String nombre;
		private String query;
		
		public Tabla(String nombre, String query) {
			this.nombre = nombre;
			this.query = query;
		}

		public String getNombre() {
			return nombre;
		}

		public String getQuery() {
			return query;
		}
	}
}
