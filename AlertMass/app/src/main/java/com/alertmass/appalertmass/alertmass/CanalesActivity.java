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

import java.util.ArrayList;
import java.util.List;


public class CanalesActivity extends Activity {

    private DataLogin datalogin;
    private static ListView ListaCanales;
    public static AdapterListaCanales aList;
    ImageView ibtnAddCanal;
    public static TextView lblCountCanal;
    public static Activity actCanal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canales);
        try{
            datalogin= DataLogin.EntregarDataLogin();
            ibtnAddCanal = (ImageView) findViewById(R.id.ibtnAddCanal);
            ibtnAddCanal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IrActAddGrupo();
                }
            });
            ListaCanales = (ListView) findViewById(R.id.lstCanales);
            lblCountCanal = (TextView) findViewById(R.id.lblCountCanal);
            actCanal =  CanalesActivity.this;
            FuncionesUtiles.CargarListaCanales();
        }catch (Exception e){
            FuncionesUtiles.LogError(e.getMessage().toString(),getApplicationContext());
        }
    }

    private void IrActAddGrupo(){
        Intent intent = new Intent(CanalesActivity.this, CategoriasCanalActivity.class);
        startActivity(intent);

    }

    public void CargarListaCanales(){
        ArrayList<Listas> items = new ArrayList<Listas>();
        List<String> subscribedChannels = ParseInstallation.getCurrentInstallation().getList("channels");
        //lblCountCanal.setText(subscribedChannels.size() + " Canales");
        for(int x = 0; x < subscribedChannels.size(); x++){
            items.add(new Listas(x,"",subscribedChannels.get(x).toString(),"" ,""));
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
