package com.alertmass.appalertmass.alertmass.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alertmass.appalertmass.alertmass.CanalesActivity;
import com.alertmass.appalertmass.alertmass.Data.Listas;
import com.alertmass.appalertmass.alertmass.R;
import com.alertmass.appalertmass.alertmass.SuscribirCanalActivity;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.PushService;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by C-266 on 24/07/2015.
 */
public class AdapterListaCanalesSuscritos extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Listas> items;
    protected View vw;

    public AdapterListaCanalesSuscritos(Activity activity, ArrayList<Listas> items){
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).GetId();
    }

    public View.OnClickListener ClickSuscribir = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Listas idbtn = (Listas) view.getTag();

            ParseInstallation.getCurrentInstallation().put(idbtn.GetTitle(), "");
           // FuncionesUtiles.ToastMensaje(vw.getContext(), "Te has suscrito al canal " + idbtn.GetTitle());
            ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        FuncionesUtiles.ToastMensaje(vw.getContext(), "Te has suscrito al canal " + idbtn.GetTitle());
                        PushService.subscribe(vw.getContext(), idbtn.GetTitle(), SuscribirCanalActivity.class);
                        FuncionesUtiles.CargarListaCanales();
                    } else {
                        e.printStackTrace();
                        FuncionesUtiles.ToastMensaje(vw.getContext(), "No se puso suscribir al canal " + idbtn.GetTitle());

                    }
                }
            });

        }
    };

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        vw = view;
        if(view==null){
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vw = inflater.inflate(R.layout.lista_suscanales, null);
        }
        Listas item = items.get(pos);
        //ImageView image = (ImageView) vw.findViewById(R.id.icon_row);

        TextView title = (TextView) vw.findViewById(R.id.txtSusCanal);
        title.setText(item.GetTitle());

        TextView subtitle = (TextView) vw.findViewById(R.id.txtSubSusCanal);
        subtitle.setText(item.GetSubTitle());

        Button btnSuscribir = (Button) vw.findViewById(R.id.btnSuscribir);
        btnSuscribir.setTag(item);
        btnSuscribir.setOnClickListener(ClickSuscribir);

        List<String> subscribedChannels = ParseInstallation.getCurrentInstallation().getList("channels");
        for(int x = 0; x < subscribedChannels.size(); x++){
            if(item.GetTitle().equals(subscribedChannels.get(x).toString()))
            {
                btnSuscribir.setVisibility(View.INVISIBLE);
            }

        }


        return vw;
    }
}
