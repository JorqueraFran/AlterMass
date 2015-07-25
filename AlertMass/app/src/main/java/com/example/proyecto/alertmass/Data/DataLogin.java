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
    private int IsFacebook = 0;
    private int Estado = 0;
    private static JSONObject jObject;
    private static DataLogin datalogin;
    private static String correo;
    private static String nombre;
    private static String pass;
    private static int isfacebook;
    private static int estado;
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
    public int GetIsFacebook(){
        return this.IsFacebook;
    }
    public int GetEstado(){
        return this.Estado;
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
    public void SetIsFacebook(int isfacebook){
        this.IsFacebook = isfacebook;
    }
    public void SetEstado(int estado){
        this.estado = estado;
    }

    public DataLogin(){
        Correo=correo;
        Pass=pass;
        Nombre=nombre;
        IsFacebook = isfacebook;
        Estado = estado;
    }
    public DataLogin(JSONObject json)
    {
        try
        {
            this.SetCorreoUser(json.getString("email"));
            this.SetNombreUser(json.getString("name"));
            this.SetPassUser(json.getString("pass"));
            this.SetEstado(1);
            this.SetIsFacebook(0);
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
    public static void ProcesarSession(String _correo, String _nombre, String _pass,int _estado, int _isfacebook)
    {
            correo = _correo;
            nombre = _nombre;
            pass = _pass;
            isfacebook=_isfacebook;
            estado = _estado;
            datalogin = new DataLogin();
            FuncionesUtiles.SetSession(nombre,pass,correo,estado,isfacebook);
    }
    public static DataLogin EntregarDataLogin()
    {
        return datalogin;
    }

}
