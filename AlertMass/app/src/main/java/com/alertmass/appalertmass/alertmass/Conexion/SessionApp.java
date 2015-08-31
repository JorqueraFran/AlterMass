package com.alertmass.appalertmass.alertmass.Conexion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by C-266 on 14/07/2015.
 */
public class SessionApp extends SQLiteOpenHelper {
    private String TABLE = "CREATE TABLE Usuario (pais TEXT)";

    public SessionApp(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
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
