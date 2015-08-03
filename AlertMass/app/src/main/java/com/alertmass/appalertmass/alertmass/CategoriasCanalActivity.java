package com.alertmass.appalertmass.alertmass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alertmass.appalertmass.alertmass.Conexion.Descargador;
import com.alertmass.appalertmass.alertmass.Conexion.Descargar;
import com.alertmass.appalertmass.alertmass.Conexion.IDescarga;
import com.alertmass.appalertmass.alertmass.Data.DataLogin;
import com.alertmass.appalertmass.alertmass.Data.Listas;
import com.alertmass.appalertmass.alertmass.util.AdapterListaNotificacion;
import com.alertmass.appalertmass.alertmass.util.FuncionesUtiles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CategoriasCanalActivity extends Activity implements IDescarga {
    private ListView ListaCategorias;
    AdapterListaNotificacion aList;
    private DataLogin datalogin = new DataLogin();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias_cana);
        try{
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
            if (FuncionesUtiles.IsSession(CategoriasCanalActivity.this, null)){
                // DataLogin.ProcesarSession(FuncionesUtiles.correosession,FuncionesUtiles.usersession,FuncionesUtiles.passsession,FuncionesUtiles.estadosession,FuncionesUtiles.isfacebooksession);
                datalogin= DataLogin.EntregarDataLogin();
            }
            ListaCategorias = (ListView) findViewById(R.id.lstCategorias);
            if(FuncionesUtiles.verificaConexion(getApplicationContext())){
                CargarListaCategorias();
            }else{
                FuncionesUtiles.AvisoSinConexion(CategoriasCanalActivity.this);
            }

        }catch (Exception e){
            FuncionesUtiles.LogError(e.getMessage().toString(),getApplicationContext());
        }
    }

    private void CargarListaCategorias(){
        try {
            new AsyncTask<Void, Void, Boolean>() {
                ProgressDialog pDialog = new ProgressDialog(CategoriasCanalActivity.this);
                String headerPWD = datalogin.GetCorreoUser()+":"+datalogin.GetPassUser();
                byte[] data = headerPWD.getBytes("UTF-8");
                String headerPWDbase64 = Base64.encodeToString(data, Base64.DEFAULT).replace("\n", "");
                @Override
                protected Boolean doInBackground(Void... params) {
                    Descargador descargador = new Descargador();
                    Descargar descarga = new Descargar();
                    descarga.urlDescarga = getResources().getString(R.string.SERVICIO_CATEGORIA);
                    descarga.headersPWD = headerPWDbase64;
                    descarga.headersPais = "55afe9edd3b9759518000001";//datalogin.GetPaisUser();
                    descarga.isPost = false;
                    descarga.callback = CategoriasCanalActivity.this;
                    descargador.execute(descarga);
                    return true;
                }

                @Override
                protected void onPreExecute() {
                    pDialog.setMessage("Cargando Categorias...");
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
    public void TerminoDescarga(Descargar descarga, byte[] data) {
        String strJSON = null;
        try {
            strJSON = new String(data, "UTF-8");
            JSONArray jArray = new JSONArray(strJSON);

            ArrayList<Listas> items = new ArrayList<Listas>();
            for(int x = 0; x <= jArray.length(); x++){
                try {
                    JSONObject json = jArray.getJSONObject(x);
                    items.add(new Listas(x,json.getString("_id"),json.getString("name"),json.getString("desc"),""));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            aList  = new AdapterListaNotificacion(CategoriasCanalActivity.this, items);
            ListaCategorias.setAdapter(aList);
            ListaCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                        Listas itemActual = (Listas)aList.getItem(pos);
                        //FuncionesUtiles.ToastMensaje(getApplicationContext(),itemActual.GetIdObj());
                        Intent intent = new Intent(CategoriasCanalActivity.this, SuscribirCanalActivity.class);
                        intent.putExtra("titulo", itemActual.GetTitle());
                        intent.putExtra("id",itemActual.GetIdObj());
                        startActivity(intent);

                    }
                });

            Log.e("JSON_CATEGORIAS", strJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ErrorDescarga(Descargar descarga, int codigoError, String descripcion) {
        Log.e("JSON_CATEGORIAS-ERROR","Error " + descripcion);
    }

    @Override
    public void ProgresoDescarga(Float porcentaje) {

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
}
