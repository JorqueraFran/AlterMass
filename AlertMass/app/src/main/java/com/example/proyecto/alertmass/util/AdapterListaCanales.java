package com.example.proyecto.alertmass.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyecto.alertmass.Data.Listas;
import com.example.proyecto.alertmass.R;

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
        DelCanal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FuncionesUtiles.ToastMensaje(vw.getContext(),"Eliminar Canal " + item.GetTitle() );
            }
        });
        return vw;
    }
}
