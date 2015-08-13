package com.alertmass.appalertmass.alertmass;


import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;


import com.alertmass.appalertmass.alertmass.Conexion.Descargador;
import com.alertmass.appalertmass.alertmass.Conexion.Descargar;
import com.alertmass.appalertmass.alertmass.Conexion.IDescarga;
import com.alertmass.appalertmass.alertmass.Data.DataLogin;
import com.alertmass.appalertmass.alertmass.util.DescargaPaises;
import com.alertmass.appalertmass.alertmass.util.FuncionesUtiles;
import com.parse.Parse;
import com.parse.PushService;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Francisca on 12-07-2015.
 */
public class Application extends android.app.Application implements IDescarga {
    @Override
    public void onCreate() {
        super.onCreate();
        try{
            // Initialize the Parse SDK.
            //Cuenta Test Francisca
            Parse.initialize(this, "7c6N33iXsEEwIQireu2QVJh9pZS2ZmTqMG5gYXzW", "nP01sUz0FMpB9N7OxlxtfkMIZ9JsrJy5m2oUG7ma");

            //Cuenta App AlertMass
            //Parse.initialize(this, "1FPKiWd4niGvmuFEistvBzy8QdEKRkAapW9E1ZNy", "6HwEgL6uMh9zSGPndGdU3atLufwYwSNHwrpRQBiT");
            PushService.setDefaultPushCallback(this, MainActivity.class);

            //DescargarPaises();
            if(DataLogin.GetPaises().size()<=0){
                DescargaPaises Descarga_Paises = new DescargaPaises();
                Descarga_Paises.SetUrlDescargaPais(getResources().getString(R.string.SERVICIO_PAISES));
                Descarga_Paises.DescargarListaPaises();
            }
        }catch (Exception e){

            FuncionesUtiles.LogError(e.getMessage().toString(), getApplicationContext());
        }

    }


    private void DescargarPaises(){
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                Descargador descargador = new Descargador();
                Descargar descarga = new Descargar();
                descarga.urlDescarga = getResources().getString(R.string.SERVICIO_PAISES);
                descarga.isPost = false;
                descarga.callback = Application.this;
                descargador.execute(descarga);
                return true;
            }

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {


            }
        }.execute();
    }
    @Override
    public void TerminoDescarga(Descargar descarga, byte[] data) {
        String strJSON = null;
        try {
            strJSON = new String(data, "UTF-8");
            JSONArray jArray = new JSONArray(strJSON);
            String[] spinnerArray = new String[jArray.length()];
            HashMap<String,String> listpais= new HashMap<String,String>();
            List<String> NomPais = new ArrayList<String>();
            for(int i=0; i < jArray.length();i++){
                if(jArray.getJSONObject(i).getBoolean("sta")){
                    listpais.put(jArray.getJSONObject(i).getString("nompais"),jArray.getJSONObject(i).getString("_id"));
                    spinnerArray[i] = jArray.getJSONObject(i).getString("nompais");
                }
            }
            DataLogin.SetPaises(listpais);

        } catch (Exception ex) {
            FuncionesUtiles.ToastMensaje(this, "Error al Cargar Paises");
        }
    }

    @Override
    public void ErrorDescarga(Descargar descarga, int codigoError, String descripcion) {
        FuncionesUtiles.ToastMensaje(this, "Error al Cargar Paises");
    }

    @Override
    public void ProgresoDescarga(Float porcentaje) {

    }
}
