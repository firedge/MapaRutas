package com.fdgproject.firedge.aad_geoloc;

import android.content.Context;
import android.view.View;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Firedge on 04/03/2015.
 */
public class DataBase {

    private ObjectContainer bd;

    public DataBase(Context ctx) {
        bd = Db4oEmbedded.openFile(
                Db4oEmbedded.newConfiguration(), ctx.getExternalFilesDir(null) +
                        "/bd.db4o");
    }

    public void insertar(Localizacion loc){
        bd.store(loc);
        bd.commit();
    }

    public ArrayList<Localizacion> getConsultaUnica(String s){
        Query consulta = bd.query();
        consulta.constrain(Localizacion.class);
        ObjectSet<Localizacion> loc = consulta.execute();
        ArrayList<Localizacion> lista = new ArrayList<Localizacion>();
        for(Localizacion l: loc){
            if(l.formatoFecha().equals(s)){
                lista.add(l);
            }
        }
        return lista;
    }

    public ArrayList<String> getConsultaFechas(){
        Query consulta = bd.query();
        consulta.constrain(Localizacion.class);
        ObjectSet<Localizacion> loc = consulta.execute();
        ArrayList<String> lista = new ArrayList<String>();
        for(Localizacion l: loc){
            String fecha = l.formatoFecha();
            if(!lista.contains(fecha)){
                lista.add(fecha);
            }
        }
        return lista;
    }

    public void update(Localizacion loc){
        bd.store(loc);
        bd.commit();
    }

    public void delete(Localizacion loc){
        bd.delete(loc);
        bd.commit();
    }

    public void close(){
        bd.close();
    }
}
