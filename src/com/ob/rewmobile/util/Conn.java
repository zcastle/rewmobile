package com.ob.rewmobile.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Conn {

	private AdminSQLiteOpenHelper adminDB = null;
	private SQLiteDatabase db = null;
	
	public Conn(Context context) {
		adminDB = new AdminSQLiteOpenHelper(context, "DBREWMobile", null, 1);
		db = adminDB.getWritableDatabase();
	}
	
	public SQLiteDatabase getDB() {
		//Log.e("PATH", db.getPath());
		return db;
	}
	
	public void close() {
		db.close();
		adminDB.close();
	}
	
	public Cursor getQuery(String tb, String[] fields, String where, String[] args) {
		return getDB().query(tb, fields, where, args, null, null, null);
	}
	
	public void insert(String tb, ContentValues values) {
		getDB().insert(tb, null, values);
	}

}
