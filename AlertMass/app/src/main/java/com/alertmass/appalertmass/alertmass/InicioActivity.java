package com.alertmass.appalertmass.alertmass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.alertmass.appalertmass.alertmass.util.FuncionesUtiles;
import com.alertmass.appalertmass.alertmass.util.ParseUtil;


public class InicioActivity extends Activity {

    private Spinner spinnerPaises;
    private Button btnEntrar;
    private TextView lblMensajeIni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        spinnerPaises = (Spinner) findViewById(R.id.spinner_paises);
        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        lblMensajeIni = (TextView) findViewById(R.id.lblMensajeIni);
        if (FuncionesUtiles.IsSession(InicioActivity.this,null)){
            finish();
            Intent intent = new Intent(InicioActivity.this, MainActivity.class);
            startActivity(intent);
        }
        if(FuncionesUtiles.verificaConexion(this)) {
            btnEntrar.setVisibility(View.VISIBLE);
            lblMensajeIni.setText("");
            FuncionesUtiles.CargarSpinnerPaises(InicioActivity.this, spinnerPaises);
        }else{
            lblMensajeIni.setText("Verifique su conexion a internet, vuelva mas tarde");
            btnEntrar.setVisibility(View.INVISIBLE);

        }
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new AsyncTask<Void, Void, Boolean>() {
                        ProgressDialog pDialog = new ProgressDialog(InicioActivity.this);
                        @Override
                        protected Boolean doInBackground(Void... params) {
                            String pais = FuncionesUtiles.SelPaisActual(spinnerPaises.getSelectedItem().toString());
                            if (!FuncionesUtiles.IsSession(InicioActivity.this,null)){
                                ParseUtil.subscribeWithPais(pais);
                                FuncionesUtiles.SetSession(pais,spinnerPaises.getSelectedItem().toString());

                            }

                            return true;
                        }
                        @Override
                        protected void onPreExecute() {
                        pDialog.setMessage("Ingresando...");
                        pDialog.setIndeterminate(false);
                        pDialog.setCancelable(false);
                        pDialog.show();
                        }

                        @Override
                        protected void onPostExecute(Boolean aBoolean) {

                            if(aBoolean){
                                Intent intent = new Intent(InicioActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                            }else{

                            }
                            pDialog.dismiss();

                        }
                    }.execute();
                //FuncionesUtiles.ToastMensaje(InicioActivity.this,"Id " + FuncionesUtiles.SelPaisActual(spinnerPaises.getSelectedItem().toString()));
                }
        });
    }
}
