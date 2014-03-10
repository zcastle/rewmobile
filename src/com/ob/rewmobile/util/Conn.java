package com.ob.rewmobile.util;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Conn {

	private AdminSQLiteOpenHelper2 adminDB = null;
	private SQLiteDatabase db = null;
	
	public Conn(Context context) {
		adminDB = new AdminSQLiteOpenHelper2(context);
		try {
			db = adminDB.open();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//db = adminDB.getWritableDatabase();
	}
	
	public void backup() throws IOException {
		//adminDB.backupDatabase();
	}
	
	public SQLiteDatabase getDB() {
		//Log.e("PATH", db.getPath());
		return db;
	}
	
	public void close() {
		db.close();
		adminDB.close();
	}
	
	public Cursor query(String tb, String[] fields, String where, String[] args) {
		return getDB().query(tb, fields, where, args, null, null, null);
	}
	
	public long insert(String tb, ContentValues values) {
		return getDB().insert(tb, null, values);
	}

}
