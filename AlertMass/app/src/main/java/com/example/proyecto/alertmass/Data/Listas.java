package com.example.proyecto.alertmass.Data;

/**
 * Created by C-266 on 24/07/2015.
 */
public class Listas {
    protected long Id;
    protected String Imagen;
    protected String Title;
    protected String SubTitle;

    public Listas(){
        this.Id=0;
        this.Imagen="";
        this.Title="";
        this.SubTitle="";
    }

    public Listas(long Id,String Title){
        this.Id = Id;;
        this.Title=Title;
        this.SubTitle="";
    }

    public Listas(long Id,String Title, String SubTitle){
        this.Id = Id;
        this.Title=Title;
        this.SubTitle=SubTitle;
    }

    public long GetId(){return Id;}
    public void SetId(long Id){this.Id=Id;}

    public String GetImagen(){return Imagen;}
    public void SetImagen(String Imagen){this.Imagen=Imagen;}

    public String GetTitle(){return Title;}
    public void SetTitle(String Title){this.Title=Title;}

    public String GetSubTitle(){return SubTitle;}
    public void SetSubTitle(String SubTitle){this.SubTitle=SubTitle;}
}
