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
import android.widget.TextView;

import com.example.proyecto.alertmass.Data.DataLogin;
import com.example.proyecto.alertmass.Data.Listas;
import com.example.proyecto.alertmass.util.AdapterListaNotificacion;
import com.example.proyecto.alertmass.util.FuncionesUtiles;

import java.util.ArrayList;


public class NotificacionesActivity extends Activity {
    private DataLogin datalogin;
    private ListView ListaAlertas;
    AdapterListaNotificacion aList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);
        datalogin= DataLogin.EntregarDataLogin();

        ListaAlertas = (ListView) findViewById(R.id.lstNoti);
        CargarListaAlerta();
    }

    private void CargarListaAlerta(){
        ArrayList<Listas> items = new ArrayList<Listas>();
        for(int x = 1; x <= 10; x++){
            items.add(new Listas(x,"Alerta " + x,"SubAlerta " + x));
        }
        aList  = new AdapterListaNotificacion(NotificacionesActivity.this, items);
        ListaAlertas.setAdapter(aList);
        ListaAlertas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Listas itemActual = (Listas)aList.getItem(pos);
                //FuncionesUtiles.ToastMensaje(getApplicationContext(), "Posicion " + pos);
                Intent intent = new Intent(NotificacionesActivity.this, DetalleNotificacionActivity.class);
                intent.putExtra("titulo", itemActual.GetTitle());
                startActivity(intent);

            }
        });
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
