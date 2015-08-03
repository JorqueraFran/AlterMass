package com.alertmass.appalertmass.alertmass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.alertmass.appalertmass.alertmass.Conexion.Descargador;
import com.alertmass.appalertmass.alertmass.Conexion.Descargar;
import com.alertmass.appalertmass.alertmass.Conexion.IDescarga;
import com.alertmass.appalertmass.alertmass.Data.DataLogin;
import com.alertmass.appalertmass.alertmass.util.DescargaPaises;
import com.alertmass.appalertmass.alertmass.util.FuncionesUtiles;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class RegisterActivity extends Activity implements IDescarga {

    private EditText txtNombre;
    private EditText txtCorreo;
    private EditText txtPass;
    private EditText txtTelefono;
    private Button btnRegistrar;
    private Spinner spinnerPais;
    String IdPais;

       @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        try{

           txtCorreo  = (EditText) findViewById(R.id.txt_EmailRegister);
           txtNombre = (EditText) findViewById(R.id.txt_UserName);
           txtPass = (EditText) findViewById(R.id.txt_Password);
           spinnerPais = (Spinner) findViewById(R.id.spinnerPais);
           txtTelefono =(EditText) findViewById(R.id.txtTelefonoPerfil);
           FuncionesUtiles.CargarSpinnerPaises(RegisterActivity.this, spinnerPais);

           btnRegistrar = (Button) findViewById(R.id.btn_Register);
           btnRegistrar.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Registrar();
               }
           });
        }catch (Exception e){
            FuncionesUtiles.LogError(e.getMessage().toString(),getApplicationContext());
        }

    }

    private void Registrar(){
        String Nombre= txtNombre.getText().toString();
        String Correo = txtCorreo.getText().toString();
        String Pass = txtPass.getText().toString();
        String Phone =txtTelefono.getText().toString();
        IdPais = DataLogin.GetIPaisUser(spinnerPais.getSelectedItem().toString());
        Boolean TieneErrores=false;
        //validacion nombre
        if(Nombre.isEmpty()){
            txtNombre.setError("Requerido");
            TieneErrores =true;
        }else if(!FuncionesUtiles.isEmailValid(Correo)){
            txtCorreo.setError(getString(R.string.error_invalid_email));
            TieneErrores =true;
        }
        //validacion correo
        if(Correo.isEmpty()){
            txtCorreo.setError("Requerido");
            TieneErrores =true;
        }
        if(Phone.isEmpty()){
            txtTelefono.setError("Requerido");
            TieneErrores =true;
        }
        //validacion password
        if(!TextUtils.isEmpty(txtPass.getText().toString()) && !FuncionesUtiles.isPasswordValid(txtPass.getText().toString())){
            txtPass.setError(getString(R.string.error_invalid_password));
            TieneErrores =true;
        }
        if(!TieneErrores){
            new AsyncTask<Void, Void, Boolean>() {
                ProgressDialog pDialog = new ProgressDialog(RegisterActivity.this);

                @Override
                protected Boolean doInBackground(Void... params) {
                    Descargador descargador = new Descargador();
                    Descargar descarga = new Descargar();
                    descarga.urlDescarga = getResources().getString(R.string.SERVICIO_CREAR_USUARIO);
                    descarga.isPost = true;
                    descarga.varsPost.put("email", RegisterActivity.this.txtCorreo.getText().toString());
                    descarga.varsPost.put("name", RegisterActivity.this.txtNombre.getText().toString());
                    descarga.varsPost.put("pass", RegisterActivity.this.txtPass.getText().toString());
                    descarga.varsPost.put("secname", RegisterActivity.this.txtNombre.getText().toString());
                    descarga.varsPost.put("mobile", RegisterActivity.this.txtTelefono.getText().toString());
                    descarga.varsPost.put("codpais",IdPais);
                    descarga.callback = RegisterActivity.this;
                    descargador.execute(descarga);
                    return true;
                }

                @Override
                protected void onPreExecute() {
                    pDialog.setMessage("Registrando...");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(false);
                    pDialog.show();
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    pDialog.dismiss();

                }
            }.execute();
        }
    }


    @Override
    public void TerminoDescarga(Descargar descarga, byte[] data) {
        String strJSON = null;

        FuncionesUtiles.ToastMensaje(this, "Usuario Registrado Correctamente");
        //finish();
        FuncionesUtiles.SetSession(txtNombre.getText().toString(), txtPass.getText().toString(), txtCorreo.getText().toString(),1,0,"",txtTelefono.getText().toString());
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);

    }

    @Override
    public void ErrorDescarga(Descargar descarga, int codigoError, String descripcion) {

        FuncionesUtiles.ToastMensaje(this, "Error al Registrar");

    }

    @Override
    public void ProgresoDescarga(Float porcentaje) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK)
        {
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
        }
        return super.onKeyDown(keyCode, event);
    }
}
