package com.fdgproject.firedge.aad_geoloc;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class ServicioIntent extends IntentService {
    //private static final String ACTION_FOO = "com.fdgproject.firedge.aad_geoloc.action.FOO";
    //private static final String ACTION_BAZ = "com.fdgproject.firedge.aad_geoloc.action.BAZ";

    //private static final String EXTRA_PARAM1 = "com.fdgproject.firedge.aad_geoloc.extra.PARAM1";
    //private static final String EXTRA_PARAM2 = "com.fdgproject.firedge.aad_geoloc.extra.PARAM2";

    private static final String ACTION_GEOCODE = "com.fdgproject.firedge.aad_geoloc.action.GEOCODE";
    private static final String EXTRA_PARAM1_LOC = "com.fdgproject.firedge.aad_geoloc.extra.LOCATION";
    private static Context ctx;

    public static void startActionGeoCode(Context context, Location location) {
        ctx = context;
        Intent intent = new Intent(context, ServicioIntent.class);
        intent.setAction(ACTION_GEOCODE);
        intent.putExtra(EXTRA_PARAM1_LOC, location);
        context.startService(intent);
    }

    public ServicioIntent() {
        super("ServicioIntent");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Se ejecuta en una hebra
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GEOCODE.equals(action)) {
                Location location = intent.getExtras().getParcelable(EXTRA_PARAM1_LOC);
                if(location!=null) {
                    handleActionGeoCode(location);
                }
            }
        }
    }

    private void handleActionGeoCode(Location location) {
        Geocoder geocoder = new Geocoder(ctx,Locale.getDefault());
        try {
            List<Address> direcciones =
                    geocoder.getFromLocation(
                            location.getLatitude(),
                            location.getLongitude(),
                            1);
            Address direccion = direcciones.get(0);
            Log.v("DIRSERVICE", direccion.toString());
            Intent intent = new Intent("DIRECCION");
            Bundle b = new Bundle();
            b.putParcelable("dir", direccion);
            b.putParcelable("loc", location);
            intent.putExtras(b);
            sendBroadcast(intent);
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("ERROR", e.toString());
        }

        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
