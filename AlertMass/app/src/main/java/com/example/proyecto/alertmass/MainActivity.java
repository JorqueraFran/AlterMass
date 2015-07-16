package com.example.proyecto.alertmass;


import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;


public class MainActivity extends TabActivity {
    TabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        TabGrupos.setContent(new Intent(this,GruposActivity.class));
        tabHost.addTab(TabGrupos);

        TabHost.TabSpec TabPerfil = tabHost.newTabSpec("Page 4");
        TabPerfil.setIndicator("Perfiles");
        TabPerfil.setContent(new Intent(this,PerfilesActivity.class));
        tabHost.addTab(TabPerfil);

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
