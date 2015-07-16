package com.example.proyecto.alertmass.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * Created by Francisca on 12-07-2015.
 */
public abstract class FuncionesUtiles {

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
}
