package com.example.proyecto.alertmass.Data;

import com.example.proyecto.alertmass.util.FuncionesUtiles;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Francisca on 12-07-2015.
 */
public class DataLogin {
    private String Correo="";
    private String Pass="";
    private String Nombre="";
    private static JSONObject jObject;
    private static DataLogin datalogin;
    private static String correo;
    private static String nombre;
    private static String pass;
    //GET
    public String GetCorreoUser(){
        return this.Correo;
    }
    public String GetPassUser(){
        return this.Pass;
    }
    public String GetNombreUser(){
        return this.Nombre;
    }
    //SET
    public void SetCorreoUser(String correo){
        this.Correo = correo;
    }
    public void SetPassUser(String pass){
        this.Pass = pass;
    }
    public void SetNombreUser(String nombre){
        this.Nombre = nombre;
    }

    public DataLogin(){
        Correo=correo;
        Pass=pass;
        Nombre=nombre;
    }
    public DataLogin(JSONObject json)
    {
        try
        {
            this.SetCorreoUser(json.getString("email"));
            this.SetNombreUser(json.getString("name"));
            this.SetPassUser(json.getString("pass"));
        }catch (JSONException e)
        {
        }

    }
    public static boolean ProcesarJson(String json)
    {
        try
        {
            jObject = new JSONObject(json);
            datalogin = new DataLogin(jObject);
        }catch (JSONException e)
        {
            // TODO Auto-generated catch block
            return false;
        }
        return true;
    }
    public static void ProcesarSession(String _correo, String _nombre, String _pass)
    {
            correo = _correo;
            nombre = _nombre;
            pass = _pass;
            datalogin = new DataLogin();
    }
    public static DataLogin EntregarDataLogin()
    {
        return datalogin;
    }

}
