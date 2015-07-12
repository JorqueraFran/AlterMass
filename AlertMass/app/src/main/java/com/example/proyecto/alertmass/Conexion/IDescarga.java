package com.example.proyecto.alertmass.Conexion;

/**
 * Created by Francisca on 11-07-2015.
 */
public interface IDescarga {
    void TerminoDescarga(Descargar descarga, byte[] data);

    void ErrorDescarga(Descargar descarga, int codigoError, String descripcion);

    void ProgresoDescarga(Float porcentaje);
}
