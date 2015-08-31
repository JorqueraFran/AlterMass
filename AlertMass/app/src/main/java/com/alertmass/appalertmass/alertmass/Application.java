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
            // Initialize the Parse SDK.
            //Cuenta Test Francisca
            //Parse.initialize(this, "7c6N33iXsEEwIQireu2QVJh9pZS2ZmTqMG5gYXzW", "nP01sUz0FMpB9N7OxlxtfkMIZ9JsrJy5m2oUG7ma");

            //Cuenta App AlertMass
            //Parse.initialize(this, "1FPKiWd4niGvmuFEistvBzy8QdEKRkAapW9E1ZNy", "6HwEgL6uMh9zSGPndGdU3atLufwYwSNHwrpRQBiT");
            //PushService.setDefaultPushCallback(this, MainActivity.class);
            ParseUtil.registerParse(this);
        }catch (Exception e){

            FuncionesUtiles.LogError(e.getMessage().toString(), getApplicationContext());
        }

    }
}
