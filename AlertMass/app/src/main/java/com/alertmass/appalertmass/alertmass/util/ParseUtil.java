package com.alertmass.appalertmass.alertmass.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.ListView;

import com.alertmass.appalertmass.alertmass.CanalesActivity;
import com.alertmass.appalertmass.alertmass.Data.Listas;
import com.alertmass.appalertmass.alertmass.InicioActivity;
import com.alertmass.appalertmass.alertmass.MainActivity;
import com.alertmass.appalertmass.alertmass.NotificacionesActivity;
import com.alertmass.appalertmass.alertmass.R;
import com.parse.Parse;
import com.parse.ParseBroadcastReceiver;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.PushService;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Francisca on 10-08-2015.
 */
public class ParseUtil extends ParsePushBroadcastReceiver {

    public static final String LOGTAG = "ParseUtil";
    public static final String PARSE_DATA_KEY = "com.parse.Data";
    public static AdapterListaNotificacion aList;
    private static JSONObject MSG_COUNTS = new JSONObject();

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

    public static void registerParse(Context context) {

        // initializing parse library
        Parse.initialize(context, AppConfig.PARSE_APPLICATION_ID, AppConfig.PARSE_CLIENT_KEY);
        ParseInstallation.getCurrentInstallation().saveInBackground();


    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
        FuncionesUtiles.CapturaAlerta(context, intent, FuncionesUtiles.paissession);
        if(NotificacionesActivity.actNotify!=null){
            ListView ListaAlertas = (ListView) NotificacionesActivity.actNotify.findViewById(R.id.lstNoti);
            FuncionesUtiles.CargarListaAlerta(aList, ListaAlertas, NotificacionesActivity.actNotify);

        }

    }
    private JSONObject getDataFromIntent(Intent intent) {
        JSONObject data = null;
        try {
            data = new JSONObject(intent.getExtras().getString(PARSE_DATA_KEY));
        } catch (JSONException e) {
            // Json was not readable...
        }
        return data;
    }

}
