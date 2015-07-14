package com.example.proyecto.alertmass;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Base64;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;

import java.security.MessageDigest;
import java.security.Signature;

/**
 * Created by Francisca on 12-07-2015.
 */
public class Application extends android.app.Application {
    public Application(){}

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the Parse SDK.
        Parse.initialize(this, "w8RyWfkFEO2O08Qhd7lDoWIZsi0i9KZJF7wMg2Z5", "Fn96QocIzlhZq5bDyWT7HW5np0eBy5V9qW6GdnAj");

        // Specify an Activity to handle all pushes by default.
        //PushService.setDefaultPushCallback(this, LoginActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
    }
}
