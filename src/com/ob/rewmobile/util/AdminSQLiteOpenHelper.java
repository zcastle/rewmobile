package com.ob.rewmobile.util;

import java.util.ArrayList;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
	
	private ArrayList<Tabla> tablas = new ArrayList<Tabla>();
	
	public AdminSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		init();
	}

	public AdminSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		init();
	}
	
	private void init() {
		tablas.add(new Tabla("empresa", "create table empresa(id integer, codigo text, ruc text, razon text, nombre text, direccion text, distrito text, igv real)"));
		tablas.add(new Tabla("centrocosto", "create table centrocosto(id integer, codigo text, nombre text, direccion text, distrito text)"));
		tablas.add(new Tabla("equipo", "create table equipo(id integer, nombre text, tipo text, serie_b text, numero_b text, serie_f text, numero_f text, impresora_p text, impresora_b text, impresora_f text, tc real, servicio real, dia integer)"));
		tablas.add(new Tabla("destinos", "create table destinos(id integer, nombre text, ip text)"));
		tablas.add(new Tabla("productos", "create table productos(id integer, codigo text, nombre text, precio real, categoria_id text, nu_orden integer, destino_id integer)"));
		tablas.add(new Tabla("categorias", "create table categorias(id integer, codigo text, nombre text)"));
		tablas.add(new Tabla("usuarios", "create table usuarios(id integer, nombre text, clave text, rol integer)"));
		tablas.add(new Tabla("pedidos", "create table pedidos(mesa text, mozo_id integer, cajero_id integer, pax integer, atencion_id integer, producto_id integer, producto text, cantidad real, precio real, mensaje text, enviado text, destino_id text, servicio text, cliente_ruc text, sync text)"));
		tablas.add(new Tabla("ventasc", "create table ventasc(fecha text, tipo text, serie text, numero text, ruc text, nombre text, direcion text, distrito text, valor real, dscto real, mozo_id integer, cajero_id integer)"));
		tablas.add(new Tabla("ventasd", "create table ventasd(tipo text, serie text, numero text, id integer, producto text, cantidad real, precio real, mensaje text)"));
		tablas.add(new Tabla("ventasp", "create table ventasp(tipo text, serie text, numero text, moneda text, valor real, cambio real, tarjeta text)"));
		tablas.add(new Tabla("departamento", "create table departamento(id integer, codigo text, nombre text)"));
		tablas.add(new Tabla("provincia", "create table provincia(id integer, codigo text, nombre text, departamento_id integer)"));
		tablas.add(new Tabla("distrito", "create table distrito(id integer, codigo text, nombre text, provincia_id integer)"));
		tablas.add(new Tabla("clientes", "create table clientes(ruc text, razon text, direccion text, distrito_id integer)"));
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
