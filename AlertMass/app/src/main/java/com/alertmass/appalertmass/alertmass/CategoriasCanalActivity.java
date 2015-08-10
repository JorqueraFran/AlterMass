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
import android.widget.TextView;

import com.alertmass.appalertmass.alertmass.Conexion.Descargador;
import com.alertmass.appalertmass.alertmass.Conexion.Descargar;
import com.alertmass.appalertmass.alertmass.Conexion.IDescarga;
import com.alertmass.appalertmass.alertmass.Data.DataLogin;
import com.alertmass.appalertmass.alertmass.Data.Listas;
import com.alertmass.appalertmass.alertmass.util.AdapterListaCategorias;
import com.alertmass.appalertmass.alertmass.util.AdapterListaNotificacion;
import com.alertmass.appalertmass.alertmass.util.FuncionesUtiles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class CategoriasCanalActivity extends Activity implements IDescarga {
    private ListView ListaCategorias;
    AdapterListaCategorias aList;
    private DataLogin datalogin;
    ProgressDialog pDialog;
    TextView lblMensajeCat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias_cana);
        try{
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
            pDialog = new ProgressDialog(CategoriasCanalActivity.this);
            pDialog.setMessage("Cargando Categorias...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            lblMensajeCat = (TextView) findViewById(R.id.lblMensajeCat);
            if (FuncionesUtiles.IsSession(CategoriasCanalActivity.this, null)){
                if(datalogin==null){
                    datalogin= DataLogin.EntregarDataLogin();

                }
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
            String Pass = datalogin.GetPassUser();
            String Correo = datalogin.GetCorreoUser();
            int isFacebook = datalogin.GetIsFacebook();
            final String headerPWD;
            if(isFacebook == 1){
                headerPWD= Correo+"::true";
            }else{
                headerPWD= Correo+":"+Pass+":false";
            }
            new AsyncTask<Void, Void, Boolean>() {

                byte[] data = headerPWD.getBytes("UTF-8");
                String headerPWDbase64 = Base64.encodeToString(data, Base64.DEFAULT).replace("\n", "");
                @Override
                protected Boolean doInBackground(Void... params) {
                    Descargador descargador = new Descargador();
                    Descargar descarga = new Descargar();
                    descarga.urlDescarga = getResources().getString(R.string.SERVICIO_CATEGORIA);
                    descarga.headersPWD = headerPWDbase64;
                    descarga.headersPais = datalogin.GetIPaisUser();
                    descarga.isPost = false;
                    descarga.callback = CategoriasCanalActivity.this;
                    descargador.execute(descarga);
                    return true;
                }

                @Override
                protected void onPreExecute() {

                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    pDialog.dismiss();

                }
            }.execute().get(15, TimeUnit.SECONDS);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void TerminoDescarga(Descargar descarga, byte[] data) {
        lblMensajeCat.setVisibility(View.GONE);
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
            aList  = new AdapterListaCategorias(CategoriasCanalActivity.this, items);
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
        lblMensajeCat.setVisibility(View.VISIBLE);
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
