package com.example.proyecto.alertmass;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.proyecto.alertmass.util.FuncionesUtiles;


public class DetalleNotificacionActivity extends Activity {

    Button btnReenviarNoti;
    TextView lblTitulo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_notificacion);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        Bundle ext = getIntent().getExtras();

        lblTitulo = (TextView) findViewById(R.id.txtTituloDetNoti);
        lblTitulo.setText(ext.getString("titulo"));

        btnReenviarNoti = (Button) findViewById(R.id.btnReenviarNoti);
        btnReenviarNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reenviar();
            }
        });

    }

    private void Reenviar(){
        FuncionesUtiles.ToastMensaje(getApplicationContext(),"Notificacion Re-Enviada");
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK)
        {
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
        }
        return super.onKeyDown(keyCode, event);
    }
}
