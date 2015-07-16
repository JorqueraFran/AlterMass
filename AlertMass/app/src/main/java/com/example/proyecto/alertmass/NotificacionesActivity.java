package com.example.proyecto.alertmass;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.proyecto.alertmass.Data.DataLogin;


public class NotificacionesActivity extends Activity {
    private DataLogin datalogin;
    private TextView Info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);
        //datalogin= DataLogin.EntregarDataLogin();
        //Info = (TextView) findViewById(R.id.Notifi);
        //Info.setText("INFORMACION EMAIL: " + datalogin.GetCorreoUser() + " NOMBRE: " + datalogin.GetNombreUser());
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK)
        {
            Intent main = new Intent(Intent.ACTION_MAIN);
            main.addCategory(Intent.CATEGORY_HOME);
            main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(main);
		/*finish();*/
            //System.runFinalizersOnExit(true);
            //System.exit(0);
            //overridePendingTransition(R.anim.right_in, R.anim.right_out);
        }
        return super.onKeyDown(keyCode, event);
    }
}
