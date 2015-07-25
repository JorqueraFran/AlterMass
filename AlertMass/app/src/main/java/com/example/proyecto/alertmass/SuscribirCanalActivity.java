package com.example.proyecto.alertmass;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.proyecto.alertmass.Data.Listas;
import com.example.proyecto.alertmass.util.AdapterListaCanales;
import com.example.proyecto.alertmass.util.AdapterListaCanalesSuscritos;
import com.example.proyecto.alertmass.util.FuncionesUtiles;

import java.util.ArrayList;


public class SuscribirCanalActivity extends Activity {

    AdapterListaCanalesSuscritos aList;
    private ListView ListaCanalesSuscritos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suscribir_canal);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);

        ListaCanalesSuscritos = (ListView) findViewById(R.id.lstCanalesSus);
        CargarListaCanalesSuscritos();
    }

    private void CargarListaCanalesSuscritos(){
        ArrayList<Listas> items = new ArrayList<Listas>();
        for(int x = 1; x <= 10; x++){
            items.add(new Listas(x,"Canal " + x,"SubCanal " + x));
        }
        aList  = new AdapterListaCanalesSuscritos(SuscribirCanalActivity.this, items);
        ListaCanalesSuscritos.setAdapter(aList);
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
