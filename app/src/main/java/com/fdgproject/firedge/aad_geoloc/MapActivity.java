package com.fdgproject.firedge.aad_geoloc;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


public class MapActivity extends Activity implements OnMapReadyCallback {

    private ArrayList<Localizacion> loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle b = getIntent().getExtras();
        loc = b.getParcelableArrayList("loc");

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng pos = new LatLng(loc.get(0).getLocalizacion().getLatitude(), loc.get(0).getLocalizacion().getLongitude());

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 13));

        for(int i = 0; i<loc.size(); i++) {
            pos = new LatLng(loc.get(i).getLocalizacion().getLatitude(), loc.get(i).getLocalizacion().getLongitude());
            map.addMarker(new MarkerOptions()
                    .title(loc.get(i).getLocalidad())
                    .snippet(loc.get(i).getCalle())
                    .position(pos));

            if(i-1>0) {
                PolylineOptions rectOptions = new PolylineOptions()
                        .add(new LatLng(loc.get(i-1).getLocalizacion().getLatitude(), loc.get(i-1).getLocalizacion().getLongitude()))
                        .add(new LatLng(loc.get(i).getLocalizacion().getLatitude(), loc.get(i).getLocalizacion().getLongitude()));

                map.addPolyline(rectOptions);
            }
        }
    }
}
