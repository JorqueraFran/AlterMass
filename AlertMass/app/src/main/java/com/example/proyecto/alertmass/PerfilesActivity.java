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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.proyecto.alertmass.Conexion.Descargador;
import com.example.proyecto.alertmass.Conexion.Descargar;
import com.example.proyecto.alertmass.Conexion.IDescarga;
import com.example.proyecto.alertmass.Data.DataLogin;
import com.example.proyecto.alertmass.util.FuncionesUtiles;


public class PerfilesActivity extends Activity implements IDescarga {

    private EditText NombrePerfil;
    private EditText CorreoPerfil;
    private EditText PassPerfil;
    private Button CambioPassPerfil;
    private Button GuardarPassPerfil;
    private TextView InfoPerfil;
    private Button CerrarSesionPerfil;
    private EditText PassConfirmPerfil;
    private TextView LblPassConfirmPerfil;
    private DataLogin datalogin;
    public String PassOld;
    public String PassNew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfiles);

        datalogin= DataLogin.EntregarDataLogin();

        NombrePerfil = (EditText) findViewById(R.id.txtNombrePerfil);
        CorreoPerfil = (EditText) findViewById(R.id.txtCorreoPerfil);
        PassPerfil = (EditText) findViewById(R.id.txtPassPerfil);
        CambioPassPerfil = (Button) findViewById(R.id.btnCambiaPassPerfil);
        InfoPerfil = (TextView) findViewById(R.id.lblInfoPerfil);
        CerrarSesionPerfil = (Button) findViewById(R.id.btnCerrarSesion);
        PassConfirmPerfil = (EditText) findViewById(R.id.txtPassConfirm);
        LblPassConfirmPerfil = (TextView) findViewById(R.id.lblPassConfirm);
        GuardarPassPerfil =(Button) findViewById(R.id.btnGuardarPass);

        NombrePerfil.setText(datalogin.GetNombreUser());
        CorreoPerfil.setText(datalogin.GetCorreoUser());
        PassPerfil.setText(datalogin.GetPassUser());

        CambioPassPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnclickCambioPassPerfil();
            }
        });

        CerrarSesionPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnclickCerrarSesionPerfil();
            }
        });

    }
    private void OnclickCerrarSesionPerfil(){
        deleteDatabase("AlertMass");

        try
        {
            Intent intent = new Intent(PerfilesActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        catch (Exception e)
        {

        }
    }

    private void OnclickCambioPassPerfil() {

        PassPerfil.setHint("Nuevo Password");
        PassPerfil.setText("");
        PassConfirmPerfil.setVisibility(View.VISIBLE);
        LblPassConfirmPerfil.setVisibility(View.VISIBLE);
        CambioPassPerfil.setVisibility(View.GONE);
        GuardarPassPerfil.setVisibility(View.VISIBLE);
        PassOld = PassPerfil.getText().toString();
        GuardarPassPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean IsErrores = false;
                if (PassPerfil.getText().equals("")) {
                    PassPerfil.setError("Requerido");
                    IsErrores = true;
                }
                if(PassConfirmPerfil.getText().equals("")){
                    PassConfirmPerfil.setError("Requerido");
                    IsErrores = true;
                }
                if(!PassPerfil.getText().toString().equals(PassConfirmPerfil.getText().toString())){
                    PassConfirmPerfil.setError("Password No Coincide");
                    PassConfirmPerfil.setText("");
                    IsErrores = true;
                }
                if(PassOld.equals(PassConfirmPerfil.getText())){
                    InfoPerfil.setText("El nuevo password debe ser distinto al antiguo.");
                    IsErrores = true;
                }
                if (!IsErrores){
                    CambioPassword();
                }
            }
        });
    }

    private void CambioPassword(){
        PassNew=PassPerfil.getText().toString();
        new AsyncTask<Void, Void, Boolean>() {
            ProgressDialog pDialog = new ProgressDialog(PerfilesActivity.this);
            String PerfilCorreo =  CorreoPerfil.getText().toString();
            String ServicioLogin = getResources().getString(R.string.SERVICIOCAMBIOPWD) + PerfilCorreo;
            @Override
            protected Boolean doInBackground(Void... params) {

                Descargador descargador = new Descargador();
                Descargar descarga = new Descargar();
                descarga.urlDescarga = ServicioLogin;
                descarga.isPost = true;
                descarga.varsPost.put("oldpass", PassOld);
                descarga.varsPost.put("newpass", PassNew);
                descarga.callback = PerfilesActivity.this;
                descargador.execute(descarga);
                return true;
            }

            @Override
            protected void onPreExecute() {
                FuncionesUtiles.ProgressDialog(pDialog, "Guardando Password...");
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
            Intent main = new Intent(Intent.ACTION_MAIN);
            main.addCategory(Intent.CATEGORY_HOME);
            main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(main);
		/*finish();*/
            //System.runFinalizersOnExit(true);
            //System.exit(0);
            //overridePendingTransition(R.anim.right_in, R.anim.right_out);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void TerminoDescarga(Descargar descarga, byte[] data) {
            PassPerfil.setHint("Password Perfil");
            PassPerfil.setText(PassNew);
            PassConfirmPerfil.setVisibility(View.GONE);
            LblPassConfirmPerfil.setVisibility(View.GONE);
            CambioPassPerfil.setVisibility(View.VISIBLE);
            GuardarPassPerfil.setVisibility(View.GONE);
            InfoPerfil.setText("Password Cambiada Con Exito!");
    }

    @Override
    public void ErrorDescarga(Descargar descarga, int codigoError, String descripcion) {
        PassPerfil.setText(PassOld);
        PassConfirmPerfil.setVisibility(View.GONE);
        LblPassConfirmPerfil.setVisibility(View.GONE);
        CambioPassPerfil.setVisibility(View.VISIBLE);
        GuardarPassPerfil.setVisibility(View.GONE);
        InfoPerfil.setText("Hubo Problemas Para Cambiar El Password!");
    }

    @Override
    public void ProgresoDescarga(Float porcentaje) {

    }
}
