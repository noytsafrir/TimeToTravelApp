package com.example.miniapppointsofinterest.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.miniapppointsofinterest.My_Screen_Utils;
import com.example.miniapppointsofinterest.My_Signal;
import com.example.miniapppointsofinterest.R;
import com.example.miniapppointsofinterest.api.ObjectApi;
import com.example.miniapppointsofinterest.api.RetrofitClient;
import com.example.miniapppointsofinterest.model.CurrentUser;
import com.example.miniapppointsofinterest.model.object.CreatedBy;
import com.example.miniapppointsofinterest.model.object.LocationBoundary;
import com.example.miniapppointsofinterest.model.object.ObjectBoundary;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.material.button.MaterialButton;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_Poing_Page extends AppCompatActivity {

    private RadioGroup addPoint_RAG_radiogroup;
    private AppCompatRadioButton addPoint_RAB_private;
    private AppCompatRadioButton addPoint_RAB_Public;
    private AppCompatEditText addPoint_EDT_pointName;
    private AppCompatEditText addPoint_EDT_description;
    private AppCompatEditText addPoint_EDT_picture;
    private Spinner addPoint_SPN_type;
    private MaterialButton addPoint_BTN_addPoint;
    private ObjectApi mApi;

    private MaterialButton addPoint_BTN_confirmLocation;
    private SupportMapFragment mapFragment;
    private GoogleMap myMap;
    private Marker selectedLocationMarker;
    private boolean isLocationSelected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_poing_page);
        My_Screen_Utils.hideSystemUI(this);
        mApi = RetrofitClient.getInstance().create(ObjectApi.class);
        startView();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        assert mapFragment != null;
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                myMap = googleMap;
                LatLng israel = new LatLng(31.0461, 34.8516);
                myMap.addMarker(new MarkerOptions().position(israel).title("Marker in israel"));
                myMap.moveCamera(CameraUpdateFactory.newLatLng(israel));

                myMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        // Clear any existing marker
                        if (selectedLocationMarker != null) {
                            selectedLocationMarker.remove();
                        }

                        // Add a marker at the clicked location
                        selectedLocationMarker = myMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title("Selected Location"));

                        // Update the flag to indicate a location is selected
                        isLocationSelected = true;
                    }
                });


                addPoint_BTN_confirmLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isLocationSelected) {
                            // Get the selected location LatLng
                            LatLng selectedLocation = selectedLocationMarker.getPosition();

                            // TODO: save the selected location in the database

                            // Show a toast message to indicate the selected location
                            String message = "Selected Location: " + selectedLocation.latitude + ", " + selectedLocation.longitude;
                            Toast.makeText(Add_Poing_Page.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Add_Poing_Page.this, "Please select a location on the map", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        addPoint_BTN_addPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int radioBtnId = addPoint_RAG_radiogroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) addPoint_RAG_radiogroup.findViewById(radioBtnId);
                String isPrivate = (String) radioButton.getText();
                createObject(addPoint_EDT_pointName.getText().toString(),10.5, 12.8 , isPrivate ,
                        addPoint_EDT_description.getText().toString(), addPoint_EDT_picture.getText().toString(),
                        addPoint_SPN_type.getSelectedItem().toString());
            }
        });
    }

    private void startView() {
        addPoint_RAG_radiogroup = findViewById(R.id.addPoint_RAG_radiogroup);
        addPoint_RAB_private= findViewById(R.id.addPoint_RAB_private);
        addPoint_RAB_Public= findViewById(R.id.addPoint_RAB_Public);
        addPoint_EDT_pointName = findViewById(R.id.addPoint_EDT_pointName);
        addPoint_EDT_description = findViewById(R.id.addPoint_EDT_description);
        addPoint_EDT_picture = findViewById(R.id.addPoint_EDT_picture);
        addPoint_SPN_type = findViewById(R.id.addPoint_SPN_type);
        addPoint_BTN_addPoint = findViewById(R.id.addPoint_BTN_addPoint);
        addPoint_BTN_confirmLocation = findViewById(R.id.addPoint_BTN_confirmLocation);
        String[] typesArray = getResources().getStringArray(R.array.types);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item,typesArray);
        addPoint_SPN_type.setAdapter(typeAdapter);
    }

    private void createObject(String alias, Double lat, Double lng, String privateOrPublic , String description , String pictures , String type) {
        ObjectBoundary newObject = new ObjectBoundary();
        newObject.setType("Point");
        newObject.setAlias(alias);
        newObject.setActive(true);
        newObject.setLocation(new LocationBoundary(lat, lng));
        newObject.setCreatedBy(new CreatedBy(CurrentUser.getInstance().getTheUser().getUserId()));
        HashMap<String, Object> details = new HashMap<String, Object>();
        details.put("privateOrPublic", privateOrPublic);
        details.put("description", description);
        details.put("pitcures", pictures);
        details.put("type", type);
        newObject.setObjectDetails(details);
        mApi.createObject(newObject).enqueue(new Callback<ObjectBoundary>() {
            @Override
            public void onResponse(Call<ObjectBoundary> call, Response<ObjectBoundary> response) {
                if (response.isSuccessful()) {
                    createNewObject(response.body());
                } else {
                    My_Signal.getInstance().toast("API call failed: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<ObjectBoundary> call, Throwable t) {
                My_Signal.getInstance().toast("API call failed: " + t.getMessage());
            }
        });
    }

    private void createNewObject(ObjectBoundary body){
        My_Signal.getInstance().toast("the point added");
        Intent intent = new Intent(this, Home_Page.class);
        startActivity(intent);    }
}