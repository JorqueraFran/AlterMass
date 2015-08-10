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


import com.alertmass.appalertmass.alertmass.Data.DataLogin;
import com.alertmass.appalertmass.alertmass.Data.Listas;
import com.alertmass.appalertmass.alertmass.util.AdapterListaCanales;
import com.alertmass.appalertmass.alertmass.util.FuncionesUtiles;
import com.parse.ParseInstallation;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class CanalesActivity extends Activity {

    private DataLogin datalogin;
    private static ListView ListaCanales;
    public static AdapterListaCanales aList;
    ImageView ibtnAddCanal;
    public static TextView lblMsjCanal;
    public static Activity actCanal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canales);
        if(FuncionesUtiles.verificaConexion(getApplicationContext())){
            try{
                if (FuncionesUtiles.IsSession(CanalesActivity.this,null)){
                    if(datalogin==null){
                        datalogin= DataLogin.EntregarDataLogin();

                    }
                }
                ibtnAddCanal = (ImageView) findViewById(R.id.ibtnAddCanal);
                ibtnAddCanal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IrActAddGrupo();
                    }
                });
                ListaCanales = (ListView) findViewById(R.id.lstCanales);
                lblMsjCanal = (TextView) findViewById(R.id.lblMensajeCanales);
                actCanal =  CanalesActivity.this;

                FuncionesUtiles.CargarListaCanales(lblMsjCanal);
            }catch (Exception e){
                FuncionesUtiles.LogError(e.getMessage().toString(),getApplicationContext());
            }
        }else{
            FuncionesUtiles.AvisoSinConexion(CanalesActivity.this);
        }
    }
    private void IrActAddGrupo(){
        Intent intent = new Intent(CanalesActivity.this, CategoriasCanalActivity.class);
        startActivity(intent);

    }

    public void CargarListaCanales(){
        try {
            ArrayList<Listas> items = new ArrayList<Listas>();
            String strCanales = FuncionesUtiles.LeerCanales(CanalesActivity.this);
            JSONArray AlertArray = new JSONArray(strCanales);
        } catch (JSONException e) {
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
