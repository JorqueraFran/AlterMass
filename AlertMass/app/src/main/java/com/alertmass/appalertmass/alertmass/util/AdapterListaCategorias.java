package com.alertmass.appalertmass.alertmass.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alertmass.appalertmass.alertmass.Data.Listas;
import com.alertmass.appalertmass.alertmass.R;

import java.util.ArrayList;

/**
 * Created by C-266 on 24/07/2015.
 */
public class AdapterListaCategorias extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Listas> items;

    public AdapterListaCategorias(Activity activity, ArrayList<Listas> items){
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
        View vw = view;
        if(view==null){
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vw = inflater.inflate(R.layout.lista_categorias, null);
        }
        Listas item = items.get(pos);
        //ImageView image = (ImageView) vw.findViewById(R.id.icon_row);

        TextView title = (TextView) vw.findViewById(R.id.txtCat);
        title.setText(item.GetTitle());

        TextView subtitle = (TextView) vw.findViewById(R.id.txtSubCat);
        subtitle.setText(item.GetSubTitle());

        TextView fecha = (TextView) vw.findViewById(R.id.txtFechaEnvCat);
        fecha.setText(item.GetSubFecha());
        return vw;
    }
}
