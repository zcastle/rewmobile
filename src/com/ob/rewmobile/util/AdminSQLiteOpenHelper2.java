package com.ob.rewmobile.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class AdminSQLiteOpenHelper2 extends SQLiteOpenHelper {
	
	private static String DB_PATH = Environment.getExternalStorageDirectory().getPath().concat("/");
	private static String DB_NAME = "REWMobile.db";

	private Context context;
	private SQLiteDatabase myDataBase;
	
	public AdminSQLiteOpenHelper2(Context context) {
		super(context, DB_NAME, null, 1);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versionAnte, int versionNue) {
	}
	
	public void createDataBase() throws IOException{
		 
		boolean dbExist = checkDataBase();
		 
		if(dbExist){
			//la base de datos existe y no hacemos nada.
			//Log.e("EXISTE", "SI");
		}else{
			//Log.e("EXISTE", "NO");
			//Llamando a este método se crea la base de datos vacía en la ruta por defecto del sistema
			//de nuestra aplicación por lo que podremos sobreescribirla con nuestra base de datos.
			this.getReadableDatabase();
			 
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copiando Base de Datos");
			}
		}
	}
	
	private boolean checkDataBase(){
		SQLiteDatabase checkDB = null;
			 
		try{
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		}catch(SQLiteException e){
			//si llegamos aqui es porque la base de datos no existe todavía.
		}
		if(checkDB != null){
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	private void copyDataBase() throws IOException{		 
		//Abrimos el fichero de base de datos como entrada
		InputStream myInput = context.getAssets().open(DB_NAME);
		 
		//Ruta a la base de datos vacía recién creada
		String outFileName = DB_PATH + DB_NAME;
		 
		//Abrimos la base de datos vacía como salida
		OutputStream myOutput = new FileOutputStream(outFileName);
		 
		//Transferimos los bytes desde el fichero de entrada al de salida
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0){
		myOutput.write(buffer, 0, length);
		}
		 
		//Liberamos los streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	 
	}

	public SQLiteDatabase open() throws SQLException{
		//Abre la base de datos
		try {
			createDataBase();
		} catch (IOException e) {
			throw new Error("Ha sido imposible crear la Base de Datos");
		}
		 
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
		return myDataBase;
	}
	
	public SQLiteDatabase getDb() {
		return myDataBase;
	}

	@Override
	public synchronized void close() {
		if(myDataBase != null)
			myDataBase.close();
		super.close();
	}
	
	public void backupDatabase() throws IOException {
        //Open your local db as the input stream
        String inFileName = DB_PATH + DB_NAME;
        File dbFile = new File(inFileName);
        FileInputStream fis = new FileInputStream(dbFile);

        String outFileName = Environment.getExternalStorageDirectory()+"/"+DB_NAME+".cp";
        //Open the empty db as the output stream
        OutputStream output = new FileOutputStream(outFileName);
        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer))>0){
            output.write(buffer, 0, length);
        }
        //Close the streams
        output.flush();
        output.close();
        fis.close();
        //Toast.makeText(context, "FIN BACKUP", Toast.LENGTH_SHORT);
    }
}
