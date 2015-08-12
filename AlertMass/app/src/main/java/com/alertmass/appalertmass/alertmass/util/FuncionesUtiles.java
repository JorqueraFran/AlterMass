package com.alertmass.appalertmass.alertmass.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alertmass.appalertmass.alertmass.CanalesActivity;
import com.alertmass.appalertmass.alertmass.Conexion.SessionApp;
import com.alertmass.appalertmass.alertmass.Data.DataLogin;
import com.alertmass.appalertmass.alertmass.Data.Listas;
import com.alertmass.appalertmass.alertmass.R;
import com.parse.ParseInstallation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Francisca on 12-07-2015.
 */
public abstract class FuncionesUtiles {
    public static SessionApp datasession;
    public static String usersession;
    public static String passsession;
    public static String correosession;
    public static String paissession;
    public static String telefonosession;
    public static int estadosession;
    public static int isfacebooksession;
    public static String JsonAlert;
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
            Cursor c = db.rawQuery("SELECT usr, pwr, correo, estado, isfacebook, pais, telefono FROM Usuario", null);
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
                        paissession = c.getString(5);
                        telefonosession = c.getString(6);
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

    public static void SetSession(String usr, String pwr, String Correo, int Estado, int IsFacebook, String Pais, String Telefono)
    {

        SQLiteDatabase db = datasession.getWritableDatabase();

        if (db != null)
        {
            db.execSQL("DELETE FROM Usuario");
            db.execSQL("INSERT INTO Usuario (usr, pwr, correo, estado, isfacebook, pais, telefono) VALUES ('" + usr + "', '" + pwr + "' , '" + Correo + "' , '" + Estado + "', '" + IsFacebook + "', '" + Pais + "', '" + Telefono + "')");
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

    public static void CargarSpinnerPaises(Context cnt, Spinner spinnerPais){
        int CountPais = DataLogin.GetPaises().size();
        String[] spinnerArray = new String[CountPais];
        Map<String, String> map = new HashMap<String, String>();
        int index = 0;
        for (Map.Entry<String, String> mapEntry : DataLogin.GetPaises().entrySet()) {
            spinnerArray[index] = mapEntry.getKey();
            index++;
        }
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(cnt,android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPais.setAdapter(adapter);
    }

    public static void SelPaisActual(Spinner spinnerPais,String IdPais){
        int CountPais = DataLogin.GetPaises().size();
        String[] spinnerArray = new String[CountPais];
        Map<String, String> map = new HashMap<String, String>();
        int index = 0;
        for (Map.Entry<String, String> mapEntry : DataLogin.GetPaises().entrySet()) {
            if (mapEntry.getValue().equals(IdPais)) {
                spinnerPais.setSelection(index);
                return;
            }
            index++;
        }

    }

    public static String GetDataAlert(){
        return JsonAlert;
    }

    public static void SetDataAlert(String DataAlert){
        JsonAlert = DataAlert;
    }

    public static void LogError(String Log,Context ctn){
        try
        {
            OutputStreamWriter FileAlert=
                    new OutputStreamWriter(
                            ctn.openFileOutput("LogAlertMass.txt", Context.MODE_PRIVATE));

            FileAlert.write(Log + "\n");
            FileAlert.close();

        }
        catch (Exception ex)
        {

        }
    }

    public static void GuardarCanales(String Canales,Context ctn, String IdUser){
        try
        {
            ContextWrapper cw = new ContextWrapper(ctn);
            File dirImages = cw.getDir("Files", Context.MODE_PRIVATE);
            File myPath = new File(dirImages, "CanalesAlertMass-"+IdUser+".txt");

            FileOutputStream fos = new FileOutputStream(myPath);
            OutputStreamWriter FileAlert=
                    new OutputStreamWriter(fos);

            FileAlert.write(Canales);
            FileAlert.close();

        }
        catch (Exception ex)
        {

        }
    }

    public static String LeerCanales(Context ctn,String IdUser){
        String TextoArchivo="";
        try
        {
            ContextWrapper cw = new ContextWrapper(ctn);
            File dirImages = cw.getDir("Files", Context.MODE_PRIVATE);
            File CanalFile = new  File(dirImages, "CanalesAlertMass-"+IdUser+".txt");
            if(CanalFile.exists())
            {
                FileInputStream fIn = new FileInputStream(CanalFile);
                InputStreamReader archivo = new InputStreamReader(fIn);
                BufferedReader br = new BufferedReader(archivo);
                TextoArchivo = br.readLine();
                br.close();
            }
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }
        return TextoArchivo;
    }

    public static void CargarListaCanales(TextView lblMsjCanal){

        try {
            ListView ListaCanales = (ListView) CanalesActivity.actCanal.findViewById(R.id.lstCanales);
            ArrayList<Listas> items = new ArrayList<Listas>();
            if (FuncionesUtiles.LeerCanales(CanalesActivity.actCanal,FuncionesUtiles.usersession)!=null) {

                String strCanales ="["+ FuncionesUtiles.LeerCanales(CanalesActivity.actCanal,FuncionesUtiles.usersession)+"]";

                JSONArray subscribedChannels = new JSONArray(strCanales);

                for(int x = 0; x < subscribedChannels.length(); x++){
                    items.add(new Listas(x,"",subscribedChannels.get(x).toString(),"" ,""));
                }
                lblMsjCanal.setVisibility(View.GONE);
                AdapterListaCanales aList = new AdapterListaCanales(CanalesActivity.actCanal, items);
                ListaCanales.setAdapter(aList);
            }else{
                lblMsjCanal.setVisibility(View.VISIBLE);
                AdapterListaCanales aList = new AdapterListaCanales(CanalesActivity.actCanal, items);
                ListaCanales.setAdapter(aList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean verificaConexion(Context ctx) {
        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        for (int i = 0; i < 2; i++) {
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        return bConectado;
    }

    public static void AvisoSinConexion(Context ctx){
        AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle("Aviso AlertMass");
        alertDialog.setMessage("AlertMass requiere que estes conectado a internet. Por favor intenta mas tarde cuando te hayas conectado a internet");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }

    public static String guardarImagen (Context context, String nombre, Bitmap imagen){
        ContextWrapper cw = new ContextWrapper(context);
        File dirImages = cw.getDir("Imagenes", Context.MODE_PRIVATE);
        File myPath = new File(dirImages, nombre.replace(" ","") + ".png");

        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(myPath);
            imagen.compress(Bitmap.CompressFormat.JPEG, 10, fos);
            fos.flush();
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return myPath.getAbsolutePath();
    }

    public static void MostrarLogoCanal(Context context, String nombre,ImageView imagenVW){
        ContextWrapper cw = new ContextWrapper(context);
        File dirImages = cw.getDir("Imagenes", Context.MODE_PRIVATE);
        File imgFile = new  File(dirImages, "Logo"+nombre + ".png");
        if(imgFile.exists())
        {
            imagenVW.setImageURI(Uri.fromFile(imgFile));
        }
    }

    public static void EliminarArchivo(Context ctn, String NombreArch){
        try
        {
            ContextWrapper cw = new ContextWrapper(ctn);
            File dir = cw.getDir("Imagenes", Context.MODE_PRIVATE);
            File file = new  File(dir, NombreArch);
            if(file.exists())
            {
                if(file.delete()){
                    Log.e("Eliminar Archivo","Correcto");
                }else{
                    Log.e("Eliminar Archivo","Fallido");
                }
            }
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }
    }

    public static void CapturaAlerta(Context context, Intent intent,String IdUser){
        Bundle extras = intent.getExtras();
        String SalidaJsonAlert="";
        String TextoArchivo = LeerArchivo(context,FuncionesUtiles.usersession);
        if(extras != null){

            String jsonData = extras.getString("com.parse.Data" );
            try
            {
                JSONObject jObject = new JSONObject(jsonData);
                JSONObject AlertDetJson = new JSONObject();

                AlertDetJson.put("Mensaje",jObject.getString("alert"));
                AlertDetJson.put("IdCanal",jObject.getString("idcanal"));
                AlertDetJson.put("NombreCanal",jObject.getString("nomcanal"));
                AlertDetJson.put("FechaEnvio",jObject.getString("fecenv"));
                AlertDetJson.put("HoraEnvio",jObject.getString("horenv"));

                if(TextoArchivo.isEmpty()){
                    SalidaJsonAlert = AlertDetJson.toString();
                }else{
                    SalidaJsonAlert = TextoArchivo + "," + AlertDetJson.toString();
                }
                ContextWrapper cw = new ContextWrapper(context);
                File dirImages = cw.getDir("Files", Context.MODE_PRIVATE);
                File myPath = new File(dirImages, "alerts" + IdUser + ".txt");

                FileOutputStream fos = new FileOutputStream(myPath);
                OutputStreamWriter FileAlert=
                        new OutputStreamWriter(fos);

                FileAlert.write(SalidaJsonAlert);
                FileAlert.close();
                FuncionesUtiles.SetDataAlert("{\"Data\":["+SalidaJsonAlert+"]}");
            }
            catch (Exception ex)
            {
                Log.e("Ficheros", "Error al escribir fichero a memoria interna");
            }
        }else{
            FuncionesUtiles.SetDataAlert("{\"Data\":[" + TextoArchivo + "]}");
        }
    }

    public static String LeerArchivo(Context context,String IdUser){
        String TextoArchivo="";
        try
        {
            ContextWrapper cw = new ContextWrapper(context);
            File dir = cw.getDir("Files", Context.MODE_PRIVATE);
            File File = new  File(dir, "alerts" + IdUser + ".txt");
            if(File.exists())
            {
                FileInputStream fIn = new FileInputStream(File);
                InputStreamReader archivo = new InputStreamReader(fIn);
                BufferedReader br = new BufferedReader(archivo);
                TextoArchivo = br.readLine();
                br.close();
            }
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }
        return TextoArchivo;
    }
}
