package com.alertmass.appalertmass.alertmass.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alertmass.appalertmass.alertmass.CanalesActivity;
import com.alertmass.appalertmass.alertmass.Conexion.Descargador;
import com.alertmass.appalertmass.alertmass.Conexion.Descargar;
import com.alertmass.appalertmass.alertmass.Data.Listas;
import com.alertmass.appalertmass.alertmass.R;
import com.alertmass.appalertmass.alertmass.SuscribirCanalActivity;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.PushService;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by C-266 on 24/07/2015.
 */
public class AdapterListaCanalesSuscritos extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Listas> items;
    protected View vw;
    protected Boolean isCargadaImg=false;

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
        public void onClick(final View view) {
            final Listas idbtn = (Listas) view.getTag();
            final View btn=view;
            ParseInstallation.getCurrentInstallation().put(idbtn.GetTitle().replace(" ",""), "");
           // FuncionesUtiles.ToastMensaje(vw.getContext(), "Te has suscrito al canal " + idbtn.GetTitle());
            ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        DescargarImagenes(null, ((int) idbtn.GetId()), idbtn.GetIdObj(),idbtn.GetTitle(), true);
                        FuncionesUtiles.ToastMensaje(vw.getContext(), "Te has suscrito al canal " + idbtn.GetTitle());
                        String TextoArchivo = FuncionesUtiles.LeerCanales(vw.getContext());
                        if(TextoArchivo==null){
                            TextoArchivo="";
                        }
                        String SalidaCanalesAlert="";
                        if(TextoArchivo.isEmpty()){
                            SalidaCanalesAlert = "\""+idbtn.GetTitle()+"\"";
                        }else{
                            SalidaCanalesAlert = TextoArchivo + "," + "\""+idbtn.GetTitle()+"\"";
                        }

                        FuncionesUtiles.GuardarCanales(SalidaCanalesAlert,vw.getContext());
                        FuncionesUtiles.CargarListaCanales(CanalesActivity.lblMsjCanal);
                        btn.setVisibility(View.INVISIBLE);
                    } else {
                        e.printStackTrace();
                        FuncionesUtiles.ToastMensaje(vw.getContext(), "No se pudo suscribir al canal " + idbtn.GetTitle());

                    }
                }
            });

        }
    };


    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        vw = view;
        Listas item = items.get(pos);

        if(view==null){
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vw = inflater.inflate(R.layout.lista_suscanales, null);
            //holder = new ViewHolder();
            //holder.imgLogo = (ImageView) view.findViewById(R.id.imgSusCanal);
            //vw.setTag(holder);
        }/*else
        {
            holder = (ViewHolder) vw.getTag();
        }*/
        //holder.position = pos;
        ImageView image = (ImageView) vw.findViewById(R.id.imgSusCanal);
        image.setTag(item);
        DescargarImagenes(image, pos, item.GetIdObj(),"",false);
        TextView title = (TextView) vw.findViewById(R.id.txtSusCanal);
        title.setText(item.GetTitle());
       // DescargarImagenes(holder, pos,item.GetIdObj());
        TextView subtitle = (TextView) vw.findViewById(R.id.txtSubSusCanal);
        subtitle.setText(item.GetSubTitle());

        Button btnSuscribir = (Button) vw.findViewById(R.id.btnSuscribir);
        btnSuscribir.setTag(item);
        btnSuscribir.setOnClickListener(ClickSuscribir);

        String strCanales ="["+ FuncionesUtiles.LeerCanales(CanalesActivity.actCanal)+"]";
        ListView ListaCanales = (ListView) CanalesActivity.actCanal.findViewById(R.id.lstCanales);
        JSONArray subscribedChannels = null;
        try {
            subscribedChannels = new JSONArray(strCanales);
            if(subscribedChannels!=null) {
                for (int x = 0; x < subscribedChannels.length(); x++) {
                    if (item.GetTitle().equals(subscribedChannels.get(x).toString())) {
                        btnSuscribir.setVisibility(View.INVISIBLE);
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return vw;
    }

    private void DescargarImagenes(final ImageView image, final int pos, final String IdCanal, final String NomCanal, final boolean guardar){
        try {
            new AsyncTask<Void, Void, Bitmap>() {

               // final Listas idbtn = (Listas) image.getTag();
                @Override
                protected Bitmap doInBackground(Void... params) {
                    Bitmap imagen = Descargar.descargarImagen(vw.getResources().getString(R.string.SERVICIO_IMAGENES)+IdCanal);
                    return imagen;
                }

                @Override
                protected void onPostExecute(Bitmap result) {


                    if(!guardar)
                    {
                        Listas idimg = (Listas) image.getTag();
                        if (pos == idimg.GetId()) {
                            image.setImageBitmap(result);
                        }
                    }else{
                        FuncionesUtiles.guardarImagen(vw.getContext(),"Logo"+NomCanal,result);
                    }

                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
