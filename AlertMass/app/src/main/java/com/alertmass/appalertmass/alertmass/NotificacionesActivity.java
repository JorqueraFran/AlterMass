package com.alertmass.appalertmass.alertmass;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alertmass.appalertmass.alertmass.Data.DataLogin;
import com.alertmass.appalertmass.alertmass.Data.Listas;
import com.alertmass.appalertmass.alertmass.util.AdapterListaNotificacion;
import com.alertmass.appalertmass.alertmass.util.FuncionesUtiles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class NotificacionesActivity extends Activity {
    private DataLogin datalogin;
    private ListView ListaAlertas;
    AdapterListaNotificacion aList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        try{
            datalogin= DataLogin.EntregarDataLogin();

            ListaAlertas = (ListView) findViewById(R.id.lstNoti);
            CargarListaAlerta();
        }catch (Exception e){
            FuncionesUtiles.LogError(e.getMessage().toString(),getApplicationContext());
        }
    }

    private void CargarListaAlerta(){
        String strAlertas = FuncionesUtiles.GetDataAlert();
        if(!strAlertas.isEmpty()){
            JSONArray AlertArray = null;
            try {
                JSONObject jObject = new JSONObject(strAlertas);
                AlertArray = jObject.getJSONArray("Data");
                Log.d("JSON-ALERT",jObject.toString());

                ArrayList<Listas> items = new ArrayList<Listas>();
                for(int x = 0; x <= AlertArray.length(); x++){
                    try {
                        JSONObject json = AlertArray.getJSONObject(x);
                        items.add(new Listas(x,"",json.getString("NombreCanal"),json.getString("Mensaje"),json.getString("FechaEnvio")+" "+json.getString("HoraEnvio")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                aList  = new AdapterListaNotificacion(NotificacionesActivity.this, items);
                ListaAlertas.setAdapter(aList);
                /*ListaAlertas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                        Listas itemActual = (Listas)aList.getItem(pos);
                        //FuncionesUtiles.ToastMensaje(getApplicationContext(), "Posicion " + pos);
                        Intent intent = new Intent(NotificacionesActivity.this, DetalleNotificacionActivity.class);
                        intent.putExtra("titulo", itemActual.GetTitle());
                        startActivity(intent);

                    }
                });*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
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