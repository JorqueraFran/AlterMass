package com.example.proyecto.alertmass.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.proyecto.alertmass.Conexion.SessionApp;

/**
 * Created by Francisca on 12-07-2015.
 */
public abstract class FuncionesUtiles {
    public static SessionApp datasession;
    public static String usersession;
    public static String passsession;
    public static String correosession;
    public static int estadosession;
    public static int isfacebooksession;
    public static boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    public static void ProgressDialog(ProgressDialog pDialog,String Mensaje){
        pDialog.setMessage(Mensaje);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public static void CancelarDialog(ProgressDialog pDialog){
        pDialog.dismiss();
    }

    public static void ToastMensaje(Context ctnx, String Mensaje){
        Toast.makeText(ctnx, Mensaje, Toast.LENGTH_SHORT).show();
    }

    public static void EnviarCorreoPassword(Context cntx){
        String Asunto = "Recuperar Contrasena";
        String Destinatario = "jorquera.fran@gmail.com";
        String Mensaje = "Esto es una prueba de recuperar contrasena";
        Intent SendMail = new Intent(Intent.ACTION_SEND);
        SendMail.setType("plain/text");
        SendMail.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{Destinatario});
        SendMail.putExtra(android.content.Intent.EXTRA_SUBJECT, Asunto);
        SendMail.putExtra(android.content.Intent.EXTRA_TEXT, Mensaje);
        cntx.startActivity(SendMail);
    }

    public static void OcultarTeclado(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static boolean isPasswordValid(String password) {
        return password.length() > 6;
    }

    public static boolean IsSession(Context context,SQLiteDatabase.CursorFactory factory)
    {
        datasession = new SessionApp(context, "AlertMass", null, 1);
        SQLiteDatabase db = datasession.getWritableDatabase();

        if (db != null)
        {
            //db.execSQL("delete from Usuario");
            Cursor c = db.rawQuery("SELECT usr, pwr, correo, estado,isfacebook FROM Usuario", null);
            int count = c.getCount();
            if (c.getCount() > 0)
            {
                if (c.moveToFirst())
                {
                    do
                    {
                        usersession = c.getString(0);
                        passsession = c.getString(1);
                        correosession = c.getString(2);
                        estadosession = c.getInt(3);
                        isfacebooksession = c.getInt(4);
                    }
                    while (c.moveToNext());
                }

                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public static void SetSession(String usr, String pwr, String Correo, int Estado, int IsFacebook)
    {

        SQLiteDatabase db = datasession.getWritableDatabase();

        if (db != null)
        {
            db.execSQL("DELETE FROM Usuario");
            db.execSQL("INSERT INTO Usuario (usr, pwr, correo, estado, isfacebook) VALUES ('" + usr + "', '" + pwr + "' , '" + Correo + "' , '" + Estado + "', '" + IsFacebook + "')");
            db.close();
        }
    }
    public static void DeleteSession()
    {

        SQLiteDatabase db = datasession.getWritableDatabase();

        if (db != null)
        {
            db.execSQL("DELETE FROM Usuario");
        }
    }

    public static boolean Boolean(int bool){
        if(bool==1){
            return true;
        }else{
            return false;
        }
    }

}
