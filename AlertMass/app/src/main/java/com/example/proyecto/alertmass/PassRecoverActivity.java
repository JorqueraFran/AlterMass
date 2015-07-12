package com.example.proyecto.alertmass;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;

import com.example.proyecto.alertmass.util.FuncionesUtiles;


public class PassRecoverActivity extends Activity {

    private AutoCompleteTextView EmailRecover;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passrecover);
        EmailRecover = (AutoCompleteTextView) findViewById(R.id.txt_EmailRecover);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK)
        {
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
        }else if(keyCode == event.KEYCODE_INSERT){
            FuncionesUtiles.ToastMensaje(this,"ENTER");
        }
        return super.onKeyDown(keyCode, event);
    }
}
