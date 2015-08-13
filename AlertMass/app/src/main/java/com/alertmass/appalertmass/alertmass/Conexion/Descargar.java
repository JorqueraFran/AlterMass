package com.alertmass.appalertmass.alertmass.Conexion;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Francisca on 11-07-2015.
 */
public class Descargar {
    public String urlDescarga;
    public HashMap<String, String> varsPost;
    public String headersPWD;
    public String headersPais;
    public boolean isPost;
    public boolean isPut;
    public IDescarga callback;
    public byte[] resultado;
    public Exception errorDescarga;
    public int codigoRespuesta;
    public String msgServidor;
    public String UrlImagen;

    public Descargar()
    {
        urlDescarga = "";
        varsPost = new HashMap<String, String>();
        headersPWD= "";
        headersPais ="";
        isPost = true;
        isPut = false;

        callback = null;
        resultado = null;
        errorDescarga = null;
        codigoRespuesta = -1;
        msgServidor = "";
        UrlImagen="";
    }

    public String ObtenerStrURLVars()
    {
        String strVars = "";

        int cant = varsPost.size();
        int cont = 0;
        for (HashMap.Entry<String, String> entry : varsPost.entrySet())
        {
            cont++;

            strVars += entry.getKey() + "=" + entry.getValue();
            if (cont < cant)
            {
                strVars += "&";
            }
        }

        return strVars;
    }

    public String ObtenerStrURLVarsPUT()
    {
        String strVars = "";

        int cant = varsPost.size();
        int cont = 0;
        for (HashMap.Entry<String, String> entry : varsPost.entrySet())
        {
            cont++;

            strVars += "\""+entry.getKey()+ "\"" + ":" + "\"" + entry.getValue() + "\"";
            if (cont < cant)
            {
                strVars += ",";
            }
        }

        return "{" + strVars + "}";
    }

    public String ObtenerHeadersPWD()
    {
        return headersPWD;
    }
    public String ObtenerHeadersPais()
    {
        return headersPais;
    }

    public HttpURLConnection EntregarConexion() throws IOException
    {
        URL url;

        try
        {

            if (!isPost && !isPut)
            {
                String strVars = ObtenerStrURLVars();
                url = new URL(urlDescarga + "?" + strVars);
            }
            else
            {
                url = new URL(urlDescarga);
            }
        }
        catch (Exception ex)
        {
            throw ex;
        }

        HttpURLConnection httpUrlCon;

        try
        {
            httpUrlCon = (HttpURLConnection) url.openConnection();
        }
        catch (IOException e)
        {
            throw e;
        }
        if(!ObtenerHeadersPWD().isEmpty()){
            httpUrlCon.setRequestProperty("PWD", ObtenerHeadersPWD());
        }
        if(!ObtenerHeadersPais().isEmpty()){
            httpUrlCon.setRequestProperty("codpais", ObtenerHeadersPais());
        }
        if (isPost)
        {
            httpUrlCon.setRequestMethod("POST");
            httpUrlCon.setDoOutput(true);

        }
        else if(isPut){
            httpUrlCon.setRequestMethod("PUT");
            httpUrlCon.setDoOutput(true);
        }
        else
        {
            httpUrlCon.setRequestMethod("GET");
        }



        httpUrlCon.setDoInput(true);

        return httpUrlCon;
    }
    public static Bitmap descargarImagen (String imageHttpAddress){
        URL imageUrl = null;
        Bitmap imagen = null;
        try{
            imageUrl = new URL(imageHttpAddress);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            imagen = BitmapFactory.decodeStream(conn.getInputStream());
        }catch(IOException ex){
            ex.printStackTrace();
        }

        return imagen;
    }
}
