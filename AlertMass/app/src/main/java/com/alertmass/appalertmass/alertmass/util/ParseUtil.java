package com.alertmass.appalertmass.alertmass.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.alertmass.appalertmass.alertmass.MainActivity;
import com.parse.Parse;
import com.parse.ParseBroadcastReceiver;
import com.parse.PushService;

/**
 * Created by Francisca on 10-08-2015.
 */
public class ParseUtil extends ParseBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent!=null){
            FuncionesUtiles.CapturaAlerta(context,intent);
            FuncionesUtiles.ToastMensaje(context,"Llego un mensaje madafaka");
        }
    }


}
