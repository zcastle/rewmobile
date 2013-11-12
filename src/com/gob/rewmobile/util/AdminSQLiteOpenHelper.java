package com.gob.rewmobile.util;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

	private SQLiteDatabase mDb;
	
	public AdminSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public AdminSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}
	
	

	/*@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
		this.mDb = db;
	}*/

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table productos(id integer, co_producto text, no_producto text, va_producto real, co_categoria text, no_categoria text, nu_orden integer, co_destino integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versionAnte, int versionNue) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists productos");
		db.execSQL("create table productos(id integer, co_producto text, no_producto text, va_producto real, co_categoria text, no_categoria text, nu_orden integer, co_destino integer)");
	}
	
	/*public Cursor searchByInputText(String inputText) throws SQLException {
		 
        String query = "SELECT id, no_producto from productos where no_producto MATCH '" + inputText + "';";
 
        Cursor mCursor = mDb.rawQuery(query, null);
 
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
 
    }*/

}
