package com.example.proyecto.alertmass.Conexion;

import android.os.AsyncTask;

import org.apache.http.util.ByteArrayBuffer;

import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by Francisca on 11-07-2015.
 */
public class Descargador extends AsyncTask<Descargar, Float, Descargar>
{
    private Descargar descarga;

    protected Descargar doInBackground(Descargar... des)
    {
        if (des.length <= 0)
        {
            return null;
        }

        descarga = des[0];

        HttpURLConnection httpCon;
        try
        {
            httpCon = descarga.EntregarConexion();
        }
        catch (Exception ex)
        {
            descarga.errorDescarga = ex;
            return descarga;
        }
        //Ahora se conecta con el servidor
        try
        {
            httpCon.connect();
        }
        catch (Exception ex)
        {
            descarga.errorDescarga = ex;
            return descarga;
        }

        //Si es post, se le envian los datos
        if (descarga.isPost)
        {
            try
            {
                httpCon.getOutputStream().write(descarga.ObtenerStrURLVars().getBytes());
            }
            catch (Exception ex)
            {
                descarga.errorDescarga = ex;
                return descarga;
            }
        }

        //Se checkea si viene el header de content length para el progreso de la descarga.
        int contentlength = -1;
        try
        {
            descarga.codigoRespuesta = httpCon.getResponseCode();
            if (descarga.codigoRespuesta != HttpURLConnection.HTTP_OK)
            {
                descarga.msgServidor = httpCon.getResponseMessage();
                descarga.errorDescarga = new Exception("Hubo un error al intentar descargar: (" + descarga.codigoRespuesta + ") " + descarga.msgServidor);
                return descarga;
            }
            else
            {
                contentlength = httpCon.getContentLength();
            }
        }
        catch (Exception ex)
        {
            descarga.errorDescarga = ex;
            return descarga;
        }

        //Finalmente, hay que descargar la respuesta.
        InputStream inputStream;
        try
        {
            inputStream = httpCon.getInputStream();
        }
        catch (Exception ex)
        {
            descarga.errorDescarga = ex;
            return descarga;
        }

        byte[] b = new byte[1024];
        try
        {
            ByteArrayBuffer mReadBuffer = new ByteArrayBuffer(0);
            while (inputStream.read(b) != -1)
            {
                mReadBuffer.append(b, 0, b.length);
                Float avance = 0f;
                if (contentlength > 0)
                {
                    avance = (Float) (mReadBuffer.length() / (float) contentlength);
                }

                publishProgress(avance);
            }

            byte[] input = mReadBuffer.buffer();
            int i = input.length;
            while (i-- > 0 && input[i] == 0)
            {

            }

            byte[] output = new byte[i + 1];
            System.arraycopy(input, 0, output, 0, i + 1);
            descarga.resultado = output;
        }
        catch (Exception ex)
        {
            descarga.errorDescarga = ex;
            return descarga;
        }

        httpCon.disconnect();

        return descarga;
    }

    protected void onProgressUpdate(Float... progress)
    {
        if (descarga.callback == null)
        {
            return;
        }

        descarga.callback.ProgresoDescarga(progress[0]);
    }

    protected void onPostExecute(Descargar resultado)
    {
        if (descarga.callback == null)
        {
            return;
        }

        if (descarga.errorDescarga != null)
        {
            descarga.callback.ErrorDescarga(descarga, descarga.codigoRespuesta, descarga.msgServidor);
        }
        else
        {
            descarga.callback.TerminoDescarga(descarga, descarga.resultado);
        }
    }
}

