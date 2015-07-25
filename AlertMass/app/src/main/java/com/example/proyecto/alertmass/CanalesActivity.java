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
import android.widget.ImageView;
import android.widget.ListView;

import com.example.proyecto.alertmass.Data.DataLogin;
import com.example.proyecto.alertmass.Data.Listas;
import com.example.proyecto.alertmass.util.AdapterListaCanales;
import com.example.proyecto.alertmass.util.AdapterListaNotificacion;
import com.example.proyecto.alertmass.util.FuncionesUtiles;

import java.util.ArrayList;


public class CanalesActivity extends Activity {

    private DataLogin datalogin;
    private ListView ListaCanales;
    AdapterListaCanales aList;
    ImageView ibtnAddCanal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canales);
        datalogin= DataLogin.EntregarDataLogin();
        ibtnAddCanal = (ImageView) findViewById(R.id.ibtnAddCanal);
        ibtnAddCanal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IrActAddGrupo();
            }
        });
        ListaCanales = (ListView) findViewById(R.id.lstCanales);
        CargarListaCanales();
    }

    private void IrActAddGrupo(){
        Intent intent = new Intent(CanalesActivity.this, SuscribirCanalActivity.class);
        startActivity(intent);

    }

    private void CargarListaCanales(){
        ArrayList<Listas> items = new ArrayList<Listas>();
        for(int x = 1; x <= 10; x++){
            items.add(new Listas(x,"Canal " + x,"SubCanal " + x));
        }
        aList  = new AdapterListaCanales(CanalesActivity.this, items);
        ListaCanales.setAdapter(aList);
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
