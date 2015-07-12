package com.example.proyecto.alertmass;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyecto.alertmass.Conexion.Descargador;
import com.example.proyecto.alertmass.Conexion.Descargar;
import com.example.proyecto.alertmass.Conexion.IDescarga;
import com.example.proyecto.alertmass.Data.DataLogin;
import com.example.proyecto.alertmass.util.FuncionesUtiles;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements LoaderCallbacks<Cursor>, IDescarga {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView lblOvido;
    private Button btnIngresar;
    private Button btnIngresarFace;
    private Button btnRegistrar;
    private DataLogin datalogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.txt_EmailRecover);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.txt_Password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.btn_Entrar);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        //mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        btnRegistrar = (Button) findViewById(R.id.btn_Registro);
        btnRegistrar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in,R.anim.left_out);
            }
        });

        lblOvido = (TextView) findViewById(R.id.lbl_Olvido);
        lblOvido.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Intent intent = new Intent(LoginActivity.this, PassRecoverActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_in,R.anim.left_out);
                }
                return false;
            }
        });


    }


    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;

        } else if (!FuncionesUtiles.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            new AsyncTask<Void, Void, Boolean>() {
                ProgressDialog pDialog = new ProgressDialog(LoginActivity.this);
                String CorreoLogin =mEmailView.getText().toString();
                String PasswordLogin = mPasswordView.getText().toString();
                String ServicioLogin = getResources().getString(R.string.SERVICIOTODOSUSUARIOS) +"/"+ CorreoLogin;

                @Override
                protected Boolean doInBackground(Void... params) {
                    Descargador descargador = new Descargador();
                    Descargar descarga = new Descargar();
                    descarga.urlDescarga = ServicioLogin;
                    descarga.isPost = false;
                    descarga.callback = LoginActivity.this;
                    descargador.execute(descarga);
                    return true;
                }

                @Override
                protected void onPreExecute() {
                    FuncionesUtiles.ProgressDialog(pDialog, "Ingresando...");
                }

                @Override
                protected void onPostExecute(final Boolean success) {
                    FuncionesUtiles.CancelarDialog(pDialog);

                    /*if (success) {
                        finish();
                    } else {
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();
                    }*/
                }

                @Override
                protected void onCancelled() {
                    FuncionesUtiles.CancelarDialog(pDialog);
                }
            }.execute();
        }
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    @Override
    public void TerminoDescarga(Descargar descarga, byte[] data) {
        try
        {
            //{ "st" : "NOPROC" }
            // procesa json de respuesta para validar el login
            String strJSON = new String(data, "UTF-8");
            JSONObject json= new JSONObject(strJSON);
            DataLogin.ProcesarJson(strJSON);
            datalogin=DataLogin.EntregarDataLogin();
            String Pass = mPasswordView.getText().toString();
            if(Pass.equals(datalogin.GetPassUser())){
                Toast.makeText(getApplicationContext(), "Bienvenido a AlertMass!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, NotificacionesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }else {
                FuncionesUtiles.ToastMensaje(this, "Password Incorrecto");
                mPasswordView.setText("");
                mPasswordView.setError("Password Incorrecto");
            }


        }
        catch (Exception ex)
        {
            FuncionesUtiles.ToastMensaje(this, "Error al Ingresar");
            return;
        }

    }

    @Override
    public void ErrorDescarga(Descargar descarga, int codigoError, String descripcion) {
        FuncionesUtiles.ToastMensaje(this, "Error al Ingresar");
    }

    @Override
    public void ProgresoDescarga(Float porcentaje) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


}
