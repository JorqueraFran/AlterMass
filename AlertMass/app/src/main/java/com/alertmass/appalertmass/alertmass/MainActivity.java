package com.alertmass.appalertmass.alertmass;


import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.alertmass.appalertmass.alertmass.util.FuncionesUtiles;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends TabActivity {
    TabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        try{
            CapturaAlerta();
            tabHost = getTabHost();
            TabHost.TabSpec TabNotifi = tabHost.newTabSpec("Page 1");

            TabNotifi.setIndicator("Alertas");
            TabNotifi.setContent(new Intent(this, NotificacionesActivity.class));
            tabHost.addTab(TabNotifi);

            TabHost.TabSpec TabCanal = tabHost.newTabSpec("Page 2");
            TabCanal.setIndicator("Canales");
            TabCanal.setContent(new Intent(this, CanalesActivity.class));
            tabHost.addTab(TabCanal);

            TabHost.TabSpec TabGrupos = tabHost.newTabSpec("Page 3");
            TabGrupos.setIndicator("Grupos");
            TabGrupos.setContent(new Intent(this, GruposActivity.class));
            tabHost.addTab(TabGrupos);

            TabHost.TabSpec TabPerfil = tabHost.newTabSpec("Page 4");
            TabPerfil.setIndicator("Perfiles");
            TabPerfil.setContent(new Intent(this, PerfilesActivity.class));
            tabHost.addTab(TabPerfil);
        }catch (Exception e){
            FuncionesUtiles.LogError(e.getMessage().toString(),getApplicationContext());
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        CapturaAlerta();
    }

    private void CapturaAlerta(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String SalidaJsonAlert="";
        String TextoArchivo = LeerArchivo();
        if(extras != null){
            Date FechaActual = new Date();
            //Calendar FechaActual = Calendar.getInstance();

            String jsonData = extras.getString("com.parse.Data" );
            String jsonCanal = extras.getString("com.parse.Channel" );
            try
            {
                JSONObject jObject = new JSONObject(jsonData);
                JSONObject AlertDetJson = new JSONObject();

                AlertDetJson.put("Mensaje",jObject.getString("alert"));
                AlertDetJson.put("IdCanal",jObject.getString("idcanal"));
                AlertDetJson.put("NombreCanal",jObject.getString("nomcanal"));
                AlertDetJson.put("FechaEnvio",jObject.getString("fecenv"));
                AlertDetJson.put("HoraEnvio",jObject.getString("horenv"));

                if(TextoArchivo.isEmpty()){
                    SalidaJsonAlert = AlertDetJson.toString();
                }else{
                    SalidaJsonAlert = TextoArchivo + "," + AlertDetJson.toString();
                }

                OutputStreamWriter FileAlert=
                        new OutputStreamWriter(
                                openFileOutput("alerts.txt", Context.MODE_PRIVATE));

                FileAlert.write(SalidaJsonAlert);
                FileAlert.close();
                FuncionesUtiles.SetDataAlert("{\"Data\":["+SalidaJsonAlert+"]}");
            }
            catch (Exception ex)
            {
                Log.e("Ficheros", "Error al escribir fichero a memoria interna");
            }
        }else{
            FuncionesUtiles.SetDataAlert("{\"Data\":["+TextoArchivo+"]}");
        }
    }

    public String LeerArchivo(){
        String TextoArchivo="";
        try
        {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput("alerts.txt")));

            TextoArchivo = fin.readLine();
            fin.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
        }
        return TextoArchivo;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK)
        {
            Intent main = new Intent(Intent.ACTION_MAIN);
            main.addCategory(Intent.CATEGORY_HOME);
            main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(main);
            //finish();
            //System.exit(0);
            //overridePendingTransition(R.anim.right_in, R.anim.right_out);
        }
        return super.onKeyDown(keyCode, event);
    }
}
