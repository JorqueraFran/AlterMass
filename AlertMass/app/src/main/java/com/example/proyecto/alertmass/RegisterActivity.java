package com.example.proyecto.alertmass;

import com.example.proyecto.alertmass.Conexion.Descargador;
import com.example.proyecto.alertmass.Conexion.Descargar;
import com.example.proyecto.alertmass.Conexion.IDescarga;
import com.example.proyecto.alertmass.util.FuncionesUtiles;
import com.example.proyecto.alertmass.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


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
    private Button btnRegistrar;
       @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

           txtCorreo  = (EditText) findViewById(R.id.txt_EmailRegister);
           txtNombre = (EditText) findViewById(R.id.txt_UserName);
           txtPass = (EditText) findViewById(R.id.txt_Password);
           btnRegistrar = (Button) findViewById(R.id.btn_Register);
           btnRegistrar.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Registrar();
               }
           });

    }

    private void Registrar(){
        String Nombre= txtNombre.getText().toString();
        String Correo = txtCorreo.getText().toString();
        String Pass = txtPass.getText().toString();
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
        //validacion password
        if(Pass.isEmpty()){
            txtPass.setError("Requerido");
            TieneErrores =true;
        }
        if(!TieneErrores){
            new AsyncTask<Void, Void, Boolean>() {
                ProgressDialog pDialog = new ProgressDialog(RegisterActivity.this);

                @Override
                protected Boolean doInBackground(Void... params) {
                    Descargador descargador = new Descargador();
                    Descargar descarga = new Descargar();
                    descarga.urlDescarga = getResources().getString(R.string.SERVICIOCREARUSUARIO);
                    descarga.isPost = true;
                    descarga.varsPost.put("email", RegisterActivity.this.txtCorreo.getText().toString());
                    descarga.varsPost.put("name", RegisterActivity.this.txtNombre.getText().toString());
                    descarga.varsPost.put("pass", RegisterActivity.this.txtPass.getText().toString());
                    descarga.varsPost.put("secname", RegisterActivity.this.txtNombre.getText().toString());
                    descarga.varsPost.put("mobile", "56912345678");
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
        Toast.makeText(getApplicationContext(), "Usuario Registrado Correctamente", Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    public void ErrorDescarga(Descargar descarga, int codigoError, String descripcion) {
        Toast.makeText(this,"Error al Registrar " + descarga.msgServidor,Toast.LENGTH_LONG).show();

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
