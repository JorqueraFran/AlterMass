package com.alertmass.appalertmass.alertmass;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.alertmass.appalertmass.alertmass.Conexion.Descargador;
import com.alertmass.appalertmass.alertmass.Conexion.Descargar;
import com.alertmass.appalertmass.alertmass.Conexion.IDescarga;
import com.alertmass.appalertmass.alertmass.Data.DataLogin;
import com.alertmass.appalertmass.alertmass.util.DescargaPaises;
import com.alertmass.appalertmass.alertmass.util.FuncionesUtiles;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    private TextView LblPassPerfil;
    private EditText TelefonoPerfil;
    public DataLogin datalogin;//= new DataLogin();
    public String PassOld;
    public String PassNew;
    public int isFacebook;
    private Spinner spinnerPais;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfiles);

        //datalogin= DataLogin.EntregarDataLogin();
        try{

            NombrePerfil = (EditText) findViewById(R.id.txtNombrePerfil);
            CorreoPerfil = (EditText) findViewById(R.id.txtCorreoPerfil);
            PassPerfil = (EditText) findViewById(R.id.txtPassPerfil);
            CambioPassPerfil = (Button) findViewById(R.id.btnCambiaPassPerfil);
            InfoPerfil = (TextView) findViewById(R.id.lblInfoPerfil);
            CerrarSesionPerfil = (Button) findViewById(R.id.btnCerrarSesion);
            PassConfirmPerfil = (EditText) findViewById(R.id.txtPassConfirm);
            LblPassConfirmPerfil = (TextView) findViewById(R.id.lblPassConfirm);
            GuardarPassPerfil =(Button) findViewById(R.id.btnGuardarPass);
            LblPassPerfil=(TextView) findViewById(R.id.lblPassPerfil);
            spinnerPais = (Spinner) findViewById(R.id.spinnerPaisPer);
            TelefonoPerfil = (EditText) findViewById(R.id.txtTelefonoPerfil);
            NombrePerfil.setText("");
            CorreoPerfil.setText("");
            PassPerfil.setText("");
            PassConfirmPerfil.setText("");


            if (FuncionesUtiles.IsSession(PerfilesActivity.this,null)){
               if(datalogin==null){
                   datalogin= DataLogin.EntregarDataLogin();

               }
            }
            FuncionesUtiles.CargarSpinnerPaises(PerfilesActivity.this, spinnerPais);
            FuncionesUtiles.SelPaisActual(spinnerPais,datalogin.GetIPaisUser());
            NombrePerfil.setText(datalogin.GetNombreUser());
            CorreoPerfil.setText(datalogin.GetCorreoUser());
            PassPerfil.setText(datalogin.GetPassUser());
            isFacebook = datalogin.GetIsFacebook();
            PassOld = datalogin.GetPassUser();
            TelefonoPerfil.setText(datalogin.GetTelefono());

            if(FuncionesUtiles.Boolean(isFacebook)){
                PassPerfil.setVisibility(View.GONE);
                CambioPassPerfil.setVisibility(View.GONE);
                LblPassPerfil.setVisibility(View.GONE);
                CerrarSesionPerfil.setText("Cerrar Sesion Facebook");
            }else{
                CerrarSesionPerfil.setText("Cerrar Sesion");
            }

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
        }catch (Exception e){
            FuncionesUtiles.LogError(e.getMessage().toString(),getApplicationContext());
        }

    }


    private void OnclickCerrarSesionPerfil(){
        AlertDialog.Builder alert = new AlertDialog.Builder(PerfilesActivity.this);
        alert.setTitle("Aviso");
        alert.setMessage("Esta seguro que desea cerrar sesion?");
        alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                FuncionesUtiles.DeleteSession();
                if (FuncionesUtiles.Boolean(isFacebook)) {
                    LoginManager.getInstance().logOut();
                }
                Intent intent = new Intent(PerfilesActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Setting Negative "NO" Button
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.show();

    }

    private void OnclickCambioPassPerfil() {

        PassPerfil.setHint("Nuevo Password");
        PassPerfil.setText("");
        PassConfirmPerfil.setVisibility(View.VISIBLE);
        LblPassConfirmPerfil.setVisibility(View.VISIBLE);
        CambioPassPerfil.setVisibility(View.GONE);
        GuardarPassPerfil.setVisibility(View.VISIBLE);
        //PassOld = PassPerfil.getText().toString();
        InfoPerfil.setText("");
        PassConfirmPerfil.setText("");
        GuardarPassPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean IsErrores = false;
                if (PassPerfil.getText().toString().isEmpty()) {
                    PassPerfil.setError("Requerido");
                    IsErrores = true;
                }
                if (PassConfirmPerfil.getText().toString().isEmpty()) {
                    PassConfirmPerfil.setError("Requerido");
                    IsErrores = true;
                }
                if (!PassPerfil.getText().toString().equals(PassConfirmPerfil.getText().toString())) {
                    PassConfirmPerfil.setError("Password No Coincide");
                    PassConfirmPerfil.setText("");
                    IsErrores = true;
                }
                if (PassOld.equals(PassConfirmPerfil.getText())) {
                    InfoPerfil.setText("El nuevo password debe ser distinto al antiguo.");
                    IsErrores = true;
                }
                if (!IsErrores) {
                    CambioPassword();
                }
            }
        });
    }

    private void CambioPassword(){
        PassNew=PassPerfil.getText().toString();
        isFacebook = datalogin.GetIsFacebook();
        try {
            String PerfilCorreo =  CorreoPerfil.getText().toString();
            final String headerPWD;
            if(isFacebook == 1){
                headerPWD= PerfilCorreo+"::true";
            }else{
                headerPWD= PerfilCorreo+":"+PassOld+":false";
            }
            //final String finalHeaderPWD = headerPWD;
            new AsyncTask<Void, Void, Boolean>() {
                ProgressDialog pDialog = new ProgressDialog(PerfilesActivity.this);

                byte[] data =headerPWD.getBytes("UTF-8");
                String headerPWDbase64 = Base64.encodeToString(data, Base64.DEFAULT).replace("\n", "");
                String ServicioLogin = getResources().getString(R.string.SERVICIO_CAMBIO_PWD);

                @Override
                protected Boolean doInBackground(Void... params) {

                    Descargador descargador = new Descargador();
                    Descargar descarga = new Descargar();
                    descarga.headersPWD = headerPWDbase64;
                    descarga.urlDescarga = ServicioLogin;
                    descarga.isPost = true;
                    descarga.varsPost.put("oldpass",PassOld);
                    descarga.varsPost.put("newpass",PassNew);
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
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
        String strJSON = null;
        try {
            strJSON = new String(data, "UTF-8");
            JSONObject jObject = new JSONObject(strJSON);
            if(jObject.getString("st").equals("OK")){
                PassPerfil.setHint("Password Perfil");
                PassPerfil.setText(PassNew);
                PassConfirmPerfil.setVisibility(View.GONE);
                LblPassConfirmPerfil.setVisibility(View.GONE);
                CambioPassPerfil.setVisibility(View.VISIBLE);
                GuardarPassPerfil.setVisibility(View.GONE);
                InfoPerfil.setText("Password Cambiada Con Exito!");
            }
        } catch (Exception ex) {
            InfoPerfil.setText("Hubo Problemas Para Cambiar El Password!");
        }
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
