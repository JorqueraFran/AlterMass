package com.alertmass.appalertmass.alertmass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alertmass.appalertmass.alertmass.util.AdapterListaCanales;
import com.alertmass.appalertmass.alertmass.util.FuncionesUtiles;


public class CanalesActivity extends Activity {
    private static ListView ListaCanales;
    public static AdapterListaCanales aList;
    ImageView ibtnAddCanal;
    public static TextView lblMsjCanal;
    public static Activity actCanal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canales);
        lblMsjCanal = (TextView) findViewById(R.id.lblMensajeCanales);
        ibtnAddCanal = (ImageView) findViewById(R.id.ibtnAddCanal);
        ListaCanales = (ListView) findViewById(R.id.lstCanales);

        actCanal =  CanalesActivity.this;
        if(FuncionesUtiles.verificaConexion(getApplicationContext())){

            try{
                if (FuncionesUtiles.IsSession(CanalesActivity.this,null)){

                }

                ibtnAddCanal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IrActAddGrupo();
                    }
                });


                FuncionesUtiles.CargarListaCanales(lblMsjCanal);
            }catch (Exception e){
                FuncionesUtiles.LogError(e.getMessage().toString(),getApplicationContext());
            }
        }else{
            FuncionesUtiles.CargarListaCanales(lblMsjCanal);
            FuncionesUtiles.AvisoSinConexion(CanalesActivity.this);
        }
    }
    private void IrActAddGrupo(){
        Intent intent = new Intent(CanalesActivity.this, CategoriasCanalActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FuncionesUtiles.CargarListaCanales(lblMsjCanal);
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
