package com.alertmass.appalertmass.alertmass.Data;

/**
 * Created by C-266 on 24/07/2015.
 */
public class Listas {
    protected long Id;
    protected String IdObj;
    protected String Imagen;
    protected String Title;
    protected String SubTitle;
    protected String Fecha;

    public Listas(){
        this.Id=0;
        this.Imagen="";
        this.IdObj="";
        this.Title="";
        this.SubTitle="";
        this.Fecha="";
    }

    public Listas(long Id,String Title){
        this.Id = Id;;
        this.Title=Title;
        this.SubTitle="";
    }

    public Listas(long Id,String IdObj,String Title, String SubTitle, String Fecha){
        this.Id = Id;
        this.Title=Title;
        this.SubTitle=SubTitle;
        this.Fecha=Fecha;
        this.IdObj=IdObj;
    }

    public long GetId(){return Id;}
    public void SetId(long Id){this.Id=Id;}

    public String GetImagen(){return Imagen;}
    public void SetImagen(String Imagen){this.Imagen=Imagen;}

    public String GetTitle(){return Title;}
    public void SetTitle(String Title){this.Title=Title;}

    public String GetSubTitle(){return SubTitle;}
    public void SetSubTitle(String SubTitle){this.SubTitle=SubTitle;}

    public String GetSubFecha(){return Fecha;}
    public void SetFecha(String Fecha){this.Fecha=Fecha;}

    public String GetIdObj(){return IdObj;}
    public void SetIdObj(String IdObj){this.IdObj=IdObj;}
}
