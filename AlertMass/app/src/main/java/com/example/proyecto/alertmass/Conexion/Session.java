package com.example.proyecto.alertmass.Conexion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by C-266 on 14/07/2015.
 */
public class Session extends SQLiteOpenHelper {
    private String TABLE = "CREATE TABLE Usuario (usr TEXT, pwr TEXT, correo TEXT)";

    public Session(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Usuario");
        db.execSQL(TABLE);
    }
}
