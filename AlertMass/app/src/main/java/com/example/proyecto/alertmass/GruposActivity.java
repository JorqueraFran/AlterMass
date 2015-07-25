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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.proyecto.alertmass.Data.DataLogin;
import com.example.proyecto.alertmass.Data.Listas;
import com.example.proyecto.alertmass.util.AdapterListaNotificacion;
import com.example.proyecto.alertmass.util.FuncionesUtiles;

import java.util.ArrayList;


public class GruposActivity extends Activity {

    private DataLogin datalogin;
    private ListView ListaGrupos;
    AdapterListaNotificacion aList;
    ImageView ibtnAddGrupo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupos);
        datalogin= DataLogin.EntregarDataLogin();

        ibtnAddGrupo = (ImageView) findViewById(R.id.ibtnAddGrupo);
        ibtnAddGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IrActAddGrupo();
            }
        });

        ListaGrupos = (ListView) findViewById(R.id.lstGrupos);
        CargarListaAlerta();
    }
    private void IrActAddGrupo(){
        Intent intent = new Intent(GruposActivity.this, CrearGrupoActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }
    private void CargarListaAlerta(){
        ArrayList<Listas> items = new ArrayList<Listas>();
        for(int x = 1; x <= 10; x++){
            items.add(new Listas(x,"Grupos " + x,"SubGrupos " + x));
        }
        aList  = new AdapterListaNotificacion(GruposActivity.this, items);
        ListaGrupos.setAdapter(aList);
        ListaGrupos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Listas itemActual = (Listas)aList.getItem(pos);
                //FuncionesUtiles.ToastMensaje(getApplicationContext(), "Posicion " + pos);
                Intent intent = new Intent(GruposActivity.this, GrupoNotificacionActivity.class);
                intent.putExtra("titulo", itemActual.GetTitle());
                startActivity(intent);
                //overridePendingTransition(R.anim.left_in, R.anim.left_out);
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
