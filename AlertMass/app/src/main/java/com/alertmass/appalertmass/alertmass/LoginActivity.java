package com.alertmass.appalertmass.alertmass;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.tv.TvInputService;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Base64;
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

import com.alertmass.appalertmass.alertmass.Conexion.Descargador;
import com.alertmass.appalertmass.alertmass.Conexion.Descargar;
import com.alertmass.appalertmass.alertmass.Conexion.IDescarga;
import com.alertmass.appalertmass.alertmass.Data.DataLogin;
import com.alertmass.appalertmass.alertmass.util.FuncionesUtiles;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private CallbackManager callbackManager;
    private LoginButton btnIngresoFace;
    /*private SessionApp datasession;
    private String usersession;
    private String passsession;
    private String correosession;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.proyecto.alertmass",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        btnIngresoFace = (LoginButton) findViewById(R.id.btn_IngresoFace);


        datalogin= DataLogin.EntregarDataLogin();
        if (FuncionesUtiles.IsSession(LoginActivity.this,null)){
            DataLogin.ProcesarSession(FuncionesUtiles.correosession, FuncionesUtiles.usersession,FuncionesUtiles.passsession,FuncionesUtiles.estadosession,FuncionesUtiles.isfacebooksession, FuncionesUtiles.paissession, FuncionesUtiles.telefonosession);
            datalogin= DataLogin.EntregarDataLogin();
            //datalogin.SetNombreUser(usersession);
            //datalogin.SetCorreoUser(correosession);
           //datalogin.SetPassUser(passsession);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        FacebookLogin();

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.txt_EmailRecover);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.txt_Password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    if(FuncionesUtiles.verificaConexion(getApplicationContext())){
                        attemptLogin();
                    }else{
                        FuncionesUtiles.AvisoSinConexion(LoginActivity.this);
                    }
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.btn_Entrar);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FuncionesUtiles.verificaConexion(getApplicationContext())){
                    attemptLogin();
                }else{
                    FuncionesUtiles.AvisoSinConexion(LoginActivity.this);
                }
            }
        });

        //mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        btnRegistrar = (Button) findViewById(R.id.btn_Registro);
        btnRegistrar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FuncionesUtiles.verificaConexion(getApplicationContext())){
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_in,R.anim.left_out);
                }else{
                   FuncionesUtiles.AvisoSinConexion(LoginActivity.this);
                }

            }
        });

        lblOvido = (TextView) findViewById(R.id.lbl_Olvido);
        lblOvido.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if(FuncionesUtiles.verificaConexion(getApplicationContext())){
                            Intent intent = new Intent(LoginActivity.this, PassRecoverActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in,R.anim.left_out);
                        }else{
                            FuncionesUtiles.AvisoSinConexion(LoginActivity.this);
                        }

                }
                return false;
            }
        });


    }

    private void FacebookLogin(){
        btnIngresoFace.setPadding(20, 20, 20, 20);
        callbackManager = CallbackManager.Factory.create();
            //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));
            //LoginManager.getInstance().registerCallback(callbackManager,
        btnIngresoFace.setText("Ingresar con Facebook");
            btnIngresoFace.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email"));
            btnIngresoFace.registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {

                            AccessToken accessToken = loginResult.getAccessToken();
                            Profile profile = Profile.getCurrentProfile();
                            GraphRequest request = GraphRequest.newMeRequest(accessToken,
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                                            Log.e("JSON-FACEBOOK", user.toString());
                                            DataLogin.ProcesarSession(user.optString("email"), user.optString("name"), "", 1, 1,"","");
                                        }
                                    });

                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name,email");

                            request.setParameters(parameters);
                            request.executeAsync();
                        /*Profile profile = Profile.getCurrentProfile();
                        DataLogin.ProcesarSession("",profile.getName(),"");*/
                        /*GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                                Log.v("LoginActivity", graphResponse.toString());
                            }
                        });*/
                            FuncionesUtiles.ToastMensaje(LoginActivity.this, "Bienvenido a AlertMass!");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }

                        @Override
                        public void onCancel() {
                            // App code
                            FuncionesUtiles.ToastMensaje(LoginActivity.this, "Inicio Cancelado");
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // App code
                            FuncionesUtiles.ToastMensaje(LoginActivity.this, "Error al Ingresar");
                        }
                    });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //AppEventsLogger.deactivateApp(this);
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
        if (!TextUtils.isEmpty(password) && !FuncionesUtiles.isPasswordValid(password)) {
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
            try{
                new AsyncTask<Void, Void, Boolean>() {
                    ProgressDialog pDialog = new ProgressDialog(LoginActivity.this);
                    String CorreoLogin =mEmailView.getText().toString();
                    String PasswordLogin = mPasswordView.getText().toString();
                    String ServicioLogin = getResources().getString(R.string.SERVICIO_TODOS_USUARIOS) +"/" + CorreoLogin;
                    String headerPWD = CorreoLogin+":"+PasswordLogin;
                    byte[] data = headerPWD.getBytes("UTF-8");
                    String headerPWDbase64 = Base64.encodeToString(data, Base64.DEFAULT).replace("\n", "");
                    @Override
                    protected Boolean doInBackground(Void... params) {
                        Descargador descargador = new Descargador();
                        Descargar descarga = new Descargar();
                        descarga.urlDescarga = ServicioLogin;
                        //descarga.headersPWD = headerPWDbase64;
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
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }


   /* private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }*/


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

    private void IraMain(){
        mPasswordView.setText("");
        mEmailView.setText("");
        FuncionesUtiles.SetSession(datalogin.GetNombreUser(), datalogin.GetPassUser(), datalogin.GetCorreoUser(),datalogin.GetEstado(),datalogin.GetIsFacebook(), datalogin.GetPaisUser(),datalogin.GetTelefono());
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
        FuncionesUtiles.ToastMensaje(getApplicationContext(), "Bienvenido a AlertMass!");

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
                IraMain();
            }else {
                //FuncionesUtiles.ToastMensaje(this, "Password Incorrecto");
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

   /* private boolean IsSession()
    {
        datasession = new SessionApp(this, "AlertMass", null, 1);
        SQLiteDatabase db = datasession.getWritableDatabase();

        if (db != null)
        {
            //db.execSQL("delete from Usuario");
            Cursor c = db.rawQuery("SELECT usr, pwr, correo FROM Usuario", null);
            int count = c.getCount();
            if (c.getCount() > 0)
            {
                if (c.moveToFirst())
                {
                    do
                    {
                        usersession = c.getString(0);
                        passsession = c.getString(1);
                        correosession = c.getString(2);
                    }
                    while (c.moveToNext());
                }

                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    private void SetSession(String usr, String pwr, String Correo)
    {

        SQLiteDatabase db = datasession.getWritableDatabase();

        if (db != null)
        {
            db.execSQL("DELETE FROM Usuario");
            db.execSQL("INSERT INTO Usuario (usr, pwr, correo) VALUES ('" + usr + "', '" + pwr + "' , '" + Correo + "')");
            db.close();
        }
    }*/
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
}

