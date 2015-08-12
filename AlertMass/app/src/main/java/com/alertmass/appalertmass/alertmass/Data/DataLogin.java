package com.alertmass.appalertmass.alertmass.Data;

import com.alertmass.appalertmass.alertmass.util.FuncionesUtiles;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Francisca on 12-07-2015.
 */
public class DataLogin {
    private String Correo="";
    private String Pass="";
    private String Nombre="";
    private String IdPais="";
    private String Pais="";
    private String Telefono="";
    private static HashMap<String,String> Paises= new HashMap<String,String>();
    private int IsFacebook = 0;
    private int Estado = 0;
    private static JSONObject jObject;
    private static DataLogin datalogin;
    private static String correo;
    private static String nombre;
    private static String pass;
    private static String idpais;
    private static String pais;
    private static String telefono;
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
    public String GetIPaisUser(){
        return this.IdPais;
    }
    public static String GetPaisUser(String Nom){
        return Paises.get(Nom);
    }
    public String GetTelefono(){
        return this.Telefono;
    }
    public int GetIsFacebook(){
        return this.IsFacebook;
    }
    public int GetEstado(){
        return this.Estado;
    }
    public static HashMap<String,String> GetPaises(){
        return Paises;
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
    public void SetIdPaisUser(String idpais){
        this.IdPais = idpais;
    }
    public void SetPaisUser(String pais){
        this.Pais = pais;
    }
    public void SetTelefono(String phone){
        this.Telefono = phone;
    }
    public void SetIsFacebook(int isfacebook){
        this.IsFacebook = isfacebook;
    }
    public void SetEstado(int estado){
        this.estado = estado;
    }
    public static void SetPaises(HashMap<String,String> paises){
        Paises = paises;
    }

    public DataLogin(){
        Correo=correo;
        Pass=pass;
        Nombre=nombre;
        Pais =pais;
        IdPais =idpais;
        Telefono=telefono;
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
            this.SetIdPaisUser(json.getString("codpais"));
            //this.SetTelefono(json.getString("mobile"));
            //this.SetPaisUser("Chile");
            this.SetTelefono(json.getString("mobile"));
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
    public static void ProcesarSession(String _correo, String _nombre, String _pass,int _estado, int _isfacebook, String _telefono,String _pais)
    {
            correo = _correo;
            nombre = _nombre;
            pass = _pass;
            isfacebook=_isfacebook;
            estado = _estado;
            telefono=_telefono;
            idpais = _pais;
            datalogin = new DataLogin();
            FuncionesUtiles.SetSession(nombre,pass,correo,estado,isfacebook,idpais,telefono);
    }
    public static DataLogin EntregarDataLogin()
    {
        DataLogin _datalogin;
        if(datalogin==null){
            DataLogin.ProcesarSession(FuncionesUtiles.correosession, FuncionesUtiles.usersession,FuncionesUtiles.passsession,FuncionesUtiles.estadosession,FuncionesUtiles.isfacebooksession, FuncionesUtiles.telefonosession, FuncionesUtiles.paissession);
            _datalogin= datalogin;
        }else{
            _datalogin= datalogin;
        }
        return _datalogin;
    }

}
