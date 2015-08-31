package com.alertmass.appalertmass.alertmass;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;

import com.alertmass.appalertmass.alertmass.Data.Listas;
import com.alertmass.appalertmass.alertmass.util.AdapterListaCanalesSuscritos;
import com.alertmass.appalertmass.alertmass.util.FuncionesUtiles;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;


public class MainActivity extends TabActivity {
    TabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        try{
            if (FuncionesUtiles.IsSession(MainActivity.this,null)){
                FuncionesUtiles.CapturaAlerta(this,getIntent(),FuncionesUtiles.paissession);
            }else{
                finish();
                Intent intent = new Intent(MainActivity.this, InicioActivity.class);
                startActivity(intent);
            }
            //FuncionesUtiles.ToastMensaje(this, "Se Creo MainActivity");

            tabHost = getTabHost();
            TabHost.TabSpec TabNotifi = tabHost.newTabSpec("Page 1");

            TabNotifi.setIndicator("", getResources().getDrawable(R.drawable.notifications));
            TabNotifi.setContent(new Intent(this, NotificacionesActivity.class));
            tabHost.addTab(TabNotifi);

            TabHost.TabSpec TabCanal = tabHost.newTabSpec("Page 2");
            TabCanal.setIndicator("", getResources().getDrawable(R.drawable.channels));
            TabCanal.setContent(new Intent(this, CanalesActivity.class));
            tabHost.addTab(TabCanal);

            TabHost.TabSpec TabGrupos = tabHost.newTabSpec("Page 3");
            TabGrupos.setIndicator("", getResources().getDrawable(R.drawable.groups));
            TabGrupos.setContent(new Intent(this, GruposActivity.class));
            tabHost.addTab(TabGrupos);

        }catch (Exception e){
            FuncionesUtiles.LogError(e.getMessage().toString(),getApplicationContext());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        //FuncionesUtiles.ToastMensaje(this, "Se lanzo MainActivity");
        //if (FuncionesUtiles.IsSession(MainActivity.this,null)){

        //}
       // FuncionesUtiles.CapturaAlerta(this, getIntent(), FuncionesUtiles.usersession);
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
                ContextWrapper cw = new ContextWrapper(this);
                File dirImages = cw.getDir("Files", Context.MODE_PRIVATE);
                File myPath = new File(dirImages, "alerts.txt");

                FileOutputStream fos = new FileOutputStream(myPath);
                OutputStreamWriter FileAlert=
                        new OutputStreamWriter(fos);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                CambiarPais();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void CambiarPais(){
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Aviso");
        alert.setMessage("Esta seguro que desea cambiar de pais?");
        alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                new AsyncTask<Void, Void, Boolean>() {
                    ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
                    @Override
                    protected Boolean doInBackground(Void... params) {
                        FuncionesUtiles.DeleteSession();
                        return true;
                    }
                    @Override
                    protected void onPreExecute() {
                        pDialog.setMessage("Cerrando...");
                        pDialog.setIndeterminate(false);
                        pDialog.setCancelable(false);
                        pDialog.show();
                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        if(aBoolean){
                            pDialog.dismiss();
                            Intent intent = new Intent(MainActivity.this, InicioActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.right_in, R.anim.right_out);
                        }


                    }
                }.execute();

            }
        });

        // Setting Negative "NO" Button
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();

    }
}
