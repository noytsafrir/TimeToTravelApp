package com.example.miniapppointsofinterest.fragments;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.miniapppointsofinterest.CallBack_List;
import com.example.miniapppointsofinterest.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    private MapFragment mapFragment;


    CallBack_List callBack_list = new CallBack_List() {
        @Override
        public void setMapLocation(double lat, double lon, String namePlayer) {
            mapFragment.setMapLocation(lat, lon,namePlayer);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_fragment);
        mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.addPoint_LAY_map,mapFragment).commit();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        LatLng sydney = new LatLng(-34, 151);
        myMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        myMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}