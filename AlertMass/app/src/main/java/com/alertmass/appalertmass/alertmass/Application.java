package com.alertmass.appalertmass.alertmass;


import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;


import com.alertmass.appalertmass.alertmass.util.FuncionesUtiles;
import com.alertmass.appalertmass.alertmass.util.ParseUtil;
import com.parse.Parse;
import com.parse.PushService;


/**
 * Created by Francisca on 12-07-2015.
 */
public class Application extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        try{

            ParseUtil.registerParse(this);
        }catch (Exception e){

            FuncionesUtiles.LogError(e.getMessage().toString(), getApplicationContext());
        }

    }
}
