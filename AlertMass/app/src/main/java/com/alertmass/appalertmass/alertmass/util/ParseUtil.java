package com.alertmass.appalertmass.alertmass.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alertmass.appalertmass.alertmass.MainActivity;
import com.parse.Parse;
import com.parse.ParseBroadcastReceiver;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.PushService;
import com.parse.SaveCallback;

/**
 * Created by Francisca on 10-08-2015.
 */
public class ParseUtil extends ParseBroadcastReceiver {

    public static void subscribeWithPais(String CodPais) {
        ParseInstallation.getCurrentInstallation().put("IdPais", CodPais);
        ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.e("alertmass","Pais Agregado");
                } else {
                    Log.e("alertmass", "Pais No Agregado");
                }
            }
        });
    }

    /*@Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
        if(intent!=null){
            FuncionesUtiles.CapturaAlerta(context,intent,FuncionesUtiles.paissession);
        }
    }*/
    public static void registerParse(Context context) {
        // initializing parse library
        Parse.initialize(context, AppConfig.PARSE_APPLICATION_ID, AppConfig.PARSE_CLIENT_KEY);
        PushService.setDefaultPushCallback(context, MainActivity.class);
        //ParseInstallation.getCurrentInstallation().saveInBackground();

        /*ParsePush.subscribeInBackground(AppConfig.PARSE_CHANNEL, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.e("AlertMass", "Successfully subscribed to Parse!");
            }
        });*/

        //Cuenta Test Francisca
        //Parse.initialize(this, "7c6N33iXsEEwIQireu2QVJh9pZS2ZmTqMG5gYXzW", "nP01sUz0FMpB9N7OxlxtfkMIZ9JsrJy5m2oUG7ma");

        //Cuenta App AlertMass
        //Parse.initialize(this, "1FPKiWd4niGvmuFEistvBzy8QdEKRkAapW9E1ZNy", "6HwEgL6uMh9zSGPndGdU3atLufwYwSNHwrpRQBiT");

    }
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent!=null){
            FuncionesUtiles.CapturaAlerta(context,intent,FuncionesUtiles.paissession);
        }
    }
}
