package com.alertmass.appalertmass.alertmass.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alertmass.appalertmass.alertmass.CanalesActivity;
import com.alertmass.appalertmass.alertmass.Data.Listas;
import com.alertmass.appalertmass.alertmass.R;
import com.alertmass.appalertmass.alertmass.SuscribirCanalActivity;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            //PushService.unsubscribe(vw.getContext(), "as"+idbtn.GetIdObj().toString());
            ParsePush.unsubscribeInBackground("as" + idbtn.GetIdObj().toString(), new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (FuncionesUtiles.LeerCanales(CanalesActivity.actCanal, FuncionesUtiles.paissession) != null) {
                        String strCanales = "[" + FuncionesUtiles.LeerCanales(CanalesActivity.actCanal, FuncionesUtiles.paissession) + "]";
                        ListView ListaCanales = (ListView) CanalesActivity.actCanal.findViewById(R.id.lstCanales);
                        try {
                            JSONArray subscribedChannels = new JSONArray(strCanales);
                            if (subscribedChannels.length() == 1) {
                                FuncionesUtiles.GuardarCanales("", vw.getContext(), FuncionesUtiles.paissession);
                            } else {
                                for (int x = 0; x < subscribedChannels.length(); x++) {
                                    JSONObject json = subscribedChannels.getJSONObject(x);
                                    if (json.getString("IdCanal").equals(idbtn.GetIdObj().toString())) {
                                        subscribedChannels = FuncionesUtiles.remove(x,subscribedChannels);
                                    }
                                }
                                String Desuscribir = subscribedChannels.toString().replace("[","").replace("]","");
                                FuncionesUtiles.GuardarCanales(Desuscribir, vw.getContext(), FuncionesUtiles.paissession);
                                FuncionesUtiles.EliminarArchivo(vw.getContext(), "Logo" + idbtn.GetIdObj().toString());
                            }
                            FuncionesUtiles.ToastMensaje(vw.getContext(), "Ya no estas suscrito al canal " + idbtn.GetTitle());
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        FuncionesUtiles.CargarListaCanales(CanalesActivity.lblMsjCanal);
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
            vw = inflater.inflate(R.layout.lista_canales, null);
        }
        item = items.get(pos);
        ImageView image = (ImageView) vw.findViewById(R.id.imgCanal);
        image.setTag(item);
        FuncionesUtiles.MostrarLogoCanal(vw.getContext(),item.GetIdObj().toString(),image);

        TextView title = (TextView) vw.findViewById(R.id.txtCanal);
        title.setText(item.GetTitle());

        TextView subtitle = (TextView) vw.findViewById(R.id.txtSubCanal);
        subtitle.setText("Pais: " + item.GetSubTitle());

        ImageView DelCanal = (ImageView) vw.findViewById(R.id.imgDelCanal);
        DelCanal.setTag(item);
        DelCanal.setOnClickListener(ClickDelCanal);
        return vw;
    }


}
