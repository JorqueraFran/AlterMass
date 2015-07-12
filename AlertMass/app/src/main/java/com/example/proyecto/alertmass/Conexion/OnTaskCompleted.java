package com.example.proyecto.alertmass.Conexion;

/**
 * Created by Francisca on 11-07-2015.
 */
public interface OnTaskCompleted {

    public void onTaskCompleted(String[][] result);
    public void onTaskError(String result);
    public void onTaskPreExecute();
}
