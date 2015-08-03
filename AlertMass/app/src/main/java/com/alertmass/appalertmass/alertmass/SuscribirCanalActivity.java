package com.alertmass.appalertmass.alertmass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.alertmass.appalertmass.alertmass.Conexion.Descargador;
import com.alertmass.appalertmass.alertmass.Conexion.Descargar;
import com.alertmass.appalertmass.alertmass.Conexion.IDescarga;
import com.alertmass.appalertmass.alertmass.Data.DataLogin;
import com.alertmass.appalertmass.alertmass.Data.Listas;
import com.alertmass.appalertmass.alertmass.R;
import com.alertmass.appalertmass.alertmass.util.AdapterListaCanalesSuscritos;
import com.alertmass.appalertmass.alertmass.util.AdapterListaNotificacion;
import com.alertmass.appalertmass.alertmass.util.FuncionesUtiles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class SuscribirCanalActivity extends Activity implements IDescarga {

    AdapterListaCanalesSuscritos aList;
    private ListView ListaCanalesSuscritos;
    private String IdCat="";
    private String NomCat;
    private DataLogin datalogin = new DataLogin();
    private TextView lblCategoriaCanal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_suscribir_canal);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
            Bundle ext = getIntent().getExtras();
            IdCat= ext.getString("id").toString();
            NomCat = ext.getString("titulo").toString();
            ListaCanalesSuscritos = (ListView) findViewById(R.id.lstCanalesSus);
            lblCategoriaCanal = (TextView) findViewById(R.id.lblCategoriaCanal);
            lblCategoriaCanal.setText(NomCat);
            CargarListaCanalesSuscritos();
        }catch (Exception e){
            FuncionesUtiles.LogError(e.getMessage().toString(),getApplicationContext());
        }
    }

    private void CargarListaCanalesSuscritos(){
        try {
            new AsyncTask<Void, Void, Boolean>() {
                ProgressDialog pDialog = new ProgressDialog(SuscribirCanalActivity.this);
                String headerPWD = datalogin.GetCorreoUser()+":"+datalogin.GetPassUser();
                byte[] data = headerPWD.getBytes("UTF-8");
                String headerPWDbase64 = Base64.encodeToString(data, Base64.DEFAULT).replace("\n", "");
                @Override
                protected Boolean doInBackground(Void... params) {
                    Descargador descargador = new Descargador();
                    Descargar descarga = new Descargar();
                    descarga.urlDescarga = getResources().getString(R.string.SERVICIO_CANALES_SUSCRIBCION)+IdCat;
                    descarga.headersPWD = headerPWDbase64;//datalogin.GetPaisUser();
                    descarga.isPost = false;
                    descarga.callback = SuscribirCanalActivity.this;
                    descargador.execute(descarga);
                    return true;
                }

                @Override
                protected void onPreExecute() {
                    pDialog.setMessage("Cargando Canales de " + NomCat + "...");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    pDialog.dismiss();

                }
            }.execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK)
        {
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void TerminoDescarga(Descargar descarga, byte[] data) {
        String strJSON = null;
        try {
            strJSON = new String(data, "UTF-8");
            JSONArray jArray = new JSONArray(strJSON);

            ArrayList<Listas> items = new ArrayList<Listas>();
            for(int x = 0; x < jArray.length(); x++){
                try {
                    JSONObject json = jArray.getJSONObject(x);
                    items.add(new Listas(x,json.getString("_id"),json.getString("name"),json.getString("desc"),""));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            aList  = new AdapterListaCanalesSuscritos(SuscribirCanalActivity.this, items);
            ListaCanalesSuscritos.setAdapter(aList);


            Log.e("JSON_CATEGORIAS", strJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ErrorDescarga(Descargar descarga, int codigoError, String descripcion) {
        Log.e("Error",descripcion);
    }

    @Override
    public void ProgresoDescarga(Float porcentaje) {

    }
}
