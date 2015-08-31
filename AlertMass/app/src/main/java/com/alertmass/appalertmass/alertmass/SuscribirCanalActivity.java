package com.alertmass.appalertmass.alertmass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alertmass.appalertmass.alertmass.Data.Listas;
import com.alertmass.appalertmass.alertmass.util.AdapterListaCanalesSuscritos;
import com.alertmass.appalertmass.alertmass.util.AdapterListaCategorias;
import com.alertmass.appalertmass.alertmass.util.FuncionesUtiles;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class SuscribirCanalActivity extends Activity{

    AdapterListaCanalesSuscritos aList;
    private ListView ListaCanalesSuscritos;
    private String IdCat="";
    private String NomCat;
    private TextView lblCategoriaCanal;
    ArrayList<Listas> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(FuncionesUtiles.verificaConexion(getApplicationContext())){
            try{
                setContentView(R.layout.activity_suscribir_canal);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                if (FuncionesUtiles.IsSession(SuscribirCanalActivity.this,null)){

                }
                items = new ArrayList<Listas>();
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
        }else{
            FuncionesUtiles.AvisoSinConexion(SuscribirCanalActivity.this);
        }
    }

    private void CargarListaCanalesSuscritos(){
        try {
            new AsyncTask<Void, Void, Boolean>() {
                ProgressDialog pDialog = new ProgressDialog(SuscribirCanalActivity.this);
                @Override
                protected Boolean doInBackground(Void... params) {
                    final ParseQuery<ParseObject> queryCanales = ParseQuery.getQuery("canales");
                    queryCanales.whereEqualTo("idcat", IdCat);
                    List<ParseObject> list;
                    try {
                        list=queryCanales.find();
                        int x=0;
                        for (ParseObject canales : list) {
                            items.add(new Listas(x,canales.getObjectId(),(String) canales.get("nomcanal"),(String) canales.get("descanal"),""));
                            x++;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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
                    if(aBoolean){
                        aList  = new AdapterListaCanalesSuscritos(SuscribirCanalActivity.this, items);
                        ListaCanalesSuscritos.setAdapter(aList);
                        pDialog.dismiss();
                    }


                }
            }.execute();
        } catch (Exception e) {
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

}
