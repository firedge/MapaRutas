package com.fdgproject.firedge.aad_geoloc;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Firedge on 06/02/2015.
 */
public class Localizacion implements Parcelable{

    private Date fecha;
    private Location localizacion;
    private String localidad;
    private String calle;

    public Localizacion(){
        this.fecha = null;
        this.localizacion = null;
        this.localidad = "";
        this.calle = "";
    }

    public Localizacion(Date fecha, Location localizacion){
        this.fecha = fecha;
        this.localizacion = localizacion;
        this.localidad = "";
        this.calle = "";
    }

    public Localizacion(Date fecha, Location localizacion, String localidad, String calle) {
        this.fecha = fecha;
        this.localizacion = localizacion;
        this.localidad = localidad;
        this.calle = calle;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Location getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(Location localizacion) {
        this.localizacion = localizacion;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String formatoFecha(){
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
        return dt.format(fecha);
    }

    @Override
    public String toString() {
        return fecha.toString() + "\n" +
                "(" + localizacion.getLatitude() + ", " + localizacion.getLongitude() + ")" + "\n" +
                "Dirección: " + calle + " - " + localidad;
    }

    /**************************** Para hacerlo Parcelable ************************************/

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        //El orden es muy importante, porque a la hora de coger los elementos, se cogen por orden.
        parcel.writeSerializable(this.fecha);
        parcel.writeParcelable(localizacion, 0);
        parcel.writeString(this.localidad);
        parcel.writeString(this.calle);
    }

    public Localizacion(Parcel p){
        this.fecha = (Date) p.readSerializable();
        this.localizacion = p.readParcelable(Location.class.getClassLoader());
        this.localidad = p.readString();
        this.calle = p.readString();
    }

    public static final Parcelable.Creator<Localizacion> CREATOR =
            new Parcelable.Creator<Localizacion>(){

                @Override
                public Localizacion createFromParcel(Parcel parcel) {
                    return new Localizacion(parcel);
                }

                @Override
                public Localizacion[] newArray(int i) {
                    return new Localizacion[i];
                }
            };
}

