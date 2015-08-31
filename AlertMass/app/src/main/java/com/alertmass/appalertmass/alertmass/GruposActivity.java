package com.alertmass.appalertmass.alertmass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.alertmass.appalertmass.alertmass.Data.Listas;
import com.alertmass.appalertmass.alertmass.util.AdapterListaCategorias;
import com.alertmass.appalertmass.alertmass.util.AdapterListaNotificacion;
import com.alertmass.appalertmass.alertmass.util.FuncionesUtiles;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class GruposActivity extends Activity {

    private ListView ListaGrupos;
    AdapterListaNotificacion aList;
    ArrayList<Listas> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos);
        try{

            ListaGrupos = (ListView) findViewById(R.id.lstGrupos);
            items = new ArrayList<Listas>();
            if (FuncionesUtiles.paissession!=null)
            {
                if(FuncionesUtiles.verificaConexion(getApplicationContext())){
                    CargarListaAlerta();
                }else{
                    FuncionesUtiles.AvisoSinConexion(GruposActivity.this);
                }
            }

        }catch (Exception e){
            FuncionesUtiles.LogError(e.getMessage().toString(),getApplicationContext());
        }
    }

    private void CargarListaAlerta(){
        try {
            new AsyncTask<Void, Void, Boolean>() {
                ProgressDialog pDialog = new ProgressDialog(GruposActivity.this);
                @Override
                protected Boolean doInBackground(Void... params) {
                    //if (FuncionesUtiles.IsSession(CategoriasCanalActivity.this,null)){

                    final ParseQuery<ParseObject> querygrupo = ParseQuery.getQuery("grupos");
                    List<ParseObject> list;
                    try {
                        list=querygrupo.find();
                        int x=0;
                        for (ParseObject grupos : list) {
                            items.add(new Listas(x,grupos.getObjectId(),(String) grupos.get("nomgrupo"),(String) grupos.get("desgrupo"),""));
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

                    aList  = new AdapterListaNotificacion(GruposActivity.this, items);
                    ListaGrupos.setAdapter(aList);
                    ListaGrupos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                            Listas itemActual = (Listas) aList.getItem(pos);
                            // FuncionesUtiles.ToastMensaje(getApplicationContext(), "Posicion " + pos);
                            Intent intent = new Intent(GruposActivity.this, GrupoNotificacionActivity.class);
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK)
        {
            Intent main = new Intent(Intent.ACTION_MAIN);
            main.addCategory(Intent.CATEGORY_HOME);
            main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(main);
        }
        return super.onKeyDown(keyCode, event);
    }
}
