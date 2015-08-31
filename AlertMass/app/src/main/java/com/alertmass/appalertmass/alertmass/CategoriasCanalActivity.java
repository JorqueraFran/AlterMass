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
import com.alertmass.appalertmass.alertmass.Data.Listas;
import com.alertmass.appalertmass.alertmass.util.AdapterListaCanalesSuscritos;
import com.alertmass.appalertmass.alertmass.util.AdapterListaCategorias;
import com.alertmass.appalertmass.alertmass.util.FuncionesUtiles;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class CategoriasCanalActivity extends Activity{
    private ListView ListaCategorias;
    AdapterListaCategorias aList;
    ProgressDialog pDialog;
    TextView lblMensajeCat;
    ArrayList<Listas> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias_cana);
        try{
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
            items = new ArrayList<Listas>();
            lblMensajeCat = (TextView) findViewById(R.id.lblMensajeCat);

            ListaCategorias = (ListView) findViewById(R.id.lstCategorias);
            if (FuncionesUtiles.paissession!=null)
            {
                if(FuncionesUtiles.verificaConexion(getApplicationContext())){
                    CargarListaCategorias();
                }else{
                    FuncionesUtiles.AvisoSinConexion(CategoriasCanalActivity.this);
                }
            }

        }catch (Exception e){
            FuncionesUtiles.LogError(e.getMessage().toString(),getApplicationContext());
        }
    }

    private void CargarListaCategorias(){
        try {
            new AsyncTask<Void, Void, Boolean>() {
                ProgressDialog pDialog = new ProgressDialog(CategoriasCanalActivity.this);
                @Override
                protected Boolean doInBackground(Void... params) {
                    //if (FuncionesUtiles.IsSession(CategoriasCanalActivity.this,null)){

                        final ParseQuery<ParseObject> queryCategorias = ParseQuery.getQuery("categorias");
                        queryCategorias.whereEqualTo("idpais",FuncionesUtiles.paissession);
                        List<ParseObject> list;
                        try {
                            list=queryCategorias.find();
                            int x=0;
                            for (ParseObject categorias : list) {
                                items.add(new Listas(x,categorias.getObjectId(),(String) categorias.get("nomcat"),(String) categorias.get("descat"),""));
                                x++;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    //}

                    return true;
                }
                @Override
                protected void onPreExecute() {
                    pDialog.setMessage("Cargando...");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {

                    aList  = new AdapterListaCategorias(CategoriasCanalActivity.this, items);
                    ListaCategorias.setAdapter(aList);
                    ListaCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                            Listas itemActual = (Listas) aList.getItem(pos);
                            // FuncionesUtiles.ToastMensaje(getApplicationContext(), "Posicion " + pos);
                            Intent intent = new Intent(CategoriasCanalActivity.this, SuscribirCanalActivity.class);
                            intent.putExtra("id", itemActual.GetIdObj());
                            intent.putExtra("titulo", itemActual.GetTitle());
                            startActivity(intent);
                            //overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                    });
                    pDialog.dismiss();
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK)
        {
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
        }
        return super.onKeyDown(keyCode, event);
    }
}
