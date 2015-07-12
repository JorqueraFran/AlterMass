package com.example.proyecto.alertmass.Conexion;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Francisca on 11-07-2015.
 */
public class Descargar {
    public String urlDescarga;
    public HashMap<String, String> varsPost;
    public boolean isPost;
    public IDescarga callback;
    public byte[] resultado;
    public Exception errorDescarga;
    public int codigoRespuesta;
    public String msgServidor;

    public Descargar()
    {
        urlDescarga = "";
        varsPost = new HashMap<String, String>();
        isPost = true;

        callback = null;
        resultado = null;
        errorDescarga = null;
        codigoRespuesta = -1;
        msgServidor = "";
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

    public HttpURLConnection EntregarConexion() throws IOException
    {
        URL url;

        try
        {
            if (!isPost)
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

        if (isPost)
        {
            httpUrlCon.setRequestMethod("POST");
            httpUrlCon.setDoOutput(true);
        }
        else
        {
            httpUrlCon.setRequestMethod("GET");
        }

        httpUrlCon.setDoInput(true);

        return httpUrlCon;
    }
}
