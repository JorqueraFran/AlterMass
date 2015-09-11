package com.alertmass.appalertmass.alertmass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ListView;

import com.alertmass.appalertmass.alertmass.Data.Listas;
import com.alertmass.appalertmass.alertmass.util.AdapterListaNotificacion;
import com.alertmass.appalertmass.alertmass.util.FuncionesUtiles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class NotificacionesActivity extends Activity {
    public static ListView ListaAlertas;
    public static AdapterListaNotificacion aList;
    public static Activity actNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        actNotify =  NotificacionesActivity.this;
        try{
            if (FuncionesUtiles.IsSession(NotificacionesActivity.this,null)){
                //FuncionesUtiles.ToastMensaje(NotificacionesActivity.this,FuncionesUtiles.paissession);

            }

            ListaAlertas = (ListView) findViewById(R.id.lstNoti);
            FuncionesUtiles.CargarListaAlerta(aList,ListaAlertas,NotificacionesActivity.this);
        }catch (Exception e){
            FuncionesUtiles.LogError(e.getMessage().toString(),getApplicationContext());
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

    @Override
    protected void onStart() {
        super.onStart();
        FuncionesUtiles.CargarListaAlerta(aList, ListaAlertas, NotificacionesActivity.this);
    }
}
