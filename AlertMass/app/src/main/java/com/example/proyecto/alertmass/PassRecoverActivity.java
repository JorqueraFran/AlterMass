package com.example.proyecto.alertmass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto.alertmass.Conexion.Descargador;
import com.example.proyecto.alertmass.Conexion.Descargar;
import com.example.proyecto.alertmass.Conexion.IDescarga;
import com.example.proyecto.alertmass.util.FuncionesUtiles;

import org.json.JSONObject;


public class PassRecoverActivity extends Activity implements IDescarga {

    private AutoCompleteTextView EmailRecover;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passrecover);
        EmailRecover = (AutoCompleteTextView) findViewById(R.id.txt_EmailRecover);
        EmailRecover.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_GO){
                    if (!FuncionesUtiles.isEmailValid(EmailRecover.getText().toString())){
                        EmailRecover.setError(getString(R.string.error_invalid_email));
                        return false;
                    }else{
                        RecuperarPass();
                        return true;
                    }
                }
                return false;
            }
        });

    }
    private void RecuperarPass(){
        new AsyncTask<Void, Void, Boolean>() {
            ProgressDialog pDialog = new ProgressDialog(PassRecoverActivity.this);
            String Correo =  EmailRecover.getText().toString();
            @Override
            protected Boolean doInBackground(Void... params) {
                FuncionesUtiles.OcultarTeclado(PassRecoverActivity.this);
                Descargador descargador = new Descargador();
                Descargar descarga = new Descargar();
                descarga.urlDescarga = getResources().getString(R.string.SERVICIO_RECUPERAR_PWD)+Correo;
                descarga.isPost = false;
                descarga.callback = PassRecoverActivity.this;
                descargador.execute(descarga);
                return true;
            }

            @Override
            protected void onPreExecute() {
                FuncionesUtiles.ProgressDialog(pDialog, "Enviando Password...");
            }

            @Override
            protected void onPostExecute(final Boolean success) {
                FuncionesUtiles.CancelarDialog(pDialog);

            }

            @Override
            protected void onCancelled() {
                FuncionesUtiles.CancelarDialog(pDialog);

            }
        }.execute();

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK)
        {
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
        }else if(keyCode == event.KEYCODE_INSERT){
            FuncionesUtiles.ToastMensaje(this,"ENTER");
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void TerminoDescarga(Descargar descarga, byte[] data) {
        try {
            String strJSON = new String(data, "UTF-8");
            JSONObject jObject = new JSONObject(strJSON);
            if(jObject.getString("st").equals("OK")){
                FuncionesUtiles.ToastMensaje(this, "E-Mail enviado correctamente!");
                finish();
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
            }else{
                FuncionesUtiles.ToastMensaje(this, "Hubo problemas para recuperar su password");
            }

        } catch (Exception ex)
            {
                FuncionesUtiles.ToastMensaje(this, "Hubo problemas para recuperar su password");
                return;
            }
    }

    @Override
    public void ErrorDescarga(Descargar descarga, int codigoError, String descripcion) {
        FuncionesUtiles.ToastMensaje(this, "Hubo problemas para recuperar su password");
    }

    @Override
    public void ProgresoDescarga(Float porcentaje) {

    }
}
