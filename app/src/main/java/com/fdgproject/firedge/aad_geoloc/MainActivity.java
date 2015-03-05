package com.fdgproject.firedge.aad_geoloc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    private GoogleApiClient cliente;
    private Location ultimaLocalizacion;
    private LocationRequest peticionLocalizaciones;
    private ArrayList<String> fechas;
    private DataBase db;
    private GridViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
            cliente = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            cliente.connect();

        }
        db = new DataBase(this);
        fechas = db.getConsultaFechas();
        GridView gv = (GridView)findViewById(R.id.gv_principal);
        adapter = new GridViewAdapter(this, R.layout.row_grid, fechas);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<Localizacion> localizaciones = db.getConsultaUnica(fechas.get(i));
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                Bundle b = new Bundle();
                b.putParcelableArrayList("loc", localizaciones);
                intent.putExtras(b);
                startActivity(intent);

            }
        });
        registerForContextMenu(gv);
    }

    //Implements
    @Override
    public void onConnected(Bundle bundle) {
        ultimaLocalizacion = LocationServices.FusedLocationApi.getLastLocation(cliente);
        if (ultimaLocalizacion != null) {
            peticionLocalizaciones = new LocationRequest();
            peticionLocalizaciones.setInterval(60000);
            //peticionLocalizaciones.setFastestInterval(5000);
            peticionLocalizaciones.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationServices.FusedLocationApi.requestLocationUpdates(cliente,
                    peticionLocalizaciones, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        cliente.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        cliente.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        //tv.append(location.getLatitude()+"\n");
        //tv.append(location.getLongitude()+"\n");
        /*tv.append(location.getAccuracy()+"\n");
        tv.append(location.getAltitude()+"\n");
        tv.append(location.getSpeed()+"\n");
        tv.append(location.getBearing()+"\n");*/
        ServicioIntent.startActionGeoCode(this, location);

        //location.bearingTo(otraLocation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receptor, new IntentFilter("DIRECCION"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receptor);
    }

    private BroadcastReceiver receptor= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null){
                Bundle bundle = intent.getExtras();
                Location loc = bundle.getParcelable("loc");
                Address adr = bundle.getParcelable("dir");
                Localizacion localizacion = new Localizacion(new Date(), loc, adr.getLocality(), adr.getAddressLine(0));
                db.insertar(localizacion);
                fechas = db.getConsultaFechas();
                adapter.notifyDataSetChanged();
                //tv.append(localizacion.toString()+"\n");
            }
        }
    };

    /*                                  Context Menu                                            */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contextual, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int id = item.getItemId();
        int index = info.position;
        if(id == R.id.action_delete){
            delete(index);
        }
        return super.onContextItemSelected(item);
    }

    public boolean delete(final int index){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getResources().getString(R.string.delete_loc));
        alert.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String fecha = fechas.get(index);
                        ArrayList<Localizacion> loc = db.getConsultaUnica(fecha);
                        for(Localizacion l : loc){
                            db.delete(l);
                        }
                        fechas = db.getConsultaFechas();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.loc_deleted), Toast.LENGTH_SHORT).show();
                    }
                });
        alert.setNegativeButton(android.R.string.cancel,null);
        alert.show();
        return true;
    }
}
