package com.alertmass.appalertmass.alertmass.util;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.alertmass.appalertmass.alertmass.Conexion.Descargador;
import com.alertmass.appalertmass.alertmass.Conexion.Descargar;
import com.alertmass.appalertmass.alertmass.Conexion.IDescarga;
import com.alertmass.appalertmass.alertmass.Data.DataLogin;
import com.alertmass.appalertmass.alertmass.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Francisca on 01-08-2015.
 */
public class DescargaPaises implements IDescarga {

    private String Url="";
    public void SetUrlDescargaPais(String servicio){
        Url=servicio;
    }

    private String GettUrlDescargaPais(){
        return Url;
    }
    public void DescargarListaPaises(){
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                Descargador descargador = new Descargador();
                Descargar descarga = new Descargar();
                descarga.urlDescarga = GettUrlDescargaPais();//getResources().getString(R.string.SERVICIO_PAISES);
                descarga.isPost = false;
                descarga.callback = DescargaPaises.this;
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
            //FuncionesUtiles.ToastMensaje(activity, "Error al Cargar Paises");
        }
    }

    @Override
    public void ErrorDescarga(Descargar descarga, int codigoError, String descripcion) {

    }

    @Override
    public void ProgresoDescarga(Float porcentaje) {

    }

}
