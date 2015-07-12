package com.example.proyecto.alertmass.Data;

import com.example.proyecto.alertmass.util.FuncionesUtiles;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Francisca on 12-07-2015.
 */
public class DataLogin {
    private String Correo;
    private String Pass;
    private String Nombre;
    private boolean IsSession;
    private static JSONObject jObject;
    private static DataLogin datalogin;

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
    public boolean GetSessionUser(){
        return this.IsSession;
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
    public void SetSessionUser(boolean session){
        this.IsSession = session;
    }
    public DataLogin(JSONObject json)
    {
        try
        {
            this.SetCorreoUser(json.getString("email"));
            this.SetNombreUser(json.getString("name"));
            this.SetPassUser(json.getString("pass"));
            this.SetSessionUser(true);
        }catch (JSONException e)
        {
            this.SetSessionUser(false);
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
    public static DataLogin EntregarDataLogin()
    {
        return datalogin;
    }

}
