package com.alertmass.appalertmass.alertmass.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alertmass.appalertmass.alertmass.Data.Listas;
import com.alertmass.appalertmass.alertmass.R;
import com.alertmass.appalertmass.alertmass.SuscribirCanalActivity;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.PushService;
import com.parse.SaveCallback;

import java.util.ArrayList;

/**
 * Created by C-266 on 24/07/2015.
 */
public class AdapterListaCanales extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Listas> items;
    protected Listas item;
    protected View vw;

    public AdapterListaCanales(Activity activity, ArrayList<Listas> items){
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

    public View.OnClickListener ClickDelCanal = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Listas idbtn = (Listas) view.getTag();
            //FuncionesUtiles.ToastMensaje(vw.getContext(),"Suscripcion Eliminada");
            ParseInstallation.getCurrentInstallation().remove(idbtn.GetTitle());
            ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        FuncionesUtiles.ToastMensaje(vw.getContext(), "Ya no estas suscrito al canal " + idbtn.GetTitle());
                        PushService.unsubscribe(vw.getContext(), idbtn.GetTitle());
                        FuncionesUtiles.CargarListaCanales();
                    } else {
                        e.printStackTrace();
                        FuncionesUtiles.ToastMensaje(vw.getContext(), "No se puso cancelar la suscripcion al canal " + idbtn.GetTitle());

                    }
                }
            });
            //PushService.unsubscribe(vw.getContext(), idbtn.GetTitle());

        }
    };

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        vw = view;
        if(view==null){
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vw = inflater.inflate(R.layout.lista_canales, null);
        }
        item = items.get(pos);
        //ImageView image = (ImageView) vw.findViewById(R.id.icon_row);

        TextView title = (TextView) vw.findViewById(R.id.txtCanal);
        title.setText(item.GetTitle());

        TextView subtitle = (TextView) vw.findViewById(R.id.txtSubCanal);
        subtitle.setText(item.GetSubTitle());

        ImageView DelCanal = (ImageView) vw.findViewById(R.id.imgDelCanal);
        DelCanal.setTag(item);
        DelCanal.setOnClickListener(ClickDelCanal);
        return vw;
    }
}
