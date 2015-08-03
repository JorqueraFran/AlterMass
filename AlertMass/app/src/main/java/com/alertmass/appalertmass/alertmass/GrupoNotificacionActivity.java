package com.alertmass.appalertmass.alertmass;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.alertmass.appalertmass.alertmass.Data.Listas;
import com.alertmass.appalertmass.alertmass.util.AdapterListaNotificacion;
import com.alertmass.appalertmass.alertmass.util.FuncionesUtiles;

import java.util.ArrayList;


public class GrupoNotificacionActivity extends Activity {

    ImageView ibtnAddGrupoNoti;
    private ListView ListaGruposNoti;
    AdapterListaNotificacion aList;
    TextView lblTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo_notificacion);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        try{
            Bundle ext = getIntent().getExtras();

            lblTitulo = (TextView) findViewById(R.id.txtNomNotiG);
            lblTitulo.setText(ext.getString("titulo"));

            ibtnAddGrupoNoti = (ImageView) findViewById(R.id.ibtnAddGrupoNoti);
            ibtnAddGrupoNoti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IrActAddGrupoNoti();
                }
            });

            ListaGruposNoti = (ListView) findViewById(R.id.lstGrupoNoti);
            CargarListaGruposNoti();
        }catch (Exception e){
            FuncionesUtiles.LogError(e.getMessage().toString(),getApplicationContext());
        }
    }

    private void IrActAddGrupoNoti(){

    }

    private void CargarListaGruposNoti(){
        ArrayList<Listas> items = new ArrayList<Listas>();
        for(int x = 1; x <= 10; x++){
            items.add(new Listas(x,"","Notificacion " + x,"Descrpcion",""));
        }
        aList  = new AdapterListaNotificacion(GrupoNotificacionActivity.this, items);
        ListaGruposNoti.setAdapter(aList);
        ListaGruposNoti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Listas itemActual = (Listas)aList.getItem(pos);
               // FuncionesUtiles.ToastMensaje(getApplicationContext(), "Posicion " + pos);
                Intent intent = new Intent(GrupoNotificacionActivity.this, DetalleNotificacionActivity.class);
                intent.putExtra("titulo", itemActual.GetTitle());
                startActivity(intent);
                //overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

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
